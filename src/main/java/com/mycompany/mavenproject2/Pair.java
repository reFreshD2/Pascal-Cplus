/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

/**
 *
 * @author refresh.jss
 */
public class Pair {
    String type;
    String name;
    
    Pair(String t, String n) {
        type = t;
        name = n;
    }
    
    void print() {
        System.out.println("<"+type+"> "+ name);
    }
}