package com.mycompany.mavenproject2;

import java.util.ArrayList;

/**
 *
 * @author kate
 */
public class TreeItem {

    private TreeItem parent;
    private Pair val;
    private ArrayList<TreeItem> childs;

    TreeItem(Pair pair) {
        this.val = pair;
        this.childs = new ArrayList();
    }

    public void setParent(TreeItem par) {
        this.parent = par;
    }

    public void addChilds(ArrayList<Pair> list) {
        for (int i = 0; i < list.size(); i++) {
            TreeItem newChild = new TreeItem(list.get(i).copy());
            newChild.setParent(this);
            this.childs.add(i, newChild);
        }
    }

    public ArrayList<TreeItem> getChilds() {
        return this.childs;
    }

    public TreeItem getParent() {
        return this.parent;
    }

    public Pair getVal() {
        return this.val;
    }
    
    public void setChilds(ArrayList<TreeItem> childs) {
        this.childs = childs;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TreeItem)) {
            return false;
        }
        TreeItem c = (TreeItem) o;
        boolean eq = true;
        if (c.getVal().equals(this.getVal())) {
            if (c.getChilds().size() != this.getChilds().size()) {
                eq = false;
            }
            int i = 0;
            while (i < this.childs.size() && eq) {
                eq = this.getChilds().get(i).equals(c.getChilds().get(i));
                i++;
            }
        }
        return eq;
    }
}
