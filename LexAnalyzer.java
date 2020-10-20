/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.*;
import java.util.*;

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
        "writeln", "readln", "true", "false", "integer"
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
            this.input = "";
            while (scan.hasNextLine()) {
                this.input += scan.nextLine();
                this.input += ' ';
            }
            fr.close();
        } catch (IOException e) {
            System.out.println("File err" + e);
        }
    }

    LexAnalyzer(String fileName) {
        this.output = new ArrayList();
        setInput(fileName);
    }

    public void makeAnalysis() {
        String lexema = new String();
        int i = 0;
        while (i < this.input.length()) {
            lexema += this.input.charAt(i);
            if (lexema.charAt(0) == '\'') {
                i++;
                while (this.input.charAt(i) != '\'') {
                    lexema += this.input.charAt(i);
                    i++;
                }
                lexema += this.input.charAt(i);
                if (lexema.length() != 3) { // строка
                    Pair lex = new Pair("string", lexema);
                    this.output.add(lex);
                } else { // символ
                    Pair lex = new Pair("char", lexema);
                    this.output.add(lex);
                }
                lexema = "";
            }
            // скобка
            if (find(this.bracket, lexema)) {
                Pair lex = new Pair("bracket", lexema);
                this.output.add(lex);
                lexema = "";
            }
            if (find(this.separator, lexema)) {
                //присваивание
                if (lexema.charAt(0) == ':' && this.input.charAt(i + 1) == '=') {
                    i++;
                    lexema += this.input.charAt(i);
                    Pair lex = new Pair("assignment", lexema);
                    this.output.add(lex);
                    lexema = "";
                } else { //разделитель
                    Pair lex = new Pair("separator", lexema);
                    this.output.add(lex);
                    lexema = "";
                }
            }
            if (find(this.operator, lexema)) {
                //присваивание
                if (this.input.charAt(i + 1) == '=') {
                    i++;
                    lexema += this.input.charAt(i);
                    Pair lex = new Pair("assignment", lexema);
                    this.output.add(lex);
                    lexema = "";
                } else {
                    // операция типа +
                    if (lexema.charAt(0) == '+' || lexema.charAt(0) == '-') {
                        Pair lex = new Pair("plus operator", lexema);
                        this.output.add(lex);
                        lexema = "";
                    } else { //операция типа *
                        Pair lex = new Pair("mult operator", lexema);
                        this.output.add(lex);
                        lexema = "";
                    }
                }
            }
            //сравнение
            if (find(this.compare, lexema)) {
                if ((this.input.charAt(i + 1) == '=' && lexema.charAt(0) != '=') || (lexema.charAt(0) == '<' && this.input.charAt(i + 1) == '>')) {
                    i++;
                    lexema += this.input.charAt(i);
                    Pair lex = new Pair("compare", lexema);
                    this.output.add(lex);
                    lexema = "";
                } else {
                    Pair lex = new Pair("compare", lexema);
                    this.output.add(lex);
                    lexema = "";
                }
            }
            //числа
            if (find(this.num, lexema)) {
                String nextChar = new String();
                nextChar += this.input.charAt(i + 1);
                while (find(this.num, nextChar)) {
                    i++;
                    lexema += this.input.charAt(i);
                    nextChar = "";
                    nextChar += this.input.charAt(i + 1);
                }
                if (nextChar.charAt(0) == '.') {
                    i++;
                    lexema += this.input.charAt(i);
                    nextChar = "";
                    nextChar += this.input.charAt(i + 1);
                    while (find(this.num, nextChar)) {
                        i++;
                        lexema += this.input.charAt(i);
                        nextChar = "";
                        nextChar += this.input.charAt(i + 1);
                    }
                    Pair lex = new Pair("real", lexema);
                    this.output.add(lex);
                    lexema = "";
                } else {
                    Pair lex = new Pair("int", lexema);
                    this.output.add(lex);
                    lexema = "";
                }
            }
            if (find(this.charPascal, lexema)) {
                String nextChar = new String();
                nextChar += this.input.charAt(i + 1);
                while (find(this.num, nextChar) || find(this.charPascal, nextChar) || nextChar.charAt(0) == '_') {
                    i++;
                    lexema += this.input.charAt(i);
                    nextChar = "";
                    nextChar += this.input.charAt(i + 1);
                }
                //ключевое слово языка
                if (find(this.keyWord, lexema)) {
                    Pair lex = new Pair("keyword", lexema);
                    this.output.add(lex);
                    lexema = "";
                } else { //идентификатор
                    Pair lex = new Pair("id", lexema);
                    this.output.add(lex);
                    lexema = "";
                }
            }
            if (lexema.length() == 1 && lexema.charAt(0) == ' ') {
                lexema = "";
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

    public void print() {
        for (int i = 0; i < this.output.size(); i++) {
            this.output.get(i).print();
        }
    }
}
