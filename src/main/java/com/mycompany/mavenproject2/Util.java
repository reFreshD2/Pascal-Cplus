/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 *
 * @author refresh.jss
 */
public class Util {

    public static void main(String[] args) throws UnsupportedEncodingException, Exception {
        String pascalFile = "program1.txt";
        LexAnalyzer pascalLexAnal = new LexAnalyzer(pascalFile);
        pascalLexAnal.makeAnalysis();
        pascalLexAnal.print();
        PascalGrammar pascalGrammar = new PascalGrammar(new Pair("nterm", "программа"));
        pascalGrammar.print();
        SynAnalyzer pascalSynAnal = new SynAnalyzer(pascalLexAnal.getListLexem(), pascalGrammar);
        try {
            pascalSynAnal.makeTable();
            pascalSynAnal.printTable();
	    pascalSynAnal.parse();
            pascalSynAnal.buildTree();
        } catch (Exception e) {
            PrintStream ps = new PrintStream(System.out, false, "cp1251");
            ps.print(e.getMessage());
        }
        
        
        //30,31,6,4,39,16,14,12,38,35,1,0
       
       
    }
}
