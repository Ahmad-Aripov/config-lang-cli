package org.example;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.parser.ConfigLangLexer;
import org.example.parser.ConfigLangParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigLangTest {

    private String parseToXml(String text) {
        CharStream input = CharStreams.fromString(text);
        ConfigLangLexer lexer = new ConfigLangLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowingErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ConfigLangParser parser = new ConfigLangParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener());
        ParseTree tree = parser.file();
        XmlGenerator generator = new XmlGenerator();
        return generator.generate(tree);
    }

    @Test
    void parsesConstAndExprAndDict() {
        String src = """
                // network config
                var base 0b0010
                ^(+ base 0b0011)
                [ host => 0b0001, port => 0b1010 ]
                """;

        String xml = parseToXml(src);

        assertTrue(xml.contains("<const name=\"base\" value=\"2\"/>"));
        assertTrue(xml.contains("<constExpr"));
        assertTrue(xml.contains("<dict>"));
        assertTrue(xml.contains("<entry name=\"host\"><number>1</number></entry>"));
        assertTrue(xml.contains("<entry name=\"port\"><number>10</number></entry>"));
    }

    @Test
    void detectsSyntaxError() {
        String src = "var 123bad 0b01";
        assertThrows(SyntaxException.class, () -> parseToXml(src));
    }
}


