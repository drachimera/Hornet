package hornet

import com.tinkerpop.gremlin.Gremlin
import com.tinkerpop.gremlin.jsr223.GremlinScriptEngine
import com.tinkerpop.gremlin.pipes.GremlinPipeline
import com.tinkerpop.gremlin.pipes.util.GroovyPipeClosure
import com.tinkerpop.pipes.Pipe
import com.tinkerpop.pipes.filter.ComparisonFilterPipe.Filter
import com.tinkerpop.pipes.filter.FilterClosurePipe
import javax.script.SimpleBindings
import com.tinkerpop.gremlin.loaders.*
import hornet.console.HornetScriptEngine
import com.tinkerpop.blueprints.pgm.Vertex
import biopipes.utils.ExecPipe
import biopipes.utils.formats.FastaFilter

/**
 * Created by IntelliJ IDEA.
 * User: qvh
 * Date: 7/28/11
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
class Hornet extends Gremlin {
    public static final Set<String> steps = new HashSet<String>();
    public static final HornetScriptEngine engine = new HornetScriptEngine();
    public static void hornet_load() {
        load();
        Hornet.defineStep('exec', [Vertex,Pipe], { new ExecPipe() });
        Hornet.defineStep('fasta', [Vertex,Pipe], { new FastaFilter() });
    }
}