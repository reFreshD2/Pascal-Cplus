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
 
    
    TreeItem(Pair pair){
        this.val = pair;
        this.childs = new ArrayList();
    }
    
    public void setParent(TreeItem par){
        this.parent = par;
    }
   
    
    public void addChilds(ArrayList<Pair> list){
        int childs_numb = list.size();
        for(int i = 0; i < childs_numb; i++){
            TreeItem newChild = new TreeItem(list.get(i));
            newChild.setParent(this);
            this.childs.add(i, newChild);
            
            
        }
    }
    public ArrayList<TreeItem> getChilds(){
         return this.childs;
    }
    public TreeItem getParent(){
         return this.parent;
    }
    public Pair getVal(){
         return this.val;
    }

    
}
