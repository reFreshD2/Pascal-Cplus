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
        sendWarning();
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
                    childs.get(i+1).setParent(grandparent);
                } else {
                    if (childs.size() == 1) {
                        grandparent.getChilds().remove(childs.get(i));
                    } else {
                        grandparent.getChilds().get(1).setChilds(childs.get(i + 1).getChilds());
                        childs.get(i+1).setParent(grandparent);
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
}
