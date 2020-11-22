/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



/**
 *
 * @author refresh.jss
 */
public class SynAnalyzer {

    private ArrayList<Pair> lexems;
    private final GrammarInterface grammar;
    private final ArrayList<ArrayList<Situation>> table;
    private ArrayList<Integer> parseString;
    private final Pair dot = new Pair("$", "");
    private final Pair nterm = new Pair("nterm", "");

    SynAnalyzer(ArrayList<Pair> lexems, GrammarInterface grammar) {
        this.lexems = new ArrayList();
        this.table = new ArrayList(new ArrayList());
        this.lexems = lexems;
        this.grammar = grammar;
        this.parseString = new ArrayList();
    }

    public void makeTable() {
        int step = 0;
        ArrayList<Situation> ceil0 = new ArrayList();
        ArrayList<Rule> axiomRules = this.grammar.getRules(this.grammar.getAxiom());
        addDefaultRules(ceil0, axiomRules, step);
        boolean wasAdding = true;
        int currentIndexEnd = 0;
        int currentIndexNterm = 0;
        while (wasAdding) {
            ArrayList<Situation> situationWithDotInEnd = this.getSituationWithDotInEnd(ceil0, currentIndexEnd);
            if (!situationWithDotInEnd.isEmpty()) {
                currentIndexEnd = ceil0.size();
            }
            for (int i = 0; i < situationWithDotInEnd.size(); i++) {
                Pair left = situationWithDotInEnd.get(i).getRule().getLeft();
                this.addSituationWithDotAtFrontOf(ceil0, left, step);
            }
            ArrayList<Situation> situationWithDotAtFrontOfNterm = this.getSituationWithDotAtFrontOfNterm(ceil0, currentIndexNterm);
            if (!situationWithDotAtFrontOfNterm.isEmpty()) {
                currentIndexNterm = ceil0.size();
            }
            for (int i = 0; i < situationWithDotAtFrontOfNterm.size(); i++) {
                int posDot = situationWithDotAtFrontOfNterm.get(i).getRule().getPosSymbol(this.dot);
                Pair Nterm = situationWithDotAtFrontOfNterm.get(i).getRule().getPair(posDot + 1);
                ArrayList<Rule> possibleRules = this.grammar.getRules(Nterm);
                addDefaultRules(ceil0, possibleRules, step);
            }
            if (situationWithDotInEnd.size() + situationWithDotAtFrontOfNterm.size() == 0) {
                wasAdding = false;
            }
        }
        this.table.add(ceil0);
        step++;
        while (step <= this.lexems.size()) {
            ArrayList<Situation> ceil = new ArrayList();
            ArrayList<Situation> situationWithDotAtFrontOfCurrentTerm = getSituationWithDotAtFrontOf(this.table.get(step - 1), this.lexems.get(step - 1), -1);
            for (int i = 0; i < situationWithDotAtFrontOfCurrentTerm.size(); i++) {
                ceil.add(situationWithDotAtFrontOfCurrentTerm.get(i));
            }
            wasAdding = true;
            currentIndexEnd = 0;
            currentIndexNterm = 0;
            while (wasAdding) {
                ArrayList<Situation> situationWithDotInEnd = this.getSituationWithDotInEnd(ceil, currentIndexEnd);
                if (!situationWithDotInEnd.isEmpty()) {
                    currentIndexEnd = ceil.size();
                }
                for (int i = 0; i < situationWithDotInEnd.size(); i++) {
                    int index = situationWithDotInEnd.get(i).getPos();
                    Pair Nterm = situationWithDotInEnd.get(i).getRule().getLeft();
                    ArrayList<Situation> successMatchSituation = getSituationWithDotAtFrontOf(this.table.get(index), Nterm, -1);
                    for (int j = 0; j < successMatchSituation.size(); j++) {
                        ceil.add(successMatchSituation.get(j));
                    }
                }
                ArrayList<Situation> situationWithDotAtFrontOfNterm = this.getSituationWithDotAtFrontOfNterm(ceil, currentIndexNterm);
                if (!situationWithDotAtFrontOfNterm.isEmpty()) {
                    currentIndexNterm = ceil.size();
                }
                for (int i = 0; i < situationWithDotAtFrontOfNterm.size(); i++) {
                    int posDot = situationWithDotAtFrontOfNterm.get(i).getRule().getPosSymbol(this.dot);
                    Pair Nterm = situationWithDotAtFrontOfNterm.get(i).getRule().getPair(posDot + 1);
                    ArrayList<Rule> possibleRules = this.grammar.getRules(Nterm);
                    addDefaultRules(ceil, possibleRules, step);
                }
                if (situationWithDotInEnd.size() + situationWithDotAtFrontOfNterm.size() == 0) {
                    wasAdding = false;
                }
            }
            table.add(ceil);
            step++;
        }
    }

    private void addDefaultRules(ArrayList<Situation> container, ArrayList<Rule> rules, int pos) {
        for (int i = 0; i < rules.size(); i++) {
            Rule ruleWithDot = rules.get(i).getRuleWithDot(0);
            Situation currentSituation = new Situation(ruleWithDot, pos);
            if (!this.isFound(currentSituation, container)) {
                container.add(currentSituation);
            }
        }
    }

    private boolean isFound(Situation situation, ArrayList<Situation> container) {
        boolean isFound = false;
        int i = 0;
        while (!isFound && (i < container.size())) {
            if (situation.equals(container.get(i))) {
                isFound = true;
            } else {
                i++;
            }
        }
        return isFound;
    }

    private ArrayList<Situation> getSituationWithDotInEnd(ArrayList<Situation> current, int index) {
        ArrayList<Situation> result = new ArrayList();
        for (int i = index; i < current.size(); i++) {
            ArrayList<Pair> rightSideRule = current.get(i).getRule().getRight();
            if (this.dot.equals(rightSideRule.get(rightSideRule.size() - 1))) {
                Situation dotInEnd = new Situation(current.get(i).getRule().copy(), current.get(i).getPos());
                result.add(dotInEnd);
            }
        }
        return result;
    }

    private ArrayList<Situation> getSituationWithDotAtFrontOf(ArrayList<Situation> current, Pair symbol, int pos) {
        ArrayList<Situation> result = new ArrayList();
        for (int i = 0; i < current.size(); i++) {
            Rule rule = current.get(i).getRule().copy();
            int posDot = rule.getPosSymbol(this.dot);
            if ((posDot != -1) && (posDot + 1 < rule.getRight().size()) && symbol.equals(rule.getPair(posDot + 1))) {
                if (pos != -1) {
                    Situation success = new Situation(rule.swap(posDot, posDot + 1), pos);
                    result.add(success);
                } else {
                    int currentPos = current.get(i).getPos();
                    Situation success = new Situation(rule.swap(posDot, posDot + 1), currentPos);
                    result.add(success);
                }
            }
        }
        return result;
    }

    private void addSituationWithDotAtFrontOf(ArrayList<Situation> current, Pair symbol, int pos) {
        for (int i = 0; i < current.size(); i++) {
            Rule rule = current.get(i).getRule().copy();
            int posDot = rule.getPosSymbol(this.dot);
            if ((posDot != -1) && (posDot + 1 < rule.getRight().size()) && symbol.equals(rule.getPair(posDot + 1))) {
                Situation success = new Situation(rule.swap(posDot, posDot + 1), pos);
                current.add(success);
            }
        }
    }

    public void printTable() throws UnsupportedEncodingException {
        PrintStream ps = new PrintStream(System.out, false, "utf-8");
        for (int i = 0; i < this.table.size(); i++) {
            ps.println("========  I[" + i + "] ========");
            ArrayList<Situation> situation = this.table.get(i);
            for (int j = 0; j < situation.size(); j++) {
                situation.get(j).print();
                ps.println();
            }
        }
    }

    private ArrayList<Situation> getSituationWithDotAtFrontOfNterm(ArrayList<Situation> current, int index) {
        ArrayList<Situation> result = new ArrayList();
        for (int i = index; i < current.size(); i++) {
            Rule rule = current.get(i).getRule().copy();
            int posDot = rule.getPosSymbol(this.dot);
            if ((posDot != -1) && (posDot + 1 < rule.getRight().size()) && this.nterm.equals(rule.getPair(posDot + 1))) {
                Situation dotAtFrontOfNterm = new Situation(rule, current.get(i).getPos());
                result.add(dotAtFrontOfNterm);
            }
        }
        return result;
    }
    
/**
 *
 * @author katebekk
 */  
    private Rule noDots(Rule rule){
        Rule newRule = new Rule(rule.getLeft(), null);
        ArrayList<Pair> right = new ArrayList();
        for(int i = 0; i < rule.getRight().size(); i++){
            if(rule.getRight().get(i).getType() != this.dot.getType()){
                right.add(rule.getRight().get(i));
            }
        }
        newRule.setRight(right);
        return newRule;
    }
     
    public void parse(){
        int tableSize = this.table.size() - 1;
        int colSize = this.table.get(tableSize).size() - 1; 
        //последняя ситуация последнего столбца
        Situation lex = this.table.get(tableSize).get(colSize);
        procedureR(lex,lexems.size()-1);
    }
    
    public void procedureR(Situation situation, Integer j){
        Rule rule = noDots(situation.getRule());
        parseString.add(this.grammar.getRuleIndex(rule));
        int m = situation.getRule().getRight().size()-1;
        int k = m;
        int c = j;
        while( k != 0 ){
            if(situation.getRule().getRight().get(k).getType() != "nterm"){
                k --;
                c --;
            }else {
                ArrayList<Situation> sit = new ArrayList();
                ArrayList<Situation> tableSt = this.table.get(c);//Ik table
                Pair left = situation.getRule().getRight().get(k);//Xk
                //находим ситуации в Ik
                for(int i = 0; i < tableSt.size(); i++ ){
                    if (left.equals(tableSt.get(i).getRule().getLeft())) {
                       sit.add(tableSt.get(i));
                    }
                }
                //из них выбираем верное
                int r = 0;
                Situation rSituation = situation;
                for(int i = 0; i < sit.size(); i++ ){
                   if(this.table.get(sit.get(i).getPos()).contains(situation)){
                       rSituation = sit.get(i);
                       r = rSituation.getPos();
                       procedureR(rSituation, c);
                   }  
                }
                procedureR(rSituation, c);
                k --;
                c = r;
            }
        }
        
    }
    
    
    public ParseTree buildTree(ArrayList<Integer> numb_seq){
        int last_item = numb_seq.size() - 1;
        Rule root_rule = this.grammar.getRuleByIndex(numb_seq.get(last_item));
        ParseTree tree = new ParseTree(root_rule.getLeft());
        walk(tree.getRoot(), numb_seq, last_item);
        return tree;
    }
    
    public void walk(TreeItem root, ArrayList<Integer> numb_seq, Integer index ){
        int num = index;       
        Rule cur_rule = this.grammar.getRuleByIndex(numb_seq.get(index));//получаем текущее правило
        //присваеваем правую часть детям текущего узла
        ArrayList<Pair> childs = cur_rule.getRight();
        root.addChilds(childs);
        int cs = childs.size();
        if(cs > 0 ){
        int number = childs.size()-1;
        
        TreeItem walker = root.getChilds().get(number);
       // обходит детей текущего узла
        while(walker != root){
            if(walker.getVal().getType() == "nterm"){//если нетерминал
                  num --;
                  walk(walker, numb_seq, num);
                  if(number != 0){
                  number --;
                  walker = root.getChilds().get(number);
                  }else walker = walker.getParent();
                
            }else if(number >0){
                number --;
                walker = root.getChilds().get(number);
            } else {
                walker = walker.getParent();
            }
        }
        }
    }
    
    
    
}
