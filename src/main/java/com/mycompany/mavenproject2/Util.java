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
public class Util {

    public static void main(String[] args) {
        String pascalFile = "program.txt";
        LexAnalyzer pascal = new LexAnalyzer(pascalFile);
        pascal.makeAnalysis();
        pascal.print();

    }
}
