package util.glue;
//based on: import com.tinkerpop.gremlin.jsr223.ScriptExecutor;

import com.tinkerpop.gremlin.jsr223.GremlinScriptEngine;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import javax.script.Bindings;
import java.io.Reader;
import java.util.List;

/**
 *
 * User: qvh
 * Date: 7/22/11
 * Time: 10:18 AM
 * This just loads a bit of added funtionality and redirects to gremlin
 */
public class gremlinScriptExeRedirect {

    public static void main(final String[] arguments) throws IOException {
        if (arguments.length == 0) {
            System.out.println("Usage: <path_to_hornet_script> <argument a1> <argument a2> ...");
        } else {
            evaluate(new FileReader(arguments[0]), Arrays.asList(arguments).subList(1, arguments.length));
        }
    }

    protected static void evaluate(final Reader reader, final List<String> arguments) {
        final GremlinScriptEngine engine = new GremlinScriptEngine();

        final Bindings bindings = engine.createBindings();
        if (arguments.size() > 0) {
            for (int i = 0; i < arguments.size(); i++) {
                bindings.put("a" + (i + 1), arguments.get(i));
            }
        }
        try {
            engine.eval(reader, bindings);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

}