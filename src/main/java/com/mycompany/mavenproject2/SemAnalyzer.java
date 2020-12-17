/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.util.ArrayList;

/**
 *
 * @author refresh.jss
 */
public class SemAnalyzer {

    private ArrayList<Pair> tableOfName;
    private ParseTree tree;
    private boolean hasError = false;

    SemAnalyzer(ParseTree tree) {
        this.tree = tree;
        this.tableOfName = new ArrayList();
    }

    public void makeAnalysis() throws Exception {
        try {
            setTableOfName();
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

    private void walkBlockVar(ArrayList<TreeItem> blockOfVar) {
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

            }
        }
    }

    private void setListVar(ArrayList<TreeItem> childs) {
        for (int i = 0; i < childs.size(); i++) {
            Pair item = childs.get(i).getVal();
            if (item.getType().equals("id")) {
                tableOfName.add(item.copy());
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
}
