/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject2;

/**
 *
 * @author refreshjss
 */
public class PossibleOperation {

    private String operation;
    private String typeOfOperand1;
    private String typeOfOperand2;
    private String returnType;

    PossibleOperation(String operation, String typeOfOperand, String returnType) {
        this.operation = operation;
        this.typeOfOperand1 = typeOfOperand;
        this.returnType = returnType;
        this.typeOfOperand2 = "";
    }

    PossibleOperation(String operation, String typeOfOperand1, String typeOfOperand2, String returnType) {
        this.operation = operation;
        this.typeOfOperand1 = typeOfOperand1;
        this.returnType = returnType;
        this.typeOfOperand2 = typeOfOperand2;
    }

    public String getReturnType() {
        return this.returnType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PossibleOperation)) {
            return false;
        }
        PossibleOperation c = (PossibleOperation) o;
        boolean eq = false;
        if (this.operation.equals(c.operation)
                && ((this.typeOfOperand1.equals(c.typeOfOperand1) && this.typeOfOperand2.equals(c.typeOfOperand2))
                || (this.typeOfOperand1.equals(c.typeOfOperand2) && this.typeOfOperand2.equals(c.typeOfOperand1)))) {
            eq = true;
        }
        return eq;
    }
}
