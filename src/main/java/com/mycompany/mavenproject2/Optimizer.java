package com.mycompany.mavenproject2;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author refreshjss
 */
public class Optimizer {

    private ArrayList<Pair> lexems;
    private ArrayList<Pair> tableOfName;
    private ParseTree tree;
    private ArrayList<String> warnings;

    Optimizer(ParseTree tree, ArrayList<Pair> table, ArrayList<Pair> lexems) {
        this.lexems = lexems;
        this.tableOfName = table;
        this.tree = tree;
        this.warnings = new ArrayList();
    }

    public void optimize() throws Exception {
        setCountOfLink(this.tree.getRoot().getChilds());
        collectWarning();
        deleteDeadCode();
        optimizeExpression(this.tree.getRoot().getChilds().get(1).getChilds().get(1).getChilds());
        sendWarning();
        this.lexems = this.tree.getLexem();
    }

    private boolean deleteVar(Pair var, ArrayList<TreeItem> varList) throws Exception {
        boolean success = false;
        int i = 0;
        while (!success && i < varList.size()) {
            if (varList.get(i).getVal().getName().equals("раздел описания")
                    || varList.get(i).getVal().getName().equals("список имен")) {
                success = deleteVar(var, varList.get(i).getChilds());
                i++;
                continue;
            }
            if (varList.get(i).getVal().equals(var)) {
                success = true;
                TreeItem parent = varList.get(i).getParent();
                if (parent.getVal().getName().equals("список имен")) {
                    if (parent.getChilds().size() > 1) {
                        if (i != parent.getChilds().size() - 1) {
                            varList.remove(i + 1);
                        }
                        varList.remove(i);
                    } else {
                        TreeItem grandparent = parent.getParent();
                        TreeItem lastItem = grandparent.getChilds().get(grandparent.getChilds().size() - 1);
                        if (lastItem.getVal().getName().equals("раздел описания")) {
                            grandparent.setChilds(lastItem.getChilds());
                            lastItem.setParent(grandparent);
                        } else {
                            TreeItem grandgrandparent = grandparent.getParent();
                            if (grandparent.getVal().getName().equals("программа")) {
                                throw new Exception("В программе отсутствуют используемые переменные");
                            }
                            grandgrandparent.getChilds().remove(grandparent);
                        }
                    }
                }
                if (parent.getVal().getName().equals("раздел описания")) {
                    TreeItem lastItem = varList.get(varList.size() - 1);
                    if (lastItem.getVal().getName().equals("раздел описания")) {
                        parent.setChilds(lastItem.getChilds());
                        lastItem.setParent(parent);
                    } else {
                        TreeItem grandparent = parent.getParent();
                        if (grandparent.getVal().getName().equals("программа")) {
                            throw new Exception("В программе отсутствуют используемые переменные");
                        }
                        grandparent.getChilds().remove(parent);
                    }
                }
            }
            i++;
        }
        return success;
    }

    public ParseTree getTree() {
        return this.tree;
    }

    private void setCountOfLink(ArrayList<TreeItem> childs) {
        for (int i = 0; i < childs.size(); i++) {
            Pair current = childs.get(i).getVal();
            if (current.getType().equals("id")
                    && (childs.get(i).getParent().getVal().getName().equals("операнд F")
                    || childs.get(i).getParent().getVal().getName().equals("цикл for")
                    || childs.get(i).getParent().getVal().getName().equals("ветвление case"))) {
                Pair var = this.findByName(current.getName());
                var.setCountOfLink(var.getCountOfLink() + 1);
            }
            if (current.getType().equals("nterm")) {
                if (current.getName().equals("ввод/вывод")
                        && (childs.get(i).getChilds().get(0).getVal().getName().equals("write")
                        || childs.get(i).getChilds().get(0).getVal().getName().equals("writeln"))) {
                    setCountOfLinkReс(childs.get(i).getChilds().get(2).getChilds());
                } else {
                    setCountOfLink(childs.get(i).getChilds());
                }
            }
        }
    }

    private void setCountOfLinkReс(ArrayList<TreeItem> childs) {
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).getVal().getType().equals("id")) {
                Pair var = this.findByName(childs.get(i).getVal().getName());
                var.setCountOfLink(var.getCountOfLink() + 1);
            } else {
                setCountOfLinkReс(childs.get(i).getChilds());
            }
        }
    }

    private void deleteDeadCode() throws Exception {
        for (int i = 0; i < this.tableOfName.size(); i++) {
            Pair current = this.tableOfName.get(i);
            if (current.getCountOfLink() == 0 && current.getInUse() == true) {
                if (this.deleteVar(
                        current,
                        this.tree.getRoot().getChilds().get(0).getChilds()
                )) {
                    this.removeLexems(current);
                }
                this.deleteVarFromBody(
                        current,
                        this.tree.getRoot().getChilds().get(1).getChilds()
                );
                this.removeLexems(current);
                String warning = "[Warning] Переменной \'"
                        + current.getName()
                        + "\' было присвоено значение, но оно нигде не использовалось. Она была удалена.";
                if (!this.warnings.contains(warning)) {
                    this.warnings.add(warning);
                }
            }
        }
    }

    private void deleteVarFromBody(Pair var, ArrayList<TreeItem> childs) throws Exception {
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).getVal().getName().equals("присваивание")
                    && childs.get(i).getChilds().get(0).getVal().equals(var)) {
                TreeItem grandparent = childs.get(i).getParent().getParent();
                if (grandparent.getVal().getName().equals("составной оператор")) {
                    if (childs.size() == 1) {
                        throw new Exception("Пустое тело!");
                    }
                    grandparent.getChilds().get(1).setChilds(childs.get(i + 1).getChilds());
                    childs.get(i + 1).setParent(grandparent);
                } else {
                    if (childs.size() == 1) {
                        grandparent.getChilds().remove(childs.get(i));
                    } else {
                        grandparent.getChilds().get(1).setChilds(childs.get(i + 1).getChilds());
                        childs.get(i + 1).setParent(grandparent);
                    }
                }
                this.resetLink();
                setCountOfLink(this.tree.getRoot().getChilds());
                deleteDeadCode();
                return;
            }
            if (childs.get(i).getVal().getType().equals("nterm")) {
                deleteVarFromBody(var, childs.get(i).getChilds());
            }
        }
    }

    private void resetLink() {
        for (int i = 0; i < this.tableOfName.size(); i++) {
            this.tableOfName.get(i).setCountOfLink(0);
        }
    }

    private void removeLexems(Pair lexem) {
        int i = 0;
        boolean success = false;
        while (!success && i < this.lexems.size()) {
            if (this.lexems.get(i).equals(lexem)) {
                success = true;
                if (this.lexems.get(i + 1).getName().equals(",")) {
                    this.lexems.remove(i + 1);
                    this.lexems.remove(i);
                } else {
                    if (this.lexems.get(i - 1).getName().equals("var")) {
                        i--;
                    }
                    while (!this.lexems.get(i).getName().equals(";")) {
                        this.lexems.remove(i);
                    }
                    this.lexems.remove(i);
                }
            }
            i++;
        }
    }

    private void collectWarning() throws Exception {
        String out = "";
        for (int i = 0; i < this.tableOfName.size(); i++) {
            if (!this.tableOfName.get(i).getInUse()) {
                out += "\'" + this.tableOfName.get(i).getName() + "\' ";
                deleteVar(
                        this.tableOfName.get(i),
                        this.tree.getRoot().getChilds().get(0).getChilds()
                );
                this.removeLexems(this.tableOfName.get(i));
            }
        }
        if (!out.isEmpty()) {
            String warning = "[Warning] Объявлены неиспользуемые переменные "
                    + out
                    + ". Они были удалены.";
            if (!this.warnings.contains(warning)) {
                this.warnings.add(warning);
            }
        }
    }

    private Pair findByName(String name) {
        boolean isFind = false;
        int i = 0;
        Pair result = null;
        while (i < this.tableOfName.size() && !isFind) {
            if (this.tableOfName.get(i).getName().equals(name)) {
                result = this.tableOfName.get(i);
                isFind = true;
            } else {
                i++;
            }
        }
        return result;
    }

    private void sendWarning() throws UnsupportedEncodingException {
        PrintStream ps = new PrintStream(System.out, false, "utf-8");
        for (int i = 0; i < this.warnings.size(); i++) {
            ps.println(this.warnings.get(i));
        }
    }

    private void optimizeExpression(ArrayList<TreeItem> childs) {
        for (int i = 0; i < childs.size(); i++) {
            TreeItem current = childs.get(i);
            ArrayList<TreeItem> optimize = new ArrayList();
            if (current.getVal().getName().equals("цикл while")) {
                optimize = getOptimize(current, "while");
                if (optimize.size() > 0) {
                    for (int j = 0; j < optimize.size(); j++) {
                        String systemVar = this.getSystemName();
                        TreeItem grand = current.getParent().getParent();
                        TreeItem assigment = createAssigment(optimize.get(j), systemVar);
                        TreeItem expression = createExpression(systemVar);
                        if (current.getChilds().get(2).getChilds().get(0).equals(optimize.get(j))) {
                            current.getChilds().get(2).getChilds().remove(0);
                            current.getChilds().get(2).getChilds().add(0, expression);
                        } else {
                            current.getChilds().get(2).getChilds().remove(2);
                            current.getChilds().get(2).getChilds().add(2, expression);
                        }
                        expression.setParent(current.getChilds().get(2));
                        assigment.setParent(grand);
                        grand.getChilds().remove(0);
                        grand.getChilds().add(0, assigment);
                        TreeItem operator = new TreeItem(new Pair("nterm", "операторы"));
                        grand.getChilds().add(operator);
                        operator.setParent(grand);
                        ArrayList<TreeItem> circle = new ArrayList();
                        circle.add(current.getParent());
                        current.getParent().setParent(operator);
                        if (grand.getChilds().size() != 1) {
                            circle.add(grand.getChilds().get(1));
                            grand.getChilds().get(1).setParent(operator);
                            grand.getChilds().remove(1);
                        }
                        operator.setChilds(circle);
                        expression.getClass();
                    }
                }
            }
            if (current.getVal().getName().equals("цикл repeat")) {
                optimize = getOptimize(current, "repeat");
                if (optimize.size() > 0) {
                    for (int j = 0; j < optimize.size(); j++) {
                        String systemVar = this.getSystemName();
                        TreeItem grand = current.getParent().getParent();
                        TreeItem assigment = createAssigment(optimize.get(j), systemVar);
                        TreeItem expression = createExpression(systemVar);
                        if (current.getChilds().get(5).getChilds().get(0).equals(optimize.get(j))) {
                            current.getChilds().get(5).getChilds().remove(0);
                            current.getChilds().get(5).getChilds().add(0, expression);
                        } else {
                            current.getChilds().get(5).getChilds().remove(2);
                            current.getChilds().get(5).getChilds().add(2, expression);
                        }
                        expression.setParent(current.getChilds().get(5));
                        assigment.setParent(grand);
                        grand.getChilds().remove(0);
                        grand.getChilds().add(0, assigment);
                        TreeItem operator = new TreeItem(new Pair("nterm", "операторы"));
                        grand.getChilds().add(operator);
                        operator.setParent(grand);
                        ArrayList<TreeItem> circle = new ArrayList();
                        circle.add(current.getParent());
                        current.getParent().setParent(operator);
                        if (grand.getChilds().size() != 1) {
                            circle.add(grand.getChilds().get(1));
                            grand.getChilds().get(1).setParent(operator);
                            grand.getChilds().remove(1);
                        }
                        operator.setChilds(circle);
                        expression.getClass();
                    }
                }
            }
            if (current.getVal().getType().equals("nterm")) {
                optimizeExpression(current.getChilds());
            }
        }
    }

    private ArrayList<TreeItem> getOptimize(TreeItem circle, String type) {
        ArrayList<TreeItem> optimize = new ArrayList();
        switch (type) {
            case "while": {
                ArrayList<Pair> varOfExpression1 = collectVar(circle.getChilds().get(2).getChilds().get(0), new ArrayList());
                ArrayList<Pair> varOfExpression2 = collectVar(circle.getChilds().get(2).getChilds().get(2), new ArrayList());
                if (varOfExpression1.size() > 0) {
                    int i = 0;
                    boolean result = true;
                    while (result && i < varOfExpression1.size()) {
                        result = !findVarAtBody(varOfExpression1.get(i), circle.getChilds().get(4).getChilds().get(1));
                        i++;
                    }
                    if (result && !isConst(circle.getChilds().get(2).getChilds().get(0))) {
                        optimize.add(circle.getChilds().get(2).getChilds().get(0));
                    }
                }
                if (varOfExpression2.size() > 0) {
                    int i = 0;
                    boolean result = true;
                    while (result && i < varOfExpression2.size()) {
                        result = !findVarAtBody(varOfExpression2.get(i), circle.getChilds().get(4).getChilds().get(1));
                        i++;
                    }
                    if (result && !isConst(circle.getChilds().get(2).getChilds().get(2))) {
                        optimize.add(circle.getChilds().get(2).getChilds().get(2));
                    }
                }
            }
            case "repeat": {
                ArrayList<Pair> varOfExpression1 = collectVar(circle.getChilds().get(5).getChilds().get(0), new ArrayList());
                ArrayList<Pair> varOfExpression2 = collectVar(circle.getChilds().get(5).getChilds().get(2), new ArrayList());
                if (varOfExpression1.size() > 0) {
                    int i = 0;
                    boolean result = true;
                    while (result && i < varOfExpression1.size()) {
                        result = !findVarAtBody(varOfExpression1.get(i), circle.getChilds().get(1).getChilds().get(1));
                        i++;
                    }
                    if (result && !isConst(circle.getChilds().get(5).getChilds().get(0))) {
                        optimize.add(circle.getChilds().get(5).getChilds().get(0));
                    }
                }
                if (varOfExpression2.size() > 0) {
                    int i = 0;
                    boolean result = true;
                    while (result && i < varOfExpression2.size()) {
                        result = !findVarAtBody(varOfExpression2.get(i), circle.getChilds().get(1).getChilds().get(1));
                        i++;
                    }
                    if (result && !isConst(circle.getChilds().get(5).getChilds().get(2))) {
                        optimize.add(circle.getChilds().get(5).getChilds().get(2));
                    }
                }
            }
        }
        return optimize;
    }

    private ArrayList<Pair> collectVar(TreeItem expression, ArrayList<Pair> var) {
        for (int i = 0; i < expression.getChilds().size(); i++) {
            if (expression.getChilds().get(i).getVal().getType().equals("nterm")) {
                var.addAll(collectVar(expression.getChilds().get(i), new ArrayList()));
                continue;
            }
            if (expression.getChilds().get(i).getVal().getType().equals("id")) {
                var.add(expression.getChilds().get(i).getVal());
            }
        }
        return var;
    }

    private boolean findVarAtBody(Pair finding, TreeItem container) {
        boolean result = false;
        int i = 0;
        while (!result && i < container.getChilds().size()) {
            if (container.getChilds().get(i).getVal().getType().equals("nterm")) {
                result = findVarAtBody(finding, container.getChilds().get(i));
                i++;
                continue;
            }
            if (container.getChilds().get(i).getVal().getType().equals("id")
                    && container.getChilds().get(i).getVal().getName().equals(finding.getName())) {
                result = true;
            }
            i++;
        }
        return result;
    }

    private TreeItem createAssigment(TreeItem expression, String varName) {
        ArrayList<Pair> childs = new ArrayList();
        childs.add(new Pair("id", varName));
        childs.add(new Pair("nterm", "знак присваивания"));
        childs.add(new Pair("nterm", "выражение"));
        childs.add(new Pair("separator", ";"));
        TreeItem assigment = new TreeItem(new Pair("nterm", "присваивание"));
        ArrayList<Pair> assigmentMark = new ArrayList();
        assigmentMark.add(new Pair("assignment", ":="));
        assigment.addChilds(childs);
        assigment.getChilds().get(1).addChilds(assigmentMark);
        TreeItem newExpression = assigment.getChilds().get(2);
        newExpression.setChilds(expression.getChilds());
        for (int i = 0; i < expression.getChilds().size(); i++) {
            expression.getChilds().get(i).setParent(newExpression);
        }
        this.tableOfName.add(assigment.getChilds().get(0).getVal());
        return assigment;
    }

    private TreeItem createExpression(String varName) {
        TreeItem expression = new TreeItem(new Pair("nterm", "выражение"));
        ArrayList<Pair> T = new ArrayList();
        T.add(new Pair("nterm", "операнд T"));
        expression.addChilds(T);
        TreeItem treeT = expression.getChilds().get(0);
        ArrayList<Pair> F = new ArrayList();
        F.add(new Pair("nterm", "операнд F"));
        treeT.addChilds(F);
        TreeItem treeF = treeT.getChilds().get(0);
        ArrayList<Pair> id = new ArrayList();
        id.add(new Pair("id", varName));
        treeF.addChilds(id);
        return expression;
    }

    private String getSystemName() {
        String prefix = "system__";
        int counter = 0;
        while (findByName(prefix + counter) != null) {
            counter++;
        }
        return prefix + counter;
    }

    private boolean isConst(TreeItem expression) {
        boolean result = true;
        if (expression.getChilds().size() > 1) {
            return false;
        }
        if (expression.getChilds().get(0).getVal().getType().equals("nterm")) {
            result = isConst(expression.getChilds().get(0));
        } else if (!expression.getChilds().get(0).getVal().getType().equals("number")
                && !expression.getChilds().get(0).getVal().getType().equals("string")
                && !expression.getChilds().get(0).getVal().getType().equals("id")) {
            return false;
        }
        return result;
    }
   
    public ArrayList<Pair> getListLexem() {
        return this.lexems;
    }
}
