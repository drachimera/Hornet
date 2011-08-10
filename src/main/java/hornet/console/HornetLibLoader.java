package hornet.console;

import biopipes.utils.ExecPipe;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.Gremlin;
import com.tinkerpop.pipes.Pipe;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Daniel Quest
 * Date: 7/25/11
 * Time: 9:37 AM
 *
 * HornetLibLoader is a centeral place to keep the Hornet only based pipes and other functionality.
 * Use this class to extend any class with Hornet capabilities.
 */
public class HornetLibLoader {

        public void displaySteps(){
//            for( step  Gremlin.steps){
//                System.out.println(step.toString());
//            }
        }

        public void addPipes(){
           List<Class> l = new LinkedList<Class>();

        }
}
