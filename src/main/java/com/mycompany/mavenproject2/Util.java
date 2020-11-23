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
        String pascalFile = "program2.txt";
        LexAnalyzer pascalLexAnal = new LexAnalyzer(pascalFile);
        pascalLexAnal.makeAnalysis();
        pascalLexAnal.print();
        PascalGrammar pascalGrammar = new PascalGrammar(new Pair("nterm", "программа"));
        pascalGrammar.print();
<<<<<<< HEAD
        SynAnalyzer pascalSynAnal = new SynAnalyzer(pascalLexAnal.getListLexem(), pascalGrammar);
        try {
            pascalSynAnal.makeTable();
            pascalSynAnal.printTable();
        } catch (Exception e) {
            PrintStream ps = new PrintStream(System.out, false, "utf-8");
            ps.print(e.getMessage());
        }

=======
        SynAnalyzer pascalSynAnal = new SynAnalyzer(pascalLexAnal.getListLexem(),pascalGrammar);
        pascalSynAnal.makeTable();
        pascalSynAnal.printTable();
        
        pascalSynAnal.parse();
        
>>>>>>> katebekk
        //30,31,6,4,39,16,14,12,38,35,1,0
        ArrayList<Integer> arrInt = new ArrayList<>();
        arrInt.add(30);
        arrInt.add(31);
        arrInt.add(6);
        arrInt.add(4);
        arrInt.add(39);
        arrInt.add(16);
        arrInt.add(14);
        arrInt.add(12);
        arrInt.add(38);
        arrInt.add(35);
        arrInt.add(1);
        arrInt.add(0);
        ParseTree tree = pascalSynAnal.buildTree(arrInt);

        arrInt.add(0);
    }
}
