package com.mycompany.mavenproject2;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author refresh.jss
 */
public class PascalGrammar implements GrammarInterface {

    private final Pair axiom;
    private final ArrayList<Rule> rules;

    PascalGrammar(Pair axiom) {
        this.axiom = axiom;
        this.rules = new ArrayList();
        fillRules();
    }

    @Override
    public void fillRules() {
        ArrayList<Pair> right = new ArrayList();
        right.add(new Pair("nterm", "раздел описания"));
        right.add(new Pair("nterm", "составной оператор"));
        right.add(new Pair("separator", "."));
        Rule r1 = new Rule(new Pair("nterm", "программа"), right);
        this.rules.add(r1);
        ArrayList<Pair> right1 = new ArrayList();
        right1.add(new Pair("keyword", "begin"));
        right1.add(new Pair("nterm", "операторы"));
        right1.add(new Pair("keyword", "end"));
        Rule r2 = new Rule(new Pair("nterm", "составной оператор"), right1);
        this.rules.add(r2);
        ArrayList<Pair> right2 = new ArrayList();
        right2.add(new Pair("keyword", "var"));
        right2.add(new Pair("nterm", "список имен"));
        right2.add(new Pair("separator", ":"));
        right2.add(new Pair("nterm", "тип"));
        right2.add(new Pair("separator", ";"));
        right2.add(new Pair("nterm", "раздел описания"));
        Rule r3 = new Rule(new Pair("nterm", "раздел описания"), right2);
        this.rules.add(r3);
        ArrayList<Pair> right3 = new ArrayList();
        right3.add(new Pair("keyword", "var"));
        right3.add(new Pair("id", ""));
        right3.add(new Pair("separator", ":"));
        right3.add(new Pair("nterm", "тип"));
        right3.add(new Pair("assignment", ":="));
        right3.add(new Pair("nterm", "выражение"));
        right3.add(new Pair("separator", ";"));
        right3.add(new Pair("nterm", "раздел описания"));
        Rule r4 = new Rule(new Pair("nterm", "раздел описания"), right3);
        this.rules.add(r4);
        ArrayList<Pair> right4 = new ArrayList();
        right4.add(new Pair("keyword", "var"));
        right4.add(new Pair("nterm", "список имен"));
        right4.add(new Pair("separator", ":"));
        right4.add(new Pair("nterm", "тип"));
        right4.add(new Pair("separator", ";"));
        Rule r5 = new Rule(new Pair("nterm", "раздел описания"), right4);
        this.rules.add(r5);
        ArrayList<Pair> right5 = new ArrayList();
        right5.add(new Pair("keyword", "var"));
        right5.add(new Pair("id", ""));
        right5.add(new Pair("separator", ":"));
        right5.add(new Pair("nterm", "тип"));
        right5.add(new Pair("assignment", ":="));
        right5.add(new Pair("nterm", "выражение"));
        right5.add(new Pair("separator", ";"));
        Rule r6 = new Rule(new Pair("nterm", "раздел описания"), right5);
        this.rules.add(r6);
        ArrayList<Pair> right6 = new ArrayList();
        right6.add(new Pair("keyword", "integer"));
        Rule r7 = new Rule(new Pair("nterm", "тип"), right6);
        this.rules.add(r7);
        ArrayList<Pair> right7 = new ArrayList();
        right7.add(new Pair("keyword", "real"));
        Rule r8 = new Rule(new Pair("nterm", "тип"), right7);
        this.rules.add(r8);
        ArrayList<Pair> right8 = new ArrayList();
        right8.add(new Pair("keyword", "boolean"));
        Rule r9 = new Rule(new Pair("nterm", "тип"), right8);
        this.rules.add(r9);
        ArrayList<Pair> right9 = new ArrayList();
        right9.add(new Pair("keyword", "char"));
        Rule r10 = new Rule(new Pair("nterm", "тип"), right9);
        this.rules.add(r10);
        ArrayList<Pair> right10 = new ArrayList();
        right10.add(new Pair("keyword", "string"));
        Rule r11 = new Rule(new Pair("nterm", "тип"), right10);
        this.rules.add(r11);
        ArrayList<Pair> right11 = new ArrayList();
        right11.add(new Pair("nterm", "выражение"));
        right11.add(new Pair("plus operator", ""));
        right11.add(new Pair("nterm", "операнд T"));
        Rule r12 = new Rule(new Pair("nterm", "выражение"), right11);
        this.rules.add(r12);
        ArrayList<Pair> right12 = new ArrayList();
        right12.add(new Pair("nterm", "операнд T"));
        Rule r13 = new Rule(new Pair("nterm", "выражение"), right12);
        this.rules.add(r13);
        ArrayList<Pair> right13 = new ArrayList();
        right13.add(new Pair("nterm", "операнд T"));
        right13.add(new Pair("mult operator", ""));
        right13.add(new Pair("nterm", "операнд F"));
        Rule r14 = new Rule(new Pair("nterm", "операнд T"), right13);
        this.rules.add(r14);
        ArrayList<Pair> right14 = new ArrayList();
        right14.add(new Pair("nterm", "операнд F"));
        Rule r15 = new Rule(new Pair("nterm", "операнд T"), right14);
        this.rules.add(r15);
        ArrayList<Pair> right15 = new ArrayList();
        right15.add(new Pair("bracket", "("));
        right15.add(new Pair("nterm", "выражение"));
        right15.add(new Pair("bracket", ")"));
        Rule r16 = new Rule(new Pair("nterm", "операнд F"), right15);
        this.rules.add(r16);
        ArrayList<Pair> right16 = new ArrayList();
        right16.add(new Pair("id", ""));
        Rule r17 = new Rule(new Pair("nterm", "операнд F"), right16);
        this.rules.add(r17);
        ArrayList<Pair> right17 = new ArrayList();
        right17.add(new Pair("int", ""));
        Rule r18 = new Rule(new Pair("nterm", "операнд F"), right17);
        this.rules.add(r18);
        ArrayList<Pair> right18 = new ArrayList();
        right18.add(new Pair("real", ""));
        Rule r19 = new Rule(new Pair("nterm", "операнд F"), right18);
        this.rules.add(r19);
        ArrayList<Pair> right19 = new ArrayList();
        right19.add(new Pair("char", ""));
        Rule r20 = new Rule(new Pair("nterm", "операнд F"), right19);
        this.rules.add(r20);
        ArrayList<Pair> right20 = new ArrayList();
        right20.add(new Pair("string", ""));
        Rule r21 = new Rule(new Pair("nterm", "операнд F"), right20);
        this.rules.add(r21);
        ArrayList<Pair> right21 = new ArrayList();
        right21.add(new Pair("nterm", "имя функции"));
        right21.add(new Pair("bracket", "("));
        right21.add(new Pair("nterm", "выражение"));
        right21.add(new Pair("bracket", ")"));
        Rule r22 = new Rule(new Pair("nterm", "операнд F"), right21);
        this.rules.add(r22);
        ArrayList<Pair> right22 = new ArrayList();
        right22.add(new Pair("nterm", "булево"));
        Rule r23 = new Rule(new Pair("nterm", "операнд F"), right22);
        this.rules.add(r23);
        ArrayList<Pair> right23 = new ArrayList();
        right23.add(new Pair("keyword", "sqr"));
        Rule r24 = new Rule(new Pair("nterm", "имя функции"), right23);
        this.rules.add(r24);
        ArrayList<Pair> right24 = new ArrayList();
        right24.add(new Pair("keyword", "sqrt"));
        Rule r25 = new Rule(new Pair("nterm", "имя функции"), right24);
        this.rules.add(r25);
        ArrayList<Pair> right25 = new ArrayList();
        right25.add(new Pair("keyword", "abs"));
        Rule r26 = new Rule(new Pair("nterm", "имя функции"), right25);
        this.rules.add(r26);
        ArrayList<Pair> right26 = new ArrayList();
        right26.add(new Pair("keyword", "exp"));
        Rule r27 = new Rule(new Pair("nterm", "имя функции"), right26);
        this.rules.add(r27);
        ArrayList<Pair> right27 = new ArrayList();
        right27.add(new Pair("keyword", "write"));
        Rule r28 = new Rule(new Pair("nterm", "имя функции"), right27);
        this.rules.add(r28);
        ArrayList<Pair> right28 = new ArrayList();
        right28.add(new Pair("keyword", "writeln"));
        Rule r29 = new Rule(new Pair("nterm", "имя функции"), right28);
        this.rules.add(r29);
        ArrayList<Pair> right29 = new ArrayList();
        right29.add(new Pair("keyword", "readln"));
        Rule r30 = new Rule(new Pair("nterm", "имя функции"), right29);
        this.rules.add(r30);
        ArrayList<Pair> right30 = new ArrayList();
        right30.add(new Pair("id", ""));
        Rule r31 = new Rule(new Pair("nterm", "список имен"), right30);
        this.rules.add(r31);
        ArrayList<Pair> right31 = new ArrayList();
        right31.add(new Pair("id", ""));
        right31.add(new Pair("separator", ","));
        right31.add(new Pair("nterm", "список имен"));
        Rule r32 = new Rule(new Pair("nterm", "список имен"), right31);
        this.rules.add(r32);
        ArrayList<Pair> right32 = new ArrayList();
        right32.add(new Pair("nterm", "присваивание"));
        right32.add(new Pair("nterm", "операторы"));
        Rule r33 = new Rule(new Pair("nterm", "операторы"), right32);
        this.rules.add(r33);
        ArrayList<Pair> right33 = new ArrayList();
        right33.add(new Pair("nterm", "цикл"));
        right33.add(new Pair("nterm", "операторы"));
        Rule r34 = new Rule(new Pair("nterm", "операторы"), right33);
        this.rules.add(r34);
        ArrayList<Pair> right34 = new ArrayList();
        right34.add(new Pair("nterm", "ветвление"));
        right34.add(new Pair("nterm", "операторы"));
        Rule r35 = new Rule(new Pair("nterm", "операторы"), right34);
        this.rules.add(r35);
        ArrayList<Pair> right35 = new ArrayList();
        right35.add(new Pair("nterm", "присваивание"));
        Rule r36 = new Rule(new Pair("nterm", "операторы"), right35);
        this.rules.add(r36);
        ArrayList<Pair> right36 = new ArrayList();
        right36.add(new Pair("nterm", "цикл"));
        Rule r37 = new Rule(new Pair("nterm", "операторы"), right36);
        this.rules.add(r37);
        ArrayList<Pair> right37 = new ArrayList();
        right37.add(new Pair("nterm", "ветвление"));
        Rule r38 = new Rule(new Pair("nterm", "операторы"), right37);
        this.rules.add(r38);
        ArrayList<Pair> right38 = new ArrayList();
        right38.add(new Pair("id", ""));
        right38.add(new Pair("nterm", "знак присваивания"));
        right38.add(new Pair("nterm", "выражение"));
        right38.add(new Pair("separator", ";"));
        Rule r39 = new Rule(new Pair("nterm", "присваивание"), right38);
        this.rules.add(r39);
        ArrayList<Pair> right39 = new ArrayList();
        right39.add(new Pair("assignment", ":="));
        Rule r40 = new Rule(new Pair("nterm", "знак присваивания"), right39);
        this.rules.add(r40);
        ArrayList<Pair> right40 = new ArrayList();
        right40.add(new Pair("assignment", "+="));
        Rule r41 = new Rule(new Pair("nterm", "знак присваивания"), right40);
        this.rules.add(r41);
        ArrayList<Pair> right41 = new ArrayList();
        right41.add(new Pair("assignment", "-="));
        Rule r42 = new Rule(new Pair("nterm", "знак присваивания"), right41);
        this.rules.add(r42);
        ArrayList<Pair> right42 = new ArrayList();
        right42.add(new Pair("assignment", "/="));
        Rule r43 = new Rule(new Pair("nterm", "знак присваивания"), right42);
        this.rules.add(r43);
        ArrayList<Pair> right43 = new ArrayList();
        right43.add(new Pair("assignment", "*="));
        Rule r44 = new Rule(new Pair("nterm", "знак присваивания"), right43);
        this.rules.add(r44);
        ArrayList<Pair> right44 = new ArrayList();
        right44.add(new Pair("ntermm", "цикл for"));
        Rule r45 = new Rule(new Pair("nterm", "цикл"), right44);
        this.rules.add(r45);
        ArrayList<Pair> right45 = new ArrayList();
        right45.add(new Pair("ntermm", "цикл while"));
        Rule r46 = new Rule(new Pair("nterm", "цикл"), right45);
        this.rules.add(r46);
        ArrayList<Pair> right46 = new ArrayList();
        right46.add(new Pair("ntermm", "цикл repeat"));
        Rule r47 = new Rule(new Pair("nterm", "цикл"), right46);
        this.rules.add(r47);
        ArrayList<Pair> right47 = new ArrayList();
        right47.add(new Pair("keyword", "for"));
        right47.add(new Pair("id", ""));
        right47.add(new Pair("assignment", ":="));
        right47.add(new Pair("int", ""));
        right47.add(new Pair("keyword", "to"));
        right47.add(new Pair("int", ""));
        right47.add(new Pair("keyword", "do"));
        right47.add(new Pair("nterm", "составной оператор"));
        right47.add(new Pair("separator", ";"));
        Rule r48 = new Rule(new Pair("nterm", "цикл for"), right47);
        this.rules.add(r48);
        ArrayList<Pair> right48 = new ArrayList();
        right48.add(new Pair("keyword", "for"));
        right48.add(new Pair("id", ""));
        right48.add(new Pair("assignment", ":="));
        right48.add(new Pair("int", ""));
        right48.add(new Pair("keyword", "downto"));
        right48.add(new Pair("int", ""));
        right48.add(new Pair("keyword", "do"));
        right48.add(new Pair("nterm", "составной оператор"));
        right48.add(new Pair("separator", ";"));
        Rule r49 = new Rule(new Pair("nterm", "цикл for"), right48);
        this.rules.add(r49);
        ArrayList<Pair> right49 = new ArrayList();
        right49.add(new Pair("keyword", "while"));
        right49.add(new Pair("bracket", "("));
        right49.add(new Pair("nterm", "булево выражение"));
        right49.add(new Pair("bracket", ")"));
        right49.add(new Pair("nterm", "составной оператор"));
        right49.add(new Pair("separator", ";"));
        Rule r50 = new Rule(new Pair("nterm", "цикл while"), right49);
        this.rules.add(r50);
        ArrayList<Pair> right50 = new ArrayList();
        right50.add(new Pair("nterm", "выражение"));
        right50.add(new Pair("compare", ""));
        right50.add(new Pair("nterm", "выражение"));
        Rule r51 = new Rule(new Pair("nterm", "булево выражение"), right50);
        this.rules.add(r51);
        ArrayList<Pair> right51 = new ArrayList();
        right51.add(new Pair("nterm", "булево"));
        Rule r52 = new Rule(new Pair("nterm", "булево выражение"), right51);
        this.rules.add(r52);
        ArrayList<Pair> right52 = new ArrayList();
        right52.add(new Pair("keyword", "true"));
        Rule r53 = new Rule(new Pair("nterm", "булево"), right52);
        this.rules.add(r53);
        ArrayList<Pair> right53 = new ArrayList();
        right53.add(new Pair("keyword", "false"));
        Rule r54 = new Rule(new Pair("nterm", "булево"), right53);
        this.rules.add(r54);
        ArrayList<Pair> right54 = new ArrayList();
        right54.add(new Pair("keyword", "repeat"));
        right54.add(new Pair("nterm", "составной оператор"));
        right54.add(new Pair("separator", ";"));
        right54.add(new Pair("bracket", "("));
        right54.add(new Pair("nterm", "булево выражение"));
        right54.add(new Pair("keyword", ")"));
        Rule r55 = new Rule(new Pair("nterm", "цикл repeat"), right54);
        this.rules.add(r55);
        ArrayList<Pair> right55 = new ArrayList();
        right55.add(new Pair("nterm", "ветвление if"));
        Rule r56 = new Rule(new Pair("nterm", "ветвление"), right55);
        this.rules.add(r56);
        ArrayList<Pair> right56 = new ArrayList();
        right56.add(new Pair("nterm", "ветвление case"));
        Rule r57 = new Rule(new Pair("nterm", "ветвление"), right56);
        this.rules.add(r57);
        ArrayList<Pair> right57 = new ArrayList();
        right57.add(new Pair("keyword", "if"));
        right57.add(new Pair("bracket", "("));
        right57.add(new Pair("nterm", "булево выражение"));
        right57.add(new Pair("bracket", ")"));
        right57.add(new Pair("keyword", "then"));
        right57.add(new Pair("nterm", "составной оператор"));
        right57.add(new Pair("separator", ";"));
        Rule r58 = new Rule(new Pair("nterm", "ветвление if"), right57);
        this.rules.add(r58);
        ArrayList<Pair> right58 = new ArrayList();
        right58.add(new Pair("keyword", "if"));
        right58.add(new Pair("bracket", "("));
        right58.add(new Pair("nterm", "булево выражение"));
        right58.add(new Pair("bracket", ")"));
        right58.add(new Pair("keyword", "then"));
        right58.add(new Pair("nterm", "составной оператор"));
        right58.add(new Pair("keyword", "else"));
        right58.add(new Pair("nterm", "составной оператор"));
        right58.add(new Pair("separator", ";"));
        Rule r59 = new Rule(new Pair("nterm", "ветвление if"), right58);
        this.rules.add(r59);
        ArrayList<Pair> right59 = new ArrayList();
        right59.add(new Pair("keyword", "case"));
        right59.add(new Pair("id", ""));
        right59.add(new Pair("keyword", "of"));
        right59.add(new Pair("nterm", "переключатель"));
        right59.add(new Pair("keyword", "else"));
        right59.add(new Pair("nterm", "составной оператор"));
        right59.add(new Pair("separator", ";"));
        right59.add(new Pair("keyword", "end"));
        right59.add(new Pair("separator", ";"));
        Rule r60 = new Rule(new Pair("nterm", "ветвление case"), right59);
        this.rules.add(r60);
        ArrayList<Pair> right60 = new ArrayList();
        right60.add(new Pair("nterm", "выбор"));
        right60.add(new Pair("setarator", ":"));
        right60.add(new Pair("nterm", "составной оператор"));
        right60.add(new Pair("separator", ";"));
        Rule r61 = new Rule(new Pair("nterm", "переключатель"), right60);
        this.rules.add(r61);
        ArrayList<Pair> right61 = new ArrayList();
        right61.add(new Pair("nterm", "выбор"));
        right61.add(new Pair("setarator", ":"));
        right61.add(new Pair("nterm", "составной оператор"));
        right61.add(new Pair("separator", ";"));
        right61.add(new Pair("nterm", "переключатель"));
        Rule r62 = new Rule(new Pair("nterm", "переключатель"), right61);
        this.rules.add(r62);
        ArrayList<Pair> right62 = new ArrayList();
        right62.add(new Pair("char", ""));
        Rule r63 = new Rule(new Pair("nterm", "выбор"), right62);
        this.rules.add(r63);
        ArrayList<Pair> right63 = new ArrayList();
        right63.add(new Pair("string", ""));
        Rule r64 = new Rule(new Pair("nterm", "выбор"), right63);
        this.rules.add(r64);
        ArrayList<Pair> right64 = new ArrayList();
        right64.add(new Pair("int", ""));
        Rule r65 = new Rule(new Pair("nterm", "выбор"), right64);
        this.rules.add(r65);
        ArrayList<Pair> right65 = new ArrayList();
        right65.add(new Pair("real", ""));
        Rule r66 = new Rule(new Pair("nterm", "выбор"), right65);
        this.rules.add(r66);
        ArrayList<Pair> right66 = new ArrayList();
        right66.add(new Pair("nterm", "булево"));
        Rule r67 = new Rule(new Pair("nterm", "выбор"), right66);
        this.rules.add(r67);
    }

    @Override
    public void print() throws UnsupportedEncodingException {
        PrintStream ps = new PrintStream(System.out,false,"utf-8");
        ps.print("Аксиома грамматики - ");
        this.axiom.print();
        ps.println();
        ps.println("Правила грамматики:");
        for (int i = 0; i < this.rules.size(); i++) {
            ps.println("==========  " + i + "  ==========");
            this.rules.get(i).print();
            ps.println();
        }
    }
    
    @Override
    public Pair getAxiom() {
        return this.axiom;
    }

    @Override
    public ArrayList<Rule> getRules(Pair left) {
        ArrayList<Rule> result = new ArrayList();
        for (int i = 0; i < this.rules.size(); i++) {
            if (left.equals(this.rules.get(i).getLeft())) {
                result.add(this.rules.get(i));
            }
        }
        return result;
    }
    

    @Override
    public Rule getRuleByIndex(int index) {
        if(index < this.rules.size())
            return this.rules.get(index);
        else return null;
    }
    
    @Override
    public int getRuleIndex(Rule rule){
       for (int i = 0; i < this.rules.size(); i++) {
           
          if(this.rules.get(i).getLeft().equals(rule.getLeft())  && this.rules.get(i).getRight().equals(rule.getRight())){
             return i;   
          } 
    
       }
       return -1;
    }
}
