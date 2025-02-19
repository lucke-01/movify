package com.ricardocreates.movify.testing;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class Test {
    public static void main(String[] args) {
        System.out.println("adsf" + null + null);
        String ob = null;
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello World'+'asdf'");
        String message = (String) exp.getValue();
        System.out.println(message);
    }
}
