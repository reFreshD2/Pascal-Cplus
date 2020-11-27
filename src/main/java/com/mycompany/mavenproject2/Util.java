/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.UnsupportedEncodingException;
import java.io.PrintStream;

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
            pascalSynAnal.printParse();
            pascalSynAnal.buildTree();
        } catch (Exception e) {
            PrintStream ps = new PrintStream(System.out, false, "utf-8");
            ps.print(e.getMessage());
        }
    }
}
