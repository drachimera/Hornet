package biopipes.utils;

import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.pipes.AbstractPipe;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.PipeClosure;
import com.tinkerpop.pipes.filter.ComparisonFilterPipe;
import com.tinkerpop.pipes.filter.ObjectFilterPipe;
import com.tinkerpop.pipes.sideeffect.AggregatorPipe;
import com.tinkerpop.pipes.sideeffect.SideEffectPipe;
import com.tinkerpop.pipes.transform.InPipe;
import com.tinkerpop.pipes.transform.OutPipe;
import com.tinkerpop.pipes.util.Pipeline;
import scala.actors.scheduler.DrainableForkJoinPool;
import util.ProcessHandler;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by IntelliJ IDEA.
 * User: qvh
 * Date: 7/21/11
 * Time: 9:25 AM
 * The exec pipe takes a string representing a command line and outputs strings representing the output from the algorithm
 */
public class ExecPipe extends AbstractPipe<String, String> {

    StringBuffer stdOut;
    StringBuffer stdErr;

    //private final PipeClosure<E, Pipe> closure;

    public ExecPipe(){
        super();
    }

    public String processNextStart() {
        try {
            if(stdOut != null && stdErr != null){
                //print any errors from the program to the screen.
                if(stdErr.length() > 0){
                    System.err.println(this.drainBuffer(stdErr));
                }
                //now, if there is anything in the output buffer, go ahead and get it and
                //pass it on to the next pipe in the system.
                if(stdOut.length() > 0){
                    String line = ProcessHandler.readline(stdOut);
                    if(line != null){ return line; }
                }
            }
            run(this.starts.next());
            if(stdOut.length() > 0){
                    String line = ProcessHandler.readline(stdOut);
                    if(line != null){ return line; }
            }else {
                return ""; //program made no output
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }

//    public ExecPipe(final PipeClosure<E, Pipe> closure) {
//        this.closure = closure;
//        this.closure.setPipe(this);
//    }
//
//    public E processNextStart() {
//        return this.closure.compute(this.starts.next());
//    }

  private void run(String command) throws IOException, InterruptedException {

        stdOut = new StringBuffer();
        stdErr = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        ProcessHandler.runCommand(command, stdOut, stdErr);
        //System.out.println(stdOut.toString());
        //System.out.println("**********************");
        //System.out.println(stdOut.indexOf("\n"));
        //String line = null;
        //int count = 0;
        //do {
        //    line = ProcessHandler.readline(stdOut);
        //    if(line != null) System.out.println(line);
        //    //count++; if(count == 11) break;
        //}while(line != null);
        ////System.out.println("");
      return;
  }

  private String drainBuffer(StringBuffer sb){
      StringBuffer out = new StringBuffer();
      String line = "";
      if(sb != null){
      do {
            line = ProcessHandler.readline(stdOut);
            if(line != null) out.append(line);
            //count++; if(count == 11) break;
        }while(line != null);
      }
      return line;
  }

//  public ExecPipe() {
//     List singleObjectList = new ArrayList() {
//            public boolean add(Object object) {
//                this.clear();
//                super.add(object);
//                return true;
//            }
//        };
//     SideEffectPipe pipe0 = new AggregatorPipe(singleObjectList);
//     Pipe pipe1 = new OutPipe("created");
//     Pipe pipe2 = new InPipe("created");
//     Pipe pipe3 = new ObjectFilterPipe(pipe0.getSideEffect(), ComparisonFilterPipe.Filter.EQUAL);
//     this.setPipes(pipe0, pipe1, pipe2, pipe3);
//   }

//Note this code is a simple system call in java based on
// http://www.devdaily.com/java/edu/pj/pj010016
//It could be made more functional (and perhaps more confusing) by following
// http://www.devdaily.com/java/java-exec-processbuilder-process-1
//  protected String processNextStart() {
//       System.out.println("Running Command: " + this.starts.next().toString());
//       Process p;
//      try {
//          p = Runtime.getRuntime().exec("ls");
//          BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
//          BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//          String s = null;
//          while ((s = stdInput.readLine()) != null) { System.out.println(s); }
//      } catch (IOException e) {
//          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//      }
//        return this.starts.next();
//  }

}
