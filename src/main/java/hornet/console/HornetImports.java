package hornet.console;

import com.tinkerpop.gremlin.Imports;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * @author Daniel Quest
 */
public class HornetImports {
    // Imports gremlinImports = new Imports();
    // gremlin does for everything it needs to import automatically
    // imports.add("com.tinkerpop.gremlin.*");

    private static final List<String> hornetimports = new ArrayList<String>();
    static {
        // hornet only imports
        hornetimports.add("hornet.Hornet");

        //loaders
        hornetimports.add("loaders.Jena.Jena2Blueprints");
        hornetimports.add("loaders.examp.*");
        hornetimports.add("util.*");
        hornetimports.add("biopipes.utils.*");
    }

    public static List<String> getImports() {
        List<String> imports = new ArrayList<String>();
        imports.addAll(hornetimports);
        //System.err.println("gremlin imports:");
        //Iterator itter = Imports.getImports().iterator();
        //while(itter.hasNext()){
        //      System.err.println(itter.next());
        //}
        imports.addAll(Imports.getImports());  //and get all the Gremlin specific imports
        return imports;
    }

}
