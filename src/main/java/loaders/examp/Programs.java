package loaders.examp;

import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

/**
 * Created by IntelliJ IDEA.
 * User: qvh
 * Date: 8/8/11
 * Time: 1:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Programs {
   public static TinkerGraph createGraph() {

        TinkerGraph graph = new TinkerGraph();

        Vertex all = graph.addVertex("1");
        all.setProperty("name", "all");

        Vertex ls = graph.addVertex("2");
        ls.setProperty("name", "ls");

        Vertex pwd = graph.addVertex("3");
        pwd.setProperty("name", "pwd");

        Vertex cat = graph.addVertex("4");
        cat.setProperty("name", "cat");

        graph.addEdge("7", ls, all, "isa");
        graph.addEdge("8", pwd, all, "isa");
        graph.addEdge("9", cat, all, "isa");

        return graph;

    }


}
