/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 *
 * @author refresh.jss
 */
public interface GrammarInterface {

    /**
     * Filling grammar by rules
     */
    public void fillRules();
    public void print() throws UnsupportedEncodingException;
    public Pair getAxiom();
    public ArrayList<Rule> getRules(Pair left);
    public Rule getRuleByIndex(int index);
}
