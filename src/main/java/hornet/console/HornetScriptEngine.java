package hornet.console;

import com.tinkerpop.gremlin.Gremlin;
import com.tinkerpop.gremlin.jsr223.GremlinScriptEngine;
import hornet.Hornet;

/**
 * Created by IntelliJ IDEA.
 * User: qvh
 * Date: 7/25/11
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class HornetScriptEngine  extends GremlinScriptEngine {
        private String imports;
        public HornetScriptEngine() {
            super();
            Hornet.hornet_load();//Gremlin.load();
            StringBuilder sb = new StringBuilder();
            for (String imp : HornetImports.getImports()) {
                sb.append("import ").append(imp).append("\n");
            }
            this.imports = sb.toString();

        }
}
