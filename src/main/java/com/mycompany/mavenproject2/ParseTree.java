package com.mycompany.mavenproject2;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author kate
 */
public class ParseTree {
    private TreeItem root;
    
    ParseTree(Pair val){
        this.root  = new TreeItem(val);
        this.root.setParent(null);
    }
    public TreeItem getRoot(){
        return root;
    }
}
