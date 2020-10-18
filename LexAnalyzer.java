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
            "case", "break","real", "char", "string", 
            "boolean", "abs", "sqr", "sqrt","exp", "write",
            "writeln", "readln", "true", "false"
    };
   
   private final String[] bracket = {
            "(",")"
   };
   
   private final String[] num = {
            "0","1","2","3","4","5","6","7","8","9"
   };
   
   private final String[] compare = {
            ">","<","<=",">=","<>","="
   };
   
   private final String[] charPascal = {
            "q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n","m",
            "Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M",
            "й","ц","у","к","е","н","г","ш","щ","з","х","ъ","ф","ы","в","а","п","р","о","л","д","ж","э","я","ч","с","м","и","т","ь","б","ю",
            "Й","Ц","У","К","Е","Н","Г","Ш","Щ","З","Х","Ъ","Ф","Ы","В","А","П","Р","О","Л","Д","Ж","Э","Я","Ч","С","М","И","Т","Ь","Б","Ю"
   };  
   String input;
   ArrayList<Pair> output;
   
   private void setInput(String fileName){
       try (FileReader fr = new FileReader(fileName); Scanner scan = new Scanner(fr)) {
            while (scan.hasNextLine()) {
		input = input + scan.nextLine();
            }
            fr.close();
	} catch (IOException e) {
            System.out.println("File err" + e);
	}
   }
   
   public LexAnalyzer(String fileName) {
       output = new ArrayList();
       setInput(fileName);
       makeAnalysis();
   }
   
   private void makeAnalysis() {
       String lexema;
       for (int i=0; i < input.length();i++) {
           
       }
   }
   
   private boolean find(String[] sourse, String sample) {
       boolean isFind = false;
       for(String atom : sourse) {
           if (sample.equals(atom)) {
               isFind = true;
           }
       }
       return isFind;
   }
}
