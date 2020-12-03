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

    SemAnalyzer(ParseTree tree) throws Exception {
        this.tree = tree;
        this.tableOfName = new ArrayList();
        try {
            setTableOfName();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    public void makeAnalysis() {
        setTypes();
    }
    
    private void setTableOfName() throws Exception {
        TreeItem blockOfVar = this.tree.getRoot().getChilds().get(0);
        if (!blockOfVar.getVal().getName().equals("раздел описания")) {
            throw new Exception("Некорректное дерево");
        }
    }
    
    private void setTypes() {
        
    }
    
    public boolean hasError() {
        return this.hasError;
    }
}
