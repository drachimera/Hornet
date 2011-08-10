package hornet.console;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Pavel A. Yaskevich
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class HornetScriptExecutor extends com.tinkerpop.gremlin.jsr223.ScriptExecutor {

    public static void main(final String[] arguments) throws IOException {
        if (arguments.length == 0) {
            System.out.println("Usage: <path_to_hornet_script> <argument a1> <argument a2> ...");
        } else {
            evaluate(new FileReader(arguments[0]), Arrays.asList(arguments).subList(1, arguments.length));
        }
    }
}


