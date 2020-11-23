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
class Rule {
    private Pair left;
    private ArrayList<Pair> right;
    private final Pair dot = new Pair("$","");
    
    Rule(Pair left, ArrayList<Pair> right) {
        this.left = left;
        this.right = new ArrayList();
        this.right = right;
    }
    
    public void print() throws UnsupportedEncodingException {
        PrintStream ps = new PrintStream(System.out,false,"utf-8");
        this.left.print();
        ps.print(" -> ");
        for (int i = 0; i < this.right.size(); i++) {
            this.getPair(i).print();
            ps.print(" ");
        }
    }
   
    
    public Pair getLeft() {
        return this.left;
    }
    
    public ArrayList<Pair> getRight() {
        return this.right;
    }
    
    public Rule getRuleWithDot(int pos) {
        ArrayList<Pair> rightWithDot = new ArrayList();
        for (int i = 0; i < pos; i++) {
            rightWithDot.add(this.getPair(i));
        }
        rightWithDot.add(this.dot);
        for (int i = pos; i < this.right.size(); i++) {
            rightWithDot.add(this.getPair(i));
        }
        return new Rule(this.left, rightWithDot);
    }
    
    public int getPosSymbol(Pair symbol) {
        boolean isFound = false;
        int i = 0;
        while (!isFound && (i < this.right.size())) {
            if (this.right.get(i).equals(symbol)) {
                isFound = true;
            } else {
                i++;
            }
        }
        if (!isFound) {
            i = -1;
        }
        return i;
    }
    
    public Rule swap(int left, int right) {
        ArrayList<Pair> newRight = new ArrayList();
        for (int i = 0; i < left; i++) {
            newRight.add(this.getPair(i));
        }
        newRight.add(this.getPair(right));
        newRight.add(this.getPair(left));
        for (int i = right + 1; i < this.right.size(); i++) {
            newRight.add(this.getPair(i));
        }
        this.right.clear();
        this.right = newRight;
        return this;
    }
    
    public Pair getPair(int pos) {
        return this.right.get(pos);
    }
    
    public Rule copy() {
        Pair left = this.left.copy();
        ArrayList<Pair> right = new ArrayList();
        for (int i = 0; i < this.right.size(); i++) {
            Pair current = this.right.get(i).copy();
            right.add(current);
        }
        return new Rule(left,right);
    }
    public void setRight(ArrayList<Pair> list){
        this.right = list;
    }
}
