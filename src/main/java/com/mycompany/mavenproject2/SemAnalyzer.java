/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author refresh.jss
 */
public class SemAnalyzer {

    private ArrayList<Pair> tableOfName;
    private ParseTree tree;
    private boolean hasError = false;
    private PossibleOperationRepository repository = new PossibleOperationRepository();
    private final int COUNT_OF_VARIABLE = 255;
    private final int MAX_LENGTH_STRING = 255;
    private ArrayList<Pair> lexems;

    SemAnalyzer(ParseTree tree, ArrayList<Pair> lexems) {
        this.tree = tree;
        this.lexems = lexems;
        this.tableOfName = new ArrayList();
    }

    public void makeAnalysis() throws Exception {
        try {
            setTableOfName();
            if (this.tableOfName.size() > this.COUNT_OF_VARIABLE) {
                throw new Exception("Недопустимое количество переменных. Максимальное возможное число - 255");
            }
            validationString();
            ArrayList<Pair> scope = new ArrayList();
            validationBody(this.tree.getRoot().getChilds().get(1).getChilds().get(1).getChilds(), scope);
            sendWarning();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void setTableOfName() throws Exception {
        TreeItem blockOfVar = this.tree.getRoot().getChilds().get(0);
        if (!blockOfVar.getVal().getName().equals("раздел описания")) {
            throw new Exception("Некорректное дерево");
        } else {
            walkBlockVar(blockOfVar.getChilds());
        }
    }

    private void walkBlockVar(ArrayList<TreeItem> blockOfVar) throws Exception {
        int start = 0;
        int end = 0;
        for (int i = 0; i < blockOfVar.size(); i++) {
            TreeItem item = blockOfVar.get(i);
            if (item.getVal().getName().equals("список имен")) {
                start = tableOfName.size();
                setListVar(item.getChilds());
                end = tableOfName.size();
            }
            if (item.getVal().getType().equals("id")) {
                start = tableOfName.size();
                tableOfName.add(item.getVal().copy());
                end = tableOfName.size();
            }
            if (item.getVal().getName().equals("тип")) {
                String type = item.getChilds().get(0).getVal().getName();
                setTypes(start, end, type);
            }
            if (item.getVal().getName().equals("раздел описания")) {
                walkBlockVar(item.getChilds());
            }
            if (item.getVal().getName().equals("выражение")) {
                String typeVar = tableOfName.get(tableOfName.size() - 1).getContextType();
                String typeExpression = execExpression(item.getChilds());
                if (!((typeVar.equals("real") && typeExpression.equals("integer"))
                        || (typeVar.equals("string") && typeExpression.equals("char"))
                        || typeVar.equals(typeExpression))) {
                    throw new Exception("Недопустимое преобразование типов переменной \'"
                            + tableOfName.get(tableOfName.size() - 1).getName()
                            + "\' в строке "
                            + tableOfName.get(tableOfName.size() - 1).getNumString()
                            + " - "
                            + typeVar
                            + " к "
                            + typeExpression);
                } else {
                    tableOfName.get(tableOfName.size() - 1).setContextValue(collectExpression(item.getChilds()));
                }
            }
        }
    }

    private String execExpression(ArrayList<TreeItem> childs) throws Exception {
        String parent = childs.get(0).getParent().getVal().getName();
        if ((parent.equals("выражение") || parent.equals("операнд T")) && childs.size() > 1) {
            String operation = childs.get(1).getVal().getName();
            String operand1 = execExpression(childs.get(0).getChilds());
            String operand2 = execExpression(childs.get(2).getChilds());
            try {
                return this.repository.getReturnType(operation, operand1, operand2);
            } catch (Exception e) {
                throw new Exception(e.getMessage()
                        + " в строке "
                        + childs.get(1).getVal().getNumString());
            }
        }
        if ((parent.equals("выражение") || parent.equals("операнд T")) && childs.size() == 1) {
            return execExpression(childs.get(0).getChilds());
        }
        if (parent.equals("операнд F") && childs.size() == 1) {
            Pair atom = childs.get(0).getVal();
            try {
                return switch (atom.getType()) {
                    case "nterm" ->
                        "boolean";
                    case "id" ->
                        getTypeOfVariable(atom.getName());
                    default ->
                        atom.getType();
                };
            } catch (Exception e) {
                throw new Exception(e.getMessage()
                        + " в строке "
                        + atom.getNumString());
            }
        }
        if (parent.equals("операнд F") && childs.size() > 1) {
            if (childs.get(0).getVal().getType().equals("bracket")) {
                return execExpression(childs.get(1).getChilds());
            } else {
                String operation = childs.get(0).getChilds().get(0).getVal().getName();
                String opearand = execExpression(childs.get(2).getChilds());
                try {
                    return this.repository.getReturnType(operation, opearand);
                } catch (Exception e) {
                    throw new Exception(e.getMessage()
                            + " в строке "
                            + childs.get(0).getChilds().get(0).getVal().getNumString());
                }
            }
        }
        return "true";
    }

    private void setListVar(ArrayList<TreeItem> childs) throws Exception {
        for (int i = 0; i < childs.size(); i++) {
            Pair item = childs.get(i).getVal();
            if (item.getType().equals("id")) {
                if (!isDuplicate(item.getName())) {
                    tableOfName.add(item.copy());
                } else {
                    throw new Exception("Использовано не уникальное имя переменной \'"
                            + item.getName()
                            + "\' в строке "
                            + item.getNumString());
                }
            }
            if (item.getName().equals("список имен")) {
                setListVar(childs.get(i).getChilds());
            }
        }
    }

    private void setTypes(int start, int end, String type) {
        for (int i = start; i < end; i++) {
            tableOfName.get(i).setContextType(type);
        }
    }

    public boolean hasError() {
        return this.hasError;
    }

    private String getTypeOfVariable(String name) throws Exception {
        String type = "";
        int i = 0;
        while (i < this.tableOfName.size() && type.isEmpty()) {
            if (this.tableOfName.get(i).getName().equals(name)) {
                type = this.tableOfName.get(i).getContextType();
            } else {
                i++;
            }
        }
        if (type.isEmpty()) {
            throw new Exception("Использована необъявленная переменная \'"
                    + name
                    + "\'"
            );
        }
        if (this.tableOfName.get(i).getContextValue().isEmpty()) {
            throw new Exception("Использована непроинициализированная переменная \'"
                    + name
                    + "\'");
        }
        return type;
    }

    private boolean isDuplicate(String name) {
        boolean isFind = false;
        int i = 0;
        while (i < this.tableOfName.size() && !isFind) {
            if (this.tableOfName.get(i).getName().equals(name)) {
                isFind = true;
            } else {
                i++;
            }
        }
        return isFind;
    }

    private void validationString() throws Exception {
        for (Pair elem : this.tableOfName) {
            if (elem.getContextType().equals("string") && elem.getContextValue().length() > this.MAX_LENGTH_STRING) {
                throw new Exception("Недопустимая длина строки в переменной \'"
                        + elem.getName()
                        + "\' в строке "
                        + elem.getNumString()
                        + ". Получена строка длинной "
                        + elem.getContextValue().length()
                        + ", а ожидалась < "
                        + this.MAX_LENGTH_STRING);
            }
        }
    }

    private String collectExpression(ArrayList<TreeItem> childs) {
        String result = "";
        int i = 0;
        while (i < childs.size()) {
            TreeItem current = childs.get(i);
            if (current.getChilds().size() > 0) {
                result += collectExpression(current.getChilds());
            } else {
                result += current.getVal().getName();
            }
            i++;
        }
        return result;
    }

    private void validationBody(ArrayList<TreeItem> childs, ArrayList<Pair> scope) throws Exception {
        int i = 0;
        while (i < childs.size()) {
            TreeItem current = childs.get(i);
            if (current.getChilds().isEmpty()) {
                if (current.getVal().getName().equals("readln")) {
                    setUserValue(childs.get(i + 2).getChilds());
                    i += 2;
                }
                switch (current.getVal().getType()) {
                    case "id" -> {
                        if (isInit(current.getVal())) {
                            scope.add(findByName(current.getVal().getName()));
                            scope.get(scope.size() - 1).setNumString(current.getVal().getNumString());
                        } else {
                            throw new Exception("Использована необъявленная переменная \'"
                                    + current.getVal().getName()
                                    + "\' в строке "
                                    + current.getVal().getNumString());
                        }
                    }
                    case "assignment" -> {
                        String typeVar = scope.get(scope.size() - 1).getContextType();
                        i++;
                        String typeExpression = "";
                        if (current.getParent().getVal().getName().equals("знак присваивания")) {
                            typeExpression = execExpression(current.getParent().getParent().getChilds().get(2).getChilds());
                        } else {
                            typeExpression = current.getParent().getChilds().get(3).getVal().getType();
                        }
                        if (current.getVal().getName().equals(":=")) {
                            if (!((typeVar.equals("real") && typeExpression.equals("integer"))
                                    || (typeVar.equals("string") && typeExpression.equals("char"))
                                    || typeVar.equals(typeExpression))) {
                                throw new Exception("Недопустимое преобразование типов переменной \'"
                                        + scope.get(scope.size() - 1).getName()
                                        + "\' в строке "
                                        + scope.get(scope.size() - 1).getNumString()
                                        + " - "
                                        + typeVar
                                        + " к "
                                        + typeExpression);
                            } else {
                                String contextValue = "";
                                if (current.getParent().getVal().getName().equals("знак присваивания")) {
                                    contextValue = collectExpression(current.getParent().getParent().getChilds().get(2).getChilds());
                                } else {
                                    contextValue = current.getParent().getChilds().get(3).getVal().getName();
                                }
                                scope.get(scope.size() - 1).setContextValue(contextValue);
                                findByName(scope.get(scope.size() - 1).getName()).setContextValue(contextValue);
                            }
                        } else {
                            String operation = current.getVal().getName();
                            try {
                                typeExpression = this.repository.getReturnType(operation, typeVar, typeExpression);
                            } catch (Exception e) {
                                throw new Exception(e.getMessage()
                                        + " в строке "
                                        + current.getVal().getNumString());
                            }
                            if (!((typeVar.equals("real") && typeExpression.equals("integer"))
                                    || (typeVar.equals("string") && typeExpression.equals("char"))
                                    || typeVar.equals(typeExpression))) {
                                throw new Exception("Недопустимое преобразование типов переменной \'"
                                        + scope.get(scope.size() - 1).getName()
                                        + "\' в строке "
                                        + scope.get(scope.size() - 1).getNumString()
                                        + " - "
                                        + typeVar
                                        + " к "
                                        + typeExpression);
                            } else {
                                String contextValue = scope.get(scope.size() - 1).getContextValue()
                                        + current.getVal().getName().substring(0, 1)
                                        + collectExpression(current.getParent().getParent().getChilds().get(2).getChilds());
                                scope.get(scope.size() - 1).setContextValue(contextValue);
                                findByName(scope.get(scope.size() - 1).getName()).setContextValue(contextValue);
                            }
                        }
                    }
                    case "compare" -> {
                        String operation = current.getVal().getName();
                        String operandType1 = execExpression(childs.get(i - 1).getChilds());
                        String operandType2 = execExpression(childs.get(i + 1).getChilds());
                        try {
                            this.repository.getReturnType(operation, operandType1, operandType2);
                        } catch (Exception e) {
                            throw new Exception(e.getMessage()
                                    + " в строке "
                                    + current.getVal().getNumString());
                        }
                        i++;
                    }
                }
            } else {
                validationBody(current.getChilds(), scope);
            }
            i++;
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

    private boolean isInit(Pair variable) {
        boolean isFind = false;
        int i = 0;
        while (i < this.tableOfName.size() && !isFind) {
            if (this.tableOfName.get(i).getName().equals(variable.getName())) {
                isFind = true;
                this.tableOfName.get(i).setInUse(true);
            } else {
                i++;
            }
        }
        return isFind;
    }

    private void sendWarning() throws UnsupportedEncodingException, Exception {
        PrintStream ps = new PrintStream(System.out, false, "utf-8");
        String out = "";
        for (int i = 0; i < this.tableOfName.size(); i++) {
            if (!this.tableOfName.get(i).getInUse()) {
                out += "\'" + this.tableOfName.get(i).getName() + "\' ";
                deleteVar(
                        this.tableOfName.get(i),
                        this.tree.getRoot().getChilds().get(0).getChilds()
                );
                this.lexems.remove(this.tableOfName.get(i));
            }
        }
        if (!out.isEmpty()) {
            ps.println("[Warning] Объявлены неиспользуемые переменные " + out);
        }
    }

    private void setUserValue(ArrayList<TreeItem> childs) {
        int i = 0;
        while (i < childs.size()) {
            TreeItem current = childs.get(i);
            switch (current.getVal().getType()) {
                case "id" ->
                    findByName(current.getVal().getName()).setContextValue("user_value");
                default ->
                    setUserValue(current.getChilds());
            }
            i++;
        }
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
                        TreeItem lastItem = grandparent.getChilds().get(grandparent.getChilds().size()-1);
                        if (lastItem.getVal().getName().equals("раздел описания")) {
                            grandparent.setChilds(lastItem.getChilds());
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
}
