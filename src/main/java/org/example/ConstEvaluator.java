package org.example;

import org.example.parser.ConfigLangBaseVisitor;
import org.example.parser.ConfigLangParser;

public class ConstEvaluator extends ConfigLangBaseVisitor<Long> {

    private final ConstEnvironment env;

    public ConstEvaluator(ConstEnvironment env) {
        this.env = env;
    }

    @Override
    public Long visitAddConstNumber(ConfigLangParser.AddConstNumberContext ctx) {
        String name = ctx.IDENT().getText();
        long base = env.get(name);
        long number = parseNumber(ctx.NUMBER().getText());
        return base + number;
    }

    @Override
    public Long visitAddConstConst(ConfigLangParser.AddConstConstContext ctx) {
        long a = env.get(ctx.IDENT(0).getText());
        long b = env.get(ctx.IDENT(1).getText());
        return a + b;
    }

    @Override
    public Long visitSqrtConst(ConfigLangParser.SqrtConstContext ctx) {
        long value = env.get(ctx.IDENT().getText());
        if (value < 0) {
            throw new IllegalArgumentException("sqrt of negative constant: " + value);
        }
        return (long) Math.sqrt(value);
    }

    public static long parseNumber(String text) {
        // text format: 0b101 or 0B101
        String bits = text.substring(2);
        return Long.parseLong(bits, 2);
    }
}


