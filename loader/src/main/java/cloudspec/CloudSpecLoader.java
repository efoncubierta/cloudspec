package cloudspec;

import cloudspec.core.spec.CloudSpec;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.io.InputStream;

public class CloudSpecLoader {
    public CloudSpec load(InputStream is) throws IOException {
        CloudSpecLexer lexer = new CloudSpecLexer(new ANTLRInputStream(is));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CloudSpecParser parser = new CloudSpecParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.spec();

        ParseTreeWalker walker = new ParseTreeWalker();
        CloudSpecRealListener listener = new CloudSpecRealListener();
        walker.walk(listener, tree);

        return listener.getCloudSpec();
    }
}
