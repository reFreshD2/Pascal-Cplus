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
        String pascalFile = "program5.txt";
        PrintStream ps = new PrintStream(System.out, false, "utf-8");
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
            try {
                SemAnalyzer pascalSemAnal = new SemAnalyzer(pascalSynAnal.getTree());
                pascalSemAnal.makeAnalysis();
                if (!pascalSemAnal.hasError()) {
                    // Трансляция в С++
                }
            } catch (Exception e) {
                ps.print(e.getMessage());
            }
        } catch (Exception e) {
            ps.print(e.getMessage());
        }
    }
}
