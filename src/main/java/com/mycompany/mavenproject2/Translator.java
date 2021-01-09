/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author refreshjss
 */
public class Translator {

    private final GrammarInterface grammar;
    private ArrayList<Integer> parseString;
    private ArrayList<Pair> lexems;
    private String body = "";
    private String header = "";
    private ArrayList<Integer> type = new ArrayList() {
        {
            add(6);
            add(7);
            add(8);
            add(9);
            add(10);
        }
    };

    Translator(GrammarInterface grammar, ArrayList<Integer> parseString, ArrayList<Pair> lexems) {
        this.grammar = grammar;
        this.parseString = parseString;
        this.lexems = lexems;
    }

    public void translate(String filename) {
        prepare();
        fillValues();
        formatIO();
        formatFunc();
        formatCode();
        if (!this.header.isEmpty()) {
            this.header += "\nusing namespace std;\n\n";
        }
        String program = this.header + this.body;
        try ( FileWriter writer = new FileWriter(filename, false)) {
            writer.write(program);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void prepare() {
        int stash = 0;
        for (int i = 0; i < this.parseString.size(); i++) {
            Rule current = this.grammar.getRuleByIndex(parseString.get(i));
            String right = "";
            right = current.getRight().stream().map(pair -> pair.toString() + " ").reduce(right, String::concat);
            String left = current.getLeft().toString();
            if (this.body.isEmpty()) {
                this.body += right;
            } else {
                if (stash == 0 && this.type.contains(parseString.get(i))) {
                    stash = parseString.get(i);
                } else {
                    if (stash != 0 && this.type.contains(parseString.get(i))) {
                        Rule typeRule = this.grammar.getRuleByIndex(stash);
                        right = "";
                        right = typeRule.getRight().stream().map(pair -> pair.toString() + " ").reduce(right, String::concat);
                        left = typeRule.getLeft().toString();
                        stash = parseString.get(i);
                    }
                    if (i == 1) {
                        this.body = replaceLast(this.body, left, right).replace("<", "<раздел описания> <");
                    } else {
                        this.body = replaceLast(this.body, left, right);
                    }
                }
            }
        }
        if (stash != 0) {
            Rule typeRule = this.grammar.getRuleByIndex(stash);
            String right = "";
            right = typeRule.getRight().stream().map(pair -> pair.toString() + " ").reduce(right, String::concat);
            String left = typeRule.getLeft().toString();
            this.body = replaceLast(this.body, left, right);
        }
    }

    private void fillValues() {
        for (int i = 0; i < this.lexems.size(); i++) {
            String typeOfLexem = this.lexems.get(i).getType();
            String value = this.lexems.get(i).getName();
            switch (typeOfLexem) {
                case "id" -> {
                    this.body = replaceFirst(this.body, "?id?", value);
                }
                case "compare" -> {
                    if (value.equals("=")) {
                        value = "==";
                    }
                    if (value.equals("<>")) {
                        value = "!=";
                    }
                    this.body = replaceFirst(this.body, "?compare?", value);
                }
                case "real" -> {
                    this.body = replaceFirst(this.body, "?real?", value);
                }
                case "integer" -> {
                    this.body = replaceFirst(this.body, "?integer?", value);
                }
                case "char" -> {
                    this.body = replaceFirst(this.body, "?char?", value);
                }
                case "string" -> {
                    this.body = replaceFirst(this.body, "?string?", value.replace("\'", "\""));
                }
                case "plus operator" -> {
                    this.body = replaceFirst(this.body, "?plus operator?", value);
                }
                case "mult operator" -> {
                    this.body = replaceFirst(this.body, "?mult operator?", value);
                }
                case "keyword" -> {
                    if (value.equals("for")) {
                        i++;
                        value = this.lexems.get(i).getName();
                        this.body = replaceFirst(this.body, "?id?", value);
                        this.body = replaceFirst(this.body, "?id?", value);
                        this.body = replaceFirst(this.body, "?id?", value);
                    }
                }
            }
        }
    }

    private void formatIO() {
        int start = this.body.indexOf("cin");
        if (start != -1) {
            this.header += "#include <iostream> \n";
            while (start != -1) {
                int end = this.body.indexOf(";", start);
                String frontOld = this.body.substring(0, start);
                String backOld = this.body.substring(end, this.body.length());
                String cin = this.body.substring(start, end).replace(",", " >> ");
                this.body = frontOld + cin + backOld;
                start = this.body.indexOf("cin", end);
            }
        }
        start = this.body.indexOf("cout");
        if (start != -1) {
            if (this.header.isEmpty()) {
                this.header += "#include <iostream> \n";
            }
            while (start != -1) {
                int end = this.body.indexOf(";", start);
                String frontOld = this.body.substring(0, start);
                String backOld = this.body.substring(end, this.body.length());
                String cout = this.body.substring(start, end).replace(",", " << ");
                this.body = frontOld + cout + backOld;
                start = this.body.indexOf("cout", end);
            }
        }
    }

    private void formatCode() {
        this.body = this.body.replaceAll("( +)", " ").trim().replaceAll("( ;)", ";");
        int start = 0;
        int end = 0;
        String newBody = "";
        int counterOfTab = 0;
        for (int i = 0; i < this.body.length(); i++) {
            char current = this.body.charAt(i);
            if (current == '{' || current == '}' || current == ';') {
                end = i;
                if (current == '{') {
                    counterOfTab++;
                }
                if (current == '}') {
                    counterOfTab--;
                }
                newBody += this.body.substring(start, end + 1)
                        + "\n"
                        + "    ".repeat(counterOfTab);
                start = end + 2;
            }
        }
        this.body = newBody;
    }

    private void formatFunc() {
        if (this.parseString.contains(21)) {
            this.header += "#include <cmath>\n";
        }
        if (this.parseString.contains(23)) {
            int start = this.body.indexOf("pow");
            if (start != -1) {
                while (start != -1) {
                    int end = this.body.indexOf(")", start);
                    String frontOld = this.body.substring(0, start);
                    String backOld = this.body.substring(end, this.body.length());
                    String pow = this.body.substring(start, end - 1) + ",2";
                    this.body = frontOld + pow + backOld;
                    start = this.body.indexOf("pow", end);
                }
            }
        }
    }

    private String replaceLast(String input, String oldSub, String newSub) {
        int start = 0;
        int end = 0;
        int countOfEq = 0;
        boolean isFind = false;
        int i = input.length() - 1;
        while (i > 0 && !isFind) {
            if (input.charAt(i) == oldSub.charAt(oldSub.length() - countOfEq - 1)) {
                if (countOfEq == 0) {
                    end = i;
                }
                if (countOfEq == oldSub.length() - 1) {
                    isFind = true;
                    start = i;
                }
                countOfEq++;
            } else {
                countOfEq = 0;
            }
            i--;
        }
        return input.substring(0, start) + newSub + input.substring(end + 1, input.length());
    }

    private String replaceFirst(String input, String oldSub, String newSub) {
        int start = 0;
        int end = 0;
        int countOfEq = 0;
        boolean isFind = false;
        int i = 0;
        while (i < input.length() && !isFind) {
            if (input.charAt(i) == oldSub.charAt(countOfEq)) {
                if (countOfEq == 0) {
                    start = i;
                }
                if (countOfEq == oldSub.length() - 1) {
                    isFind = true;
                    end = i;
                }
                countOfEq++;
            } else {
                countOfEq = 0;
            }
            i++;
        }
        return input.substring(0, start) + newSub + input.substring(end + 1, input.length());
    }
}
