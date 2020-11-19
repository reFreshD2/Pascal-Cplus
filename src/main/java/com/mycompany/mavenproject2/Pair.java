/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author refresh.jss
 */
public class Pair {
    private String type;
    private String name;
    
    Pair(String t, String n) {
        type = t;
        name = n;
    }
    
    void print() throws UnsupportedEncodingException {
        PrintStream ps = new PrintStream(System.out,false,"utf-8");
        ps.print("( <"+type+"> "+ name + " )");
    }
    
    @Override
    public boolean equals(Object o) { 
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair c = (Pair) o;
        boolean eq = false;
        if (this.type.equals(c.type)) {
            if (this.name.equals("") || c.name.equals("")) {
                eq = true;
            } else if (this.name.equals(c.name)) {
                eq = true;
            }
        }
        return eq;
    } 
}
