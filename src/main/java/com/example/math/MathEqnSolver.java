package com.example.math;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MathEqnSolver {
    public static void main(String[] args) {

        System.out.println(solveEquation("( 4 + 5 ) * 6"));
        System.out.println(solveEquation("( 5 * 3 + ( 3 - 2 ) ) + 2"));
        System.out.println(solveEquation("5"));
        System.out.println(solveEquation("( 5 * ( 3 + ( 3 - 2 ) ) + 1 )"));
        System.out.println(solveEquation("( 6 / 3 ) + ( 3 * 7 - 1 )"));

    }

    public static int solveEquation(String equation) {

        if(!equation.contains("(")) {
            List<String> eqtnList = new LinkedList<>(Arrays.asList(equation.split(" ")));
            solveSubEqtn("*", eqtnList);
            solveSubEqtn("/", eqtnList);
            solveSubEqtn("+", eqtnList);
            solveSubEqtn("-", eqtnList);
            return Integer.valueOf(eqtnList.get(0));
        }

        String sub = equation.substring(equation.lastIndexOf("(") + 2);
        String sub2 = sub.substring(0, sub.indexOf(")") - 1);
        String allBefore = equation.substring(0, equation.lastIndexOf("("));
        String allAfter = sub.substring(sub.indexOf(")"));

        while(!allBefore.isEmpty() || !allAfter.isEmpty()) {
            if (!allAfter.isEmpty()) {
                allAfter = allAfter.substring(1);
            }
            return solveEquation(allBefore + solveEquation(sub2) + allAfter);
        }
        return 0;
    }

    public static void solveSubEqtn(String operator, List<String> eqtnList) {
        int operatorIndex;
        while (eqtnList.contains(operator)) {
            operatorIndex = eqtnList.indexOf(operator);
            String solution = "";
            if(operator.equals("*")) {
                solution = String.valueOf(Integer.valueOf(eqtnList.get(operatorIndex - 1)) * Integer.valueOf(eqtnList.get(operatorIndex + 1)));
            } else if (operator.equals("/")) {
                solution = String.valueOf(Integer.valueOf(eqtnList.get(operatorIndex - 1)) / Integer.valueOf(eqtnList.get(operatorIndex + 1)));
            } else if (operator.equals("+")) {
                solution = String.valueOf(Integer.valueOf(eqtnList.get(operatorIndex - 1)) + Integer.valueOf(eqtnList.get(operatorIndex + 1)));
            } else if (operator.equals("-")) {
                solution = String.valueOf(Integer.valueOf(eqtnList.get(operatorIndex - 1)) - Integer.valueOf(eqtnList.get(operatorIndex + 1)));
            }
            eqtnList.set(0, solution);
            eqtnList.remove(operatorIndex + 1);
            eqtnList.remove(operatorIndex);
        }

    }
}
