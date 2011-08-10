package hornet.console;

import util.ProcessHandler;
import biopipes.utils.CoDeveloperPipe;
import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory;
import com.tinkerpop.gremlin.Gremlin;
import com.tinkerpop.gremlin.Imports;
import com.tinkerpop.gremlin.console.*;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.transform.OutPipe;
import com.tinkerpop.pipes.transform.PropertyPipe;
import com.tinkerpop.pipes.util.Pipeline;
import com.tinkerpop.pipes.util.SingleIterator;
import groovy.lang.Closure;
import jline.History;
import org.codehaus.groovy.ant.Groovy;
import org.codehaus.groovy.tools.shell.Command;
import org.codehaus.groovy.tools.shell.Groovysh;
import org.codehaus.groovy.tools.shell.IO;
import org.codehaus.groovy.tools.shell.InteractiveShellRunner;
import hornet.*;


//Gremlin.defineStep('codeveloper',[Vertex,Pipe], {_{x = it}.out('created').in('created'){!x.equals(it)}})
//g = TinkerGraphFactory.createTinkerGraph()
////while( g.v(1).codeveloper.hasNext() ){
//    println g.v(1).codeveloper.name().next()
////}

//println "Finished";

//Hornet defined pipes
import biopipes.*;
import biopipes.utils.ExecPipe;
import scala.reflect.This;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.acl.Owner;
import java.util.*;


public class HConsole {

    private static final String HISTORY_FILE = ".hornet_history";
    private static final String STANDARD_INPUT_PROMPT = "hornet> ";
    private static final String STANDARD_RESULT_PROMPT = "==>";
    private static final String STARTUP_SCREEN = "      \\ / \n--=_/( . )\\_=--";
    private static final String HELP_SCREEN = "type: help for groovy help; Hornet.getStepNames() for pipeline list";

    public HConsole(final IO io, final String inputPrompt, final String resultPrompt) {
        io.out.println();
        io.out.println(HELP_SCREEN);
        io.out.println(STARTUP_SCREEN);

        final Groovysh groovy = new Groovysh();
        groovy.setResultHook(new NullResultHookClosure(groovy));
        for (String imps : HornetImports.getImports()) {
            groovy.execute("import " + imps);
            //System.err.println("importing: " + imps);
        }
        groovy.setResultHook(new ResultHookClosure(groovy, io, resultPrompt));
        groovy.setHistory(new History());

        final InteractiveShellRunner runner = new InteractiveShellRunner(groovy, new PromptClosure(groovy, inputPrompt));
        runner.setErrorHandler(new ErrorHookClosure(runner, io));
        try {
            runner.setHistory(new History(new File(System.getProperty("user.home") + "/" + HISTORY_FILE)));
        } catch (IOException e) {
            io.err.println("Unable to create history file: " + HISTORY_FILE);
        }

        Hornet.hornet_load();
        //Gremlin.defineStep("exec",new ArrayList<Class>(), {new ExecPipe()} );
        //HornetImports.addPipes();
        //displaySteps();    // great method, need to be able to call it in the shell

        try {
            runner.run();
        } catch (Error e) {
            //System.err.println(e.getMessage());
        }
    }

//    private class COMMANDS extends Closure {
//        Object owner;
//        public COMMANDS(Object owner) {
//            super(owner);
//            this.owner = owner;
//            doCall();
//        }
//        public void doCall(){
//            ((HConsole)this.owner).displaySteps();
//        }
//    }

    public void displaySteps(){
        Set steps = Hornet.getStepNames();
        Iterator i = steps.iterator();
        while(i.hasNext()){
                System.out.println(i.next());
        }
    }



    public HConsole() {
        this(new IO(System.in, System.out, System.err), STANDARD_INPUT_PROMPT, STANDARD_RESULT_PROMPT);
    }



    public static void main(final String[] args) {

        new HConsole();



//        Graph g = TinkerGraphFactory.createTinkerGraph();
//        Pipe co = new CoDeveloperPipe();
//
//        Vertex v = g.getVertex(new Integer("1"));
//        LinkedList ll = new LinkedList(); ll.add(v);
//        co.setStarts(ll.descendingIterator());
//        while (co.hasNext()){
//            System.out.println(co.next());
//        }

    }
}