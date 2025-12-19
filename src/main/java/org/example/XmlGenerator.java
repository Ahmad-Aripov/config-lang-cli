package org.example;

import org.antlr.v4.runtime.tree.ParseTree;
import org.example.parser.ConfigLangBaseVisitor;
import org.example.parser.ConfigLangParser;

public class XmlGenerator extends ConfigLangBaseVisitor<Void> {

    private final StringBuilder sb = new StringBuilder();
    private final ConstEnvironment env = new ConstEnvironment();
    private final ConstEvaluator evaluator = new ConstEvaluator(env);

    public String generate(ParseTree tree) {
        sb.setLength(0);
        sb.append("<config>");
        visit(tree);
        sb.append("</config>");
        return sb.toString();
    }

    @Override
    public Void visitFile(ConfigLangParser.FileContext ctx) {
        for (ConfigLangParser.StatementContext st : ctx.statement()) {
            visit(st);
        }
        return null;
    }

    @Override
    public Void visitConstDecl(ConfigLangParser.ConstDeclContext ctx) {
        String name = ctx.IDENT().getText();

        if (ctx.value().NUMBER() == null) {
            throw new IllegalArgumentException("Only numeric constants are supported in const declarations: " + name);
        }

        long value = ConstEvaluator.parseNumber(ctx.value().NUMBER().getText());
        env.define(name, value);

        sb.append("<const name=\"")
                .append(escape(name))
                .append("\" value=\"")
                .append(value)
                .append("\"/>");
        return null;
    }

    @Override
    public Void visitConstExpr(ConfigLangParser.ConstExprContext ctx) {
        long result = evaluator.visit(ctx.expr());

        sb.append("<constExpr result=\"")
                .append(result)
                .append("\">");
        appendExprXml(ctx.expr(), result);
        sb.append("</constExpr>");
        return null;
    }

    @Override
    public Void visitDict(ConfigLangParser.DictContext ctx) {
        sb.append("<dict>");
        if (ctx.pairList() != null) {
            for (ConfigLangParser.PairContext pair : ctx.pairList().pair()) {
                String key = pair.IDENT().getText();
                sb.append("<entry name=\"").append(escape(key)).append("\">");
                appendValue(pair.value());
                sb.append("</entry>");
            }
        }
        sb.append("</dict>");
        return null;
    }

    private void appendExprXml(ConfigLangParser.ExprContext expr, long result) {
        if (expr instanceof ConfigLangParser.AddConstNumberContext addNum) {
            String name = addNum.IDENT().getText();
            long base = env.get(name);
            long number = ConstEvaluator.parseNumber(addNum.NUMBER().getText());
            sb.append("<add baseConst=\"")
                    .append(escape(name))
                    .append("\" baseValue=\"")
                    .append(base)
                    .append("\" addend=\"")
                    .append(number)
                    .append("\"/>");
        } else if (expr instanceof ConfigLangParser.AddConstConstContext addConst) {
            String a = addConst.IDENT(0).getText();
            String b = addConst.IDENT(1).getText();
            long av = env.get(a);
            long bv = env.get(b);
            sb.append("<add constA=\"")
                    .append(escape(a))
                    .append("\" valueA=\"")
                    .append(av)
                    .append("\" constB=\"")
                    .append(escape(b))
                    .append("\" valueB=\"")
                    .append(bv)
                    .append("\"/>");
        } else if (expr instanceof ConfigLangParser.SqrtConstContext sqrt) {
            String name = sqrt.IDENT().getText();
            long v = env.get(name);
            sb.append("<sqrt const=\"")
                    .append(escape(name))
                    .append("\" value=\"")
                    .append(v)
                    .append("\"/>");
        }
    }

    private void appendValue(ConfigLangParser.ValueContext value) {
        if (value.NUMBER() != null) {
            long v = ConstEvaluator.parseNumber(value.NUMBER().getText());
            sb.append("<number>").append(v).append("</number>");
        } else if (value.dict() != null) {
            visitDict(value.dict());
        }
    }

    private static String escape(String s) {
        return s
                .replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}


