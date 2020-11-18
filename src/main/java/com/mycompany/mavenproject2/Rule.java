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
    Pair left;
    ArrayList<Pair> right;
    
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
            this.right.get(i).print();
            ps.print(" ");
        }
    }
}
