package loaders;

import com.tinkerpop.blueprints.pgm.Graph;

/**
 * Created by IntelliJ IDEA.
 * User: qvh
 * Date: 7/21/11
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GraphLoader {
    /**
     * Populates g with data from filename in a specific format
     * @param g         e.g. GraphSail
     * @param filename  e.g foo.owl
     * @param format    e.g. OWL
     * @return
     */
    public Graph load(Graph g, String filename, String format);
}
