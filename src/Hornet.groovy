import com.tinkerpop.blueprints.pgm.Vertex
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.gremlin.Gremlin
import com.tinkerpop.gremlin.Imports
import com.tinkerpop.gremlin.console.ErrorHookClosure
import com.tinkerpop.gremlin.console.NullResultHookClosure
import com.tinkerpop.gremlin.console.PromptClosure
import com.tinkerpop.gremlin.console.ResultHookClosure
import com.tinkerpop.pipes.Pipe
import jline.History
import org.codehaus.groovy.tools.shell.Groovysh
import org.codehaus.groovy.tools.shell.IO
import org.codehaus.groovy.tools.shell.InteractiveShellRunner


//Gremlin.defineStep('codeveloper',[Vertex,Pipe], {_{x = it}.out('created').in('created'){!x.equals(it)}})
//g = TinkerGraphFactory.createTinkerGraph()
////while( g.v(1).codeveloper.hasNext() ){
//    println g.v(1).codeveloper.name().next()
////}

//println "Finished";

//Hornet defined pipes
import biopipes.*
import biopipes.utils.ExecPipe


public class Hornet {

    private static final String HISTORY_FILE = ".hornet_history";
    private static final String STANDARD_INPUT_PROMPT = "hornet> ";
    private static final String STANDARD_RESULT_PROMPT = "==>";
    private static final String STARTUP_SCREEN = "      \\ / \n--=_/( . )\\_=--";

    public Hornet(final IO io, final String inputPrompt, final String resultPrompt) {
        io.out.println();
        io.out.println(STARTUP_SCREEN);

        final Groovysh groovy = new Groovysh();
        groovy.setResultHook(new NullResultHookClosure(groovy));
        for (String imps : Imports.getImports()) {
            groovy.execute("import " + imps);
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

        Gremlin.load();
        addPipes();
        //displaySteps();    // great method, need to be able to call it in the shell

        try {
            runner.run();
        } catch (Error e) {
            //System.err.println(e.getMessage());
        }
    }

    public void displaySteps(){
       for( step in  Gremlin.steps){
           println step.toString()
       }
    }

    public void addPipes(){
        Gremlin.defineStep('exec', [Vertex,Pipe], { new ExecPipe() });
    }

    public Hornet() {
        this(new IO(System.in, System.out, System.err), STANDARD_INPUT_PROMPT, STANDARD_RESULT_PROMPT);
    }


    public static void main(final String[] args) {
        new Hornet();
    }
}