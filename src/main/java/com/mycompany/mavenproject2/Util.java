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
        String pascalFile = "deadcode.pas";
        PrintStream ps = new PrintStream(System.out, false, "utf-8");
        LexAnalyzer pascalLexAnal = new LexAnalyzer(pascalFile);
        try {
            pascalLexAnal.makeAnalysis();
            //pascalLexAnal.print();
            PascalGrammar pascalGrammar = new PascalGrammar(new Pair("nterm", "программа"));
            //pascalGrammar.print();
            SynAnalyzer pascalSynAnal = new SynAnalyzer(pascalLexAnal.getListLexem(),
                    pascalGrammar);
            pascalSynAnal.makeTable();
            //pascalSynAnal.printTable();
            pascalSynAnal.parse();
            pascalSynAnal.printParse();
            pascalSynAnal.buildTree();
            SemAnalyzer pascalSemAnal = new SemAnalyzer(
                    pascalSynAnal.getTree(),
                    pascalLexAnal.getListLexem()
            );
            pascalSemAnal.makeAnalysis();
            if (!pascalSemAnal.hasError()) {
                CGrammar cGrammar = new CGrammar(new Pair("nterm", "программа"));
                //cGrammar.print();
                Translator pascalToC = new Translator(
                        cGrammar, 
                        pascalGrammar,
                        pascalSemAnal.getTree(),
                        pascalLexAnal.getListLexem()
                );
                pascalToC.translate("program.cpp");
            }
        } catch (Exception e) {
            ps.print(e.getMessage());
        }
    }
}
