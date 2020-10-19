/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author refresh.jss
 */
public class LexAnalyzer {

    private final String[] keyWord = {
        "begin", "end", "for", "to", "var", "downto", "do",
        "while", "repeat", "until", "if", "then", "else",
        "case", "break", "real", "char", "string",
        "boolean", "abs", "sqr", "sqrt", "exp", "write",
        "writeln", "readln", "true", "false"
    };

    private final String[] bracket = {
        "(", ")"
    };

    private final String[] num = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };

    private final String[] compare = {
        ">", "<", "<=", ">=", "<>", "="
    };

    private final String[] charPascal = {
        "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m",
        "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M",
        "й", "ц", "у", "к", "е", "н", "г", "ш", "щ", "з", "х", "ъ", "ф", "ы", "в", "а", "п", "р", "о", "л", "д", "ж", "э", "я", "ч", "с", "м", "и", "т", "ь", "б", "ю",
        "Й", "Ц", "У", "К", "Е", "Н", "Г", "Ш", "Щ", "З", "Х", "Ъ", "Ф", "Ы", "В", "А", "П", "Р", "О", "Л", "Д", "Ж", "Э", "Я", "Ч", "С", "М", "И", "Т", "Ь", "Б", "Ю"
    };

    private final String[] separator = {
        ":", ".", ";", ","
    };

    private final String[] operator = {
        "+", "-", "*", "/"
    };

    String input;
    ArrayList<Pair> output;

    private void setInput(String fileName) {
        try ( FileReader fr = new FileReader(fileName);  Scanner scan = new Scanner(fr)) {
            while (scan.hasNextLine()) {
                input = input + scan.nextLine();
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("File err" + e);
        }
    }

    LexAnalyzer(String fileName) {
        output = new ArrayList();
        setInput(fileName);
        try {
            makeAnalysis();
        } catch (Exception ex) {
            Logger.getLogger(LexAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void makeAnalysis() throws Exception {
        String lexema = new String();
        int i = 0;
        while (i < input.length()) {
            // строка
            if (input.charAt(i) == '"') {
                lexema += input.charAt(i);
                i++;
                while (input.charAt(i) == '"') {
                    lexema += input.charAt(i);
                    i++;
                }
                lexema += input.charAt(i);
                Pair lex = new Pair("string", lexema);
                output.add(lex);
                lexema = "";
            }
            // символ
            if (input.charAt(i) == '\'') {
                int j;
                for (j = i; j < i + 3; j++) {
                    lexema += input.charAt(i);
                }
                i = j;
                if (lexema.charAt(2) == '\'') {
                    Pair lex = new Pair("char", lexema);
                    output.add(lex);
                    lexema = "";
                } else {
                    throw new Exception("Wrong lexem");
                }
            }
            while (input.charAt(i) != ' ' && i < input.length()) {
                lexema += input.charAt(i);
                // скобка
                if (find(bracket, lexema)) {
                    Pair lex = new Pair("bracket", lexema);
                    output.add(lex);
                    lexema = "";
                }
                if (find(separator, lexema)) {
                    //присваивание
                    if (lexema.charAt(0) == ':' && input.charAt(i + 1) == '=') {
                        i++;
                        lexema += input.charAt(i);
                        Pair lex = new Pair("assignment", lexema);
                        output.add(lex);
                        lexema = "";
                    } else { //разделитель
                        Pair lex = new Pair("separator", lexema);
                        output.add(lex);
                        lexema = "";
                    }
                }
                i++;
            }
            i++;
        }
    }

    private boolean find(String[] sourse, String sample) {
        boolean isFind = false;
        for (String atom : sourse) {
            if (sample.equals(atom)) {
                isFind = true;
            }
        }
        return isFind;
    }
}
