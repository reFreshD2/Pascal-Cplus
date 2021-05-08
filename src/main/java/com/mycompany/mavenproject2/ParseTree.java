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

    ParseTree(Pair val) {
        this.root = new TreeItem(val);
        this.root.setParent(null);
    }

    public TreeItem getRoot() {
        return root;
    }
    
    public ArrayList<Pair> getLexem() {
        ArrayList<Pair> lexems = new ArrayList();
        return this.recLexem(this.root, lexems);
    }
    
    private ArrayList<Pair> recLexem(TreeItem item, ArrayList<Pair> lexems) {
        for (int i = 0; i < item.getChilds().size(); i++) {
            if (item.getChilds().get(i).getVal().getType().equals("nterm")) {
                lexems = recLexem(item.getChilds().get(i), lexems);
                continue;
            }
            lexems.add(item.getChilds().get(i).getVal());
        }
        return lexems;
    }
}
