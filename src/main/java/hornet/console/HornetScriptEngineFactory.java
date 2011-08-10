package hornet.console;

import com.tinkerpop.gremlin.jsr223.GremlinScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.Arrays;
import java.util.List;
import hornet.console.HornetScriptEngine;

/**
 * User: qvh
 * Date: 7/25/11
 * Time: 3:19 PM
based on GremlinScriptEngineFactory
 */
public class HornetScriptEngineFactory extends GremlinScriptEngineFactory implements ScriptEngineFactory {
    private static final String ENGINE_NAME = "hornet";
    private static final String LANGUAGE_NAME = "hornet";
    private static final String VERSION_NUMBER = "0.1";
    private static final String PLAIN = "plain";
    private static final List<String> EXTENSIONS = Arrays.asList("hrnt", "hornet", "hrt");

    public ScriptEngine getScriptEngine() {
        return new HornetScriptEngine();
    }
}