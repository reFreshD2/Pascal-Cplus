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
public class Situation {
    private Rule rule;
    private int pos;
    
    Situation(Rule rule, int pos) {
        this.rule = rule;
        this.pos = pos;
    }
    
    public Rule getRule() {
        return this.rule;
    }
    
    public void print() throws UnsupportedEncodingException {
        PrintStream ps = new PrintStream(System.out,false,"utf-8");
        ps.print("[");
        rule.print();
        ps.print(", "+ pos +"]");
    }
    
    @Override
    public boolean equals(Object o) { 
        if (o == this) {
            return true;
        }
        if (!(o instanceof Situation)) {
            return false;
        }
        Situation c = (Situation) o;
        boolean eq = true;
        if (this.rule.getLeft().equals(c.getRule().getLeft())) {
            eq = false;
        }
        if (this.rule.getRight().size() != c.getRule().getRight().size()) {
            eq = false;
        }
        int i = 0;
        while (eq && (i < this.rule.getRight().size())) {
            if (!this.rule.getPair(i).equals(c.getRule().getPair(i))) {
                eq = false;
            }
        }
        return eq;
    } 
}
