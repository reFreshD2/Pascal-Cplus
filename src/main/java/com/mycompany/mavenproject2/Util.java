/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author refresh.jss
 */
public class Util {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String pascalFile = "program1.txt";
        LexAnalyzer pascalLexAnal = new LexAnalyzer(pascalFile);
        pascalLexAnal.makeAnalysis();
        pascalLexAnal.print();
        PascalGrammar pascalGrammar = new PascalGrammar(new Pair("nterm","программа"));
        pascalGrammar.print();
        SynAnalyzer pascalSynAnal = new SynAnalyzer(pascalLexAnal.getListLexem(),pascalGrammar);
        pascalSynAnal.makeTable();
        pascalSynAnal.printTable();
    }
}
