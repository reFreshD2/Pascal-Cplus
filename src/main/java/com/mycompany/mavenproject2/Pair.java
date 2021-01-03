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
    private int numString;
    private String contextType = "";
    private String contextValue = "";
   
    Pair(String type, String name) {
        this.type = type;
        this.name = name;
        this.numString = 0;
    }
    
    Pair(String type, String name, int numString) {
        this.type = type;
        this.name = name;
        this.numString = numString;
    }
    
    public void setContextType(String type) {
        this.contextType = type;
    }
    
    public String getContextType() {
        return this.contextType;
    }
    
    public Pair copy() {
        return new Pair(type,name, numString);
    }
    
    public int getNumString() {
        return this.numString;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getName() {
        return this.name;
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
    
    public void setAllFields(Pair other) {
        this.name = other.getName();
        this.numString = other.getNumString();
        this.type = other.getType();
        this.contextType = other.getContextType();
    }
    
    public void setContextValue(String value) {
        this.contextValue = value;
    }
    
    public String getContextValue() {
        return this.contextValue;
    }
}
