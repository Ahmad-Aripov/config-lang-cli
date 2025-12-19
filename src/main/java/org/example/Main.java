package org.example;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.example.parser.ConfigLangLexer;
import org.example.parser.ConfigLangParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2 || !"-i".equals(args[0])) {
            System.err.println("Usage: java -jar config-lang-cli.jar -i <input-file>");
            System.exit(1);
        }

        Path inputPath = Path.of(args[1]);
        if (!Files.exists(inputPath)) {
            System.err.println("Input file does not exist: " + inputPath);
            System.exit(1);
        }

        try {
            CharStream input = CharStreams.fromPath(inputPath);
            ConfigLangLexer lexer = new ConfigLangLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(new ThrowingErrorListener());
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ConfigLangParser parser = new ConfigLangParser(tokens);

            parser.removeErrorListeners();
            parser.addErrorListener(new ThrowingErrorListener());

            ParseTree tree = parser.file();

            XmlGenerator generator = new XmlGenerator();
            String xml = generator.generate(tree);
            System.out.println(xml);
        } catch (SyntaxException e) {
            System.err.println("Syntax error: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            System.exit(3);
        }
    }
}
