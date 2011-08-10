package loaders.Jena;

/** This class is not yet functioning
 *  when it is, it will open an owl file (or RDF or turtle or whatever) and load the information
 *  in that file into a blueprints graph
 */

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.Command;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;
import loaders.GraphLoader;
import org.apache.velocity.runtime.directive.Foreach;
import sun.security.provider.certpath.Vertex;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author Daniel Quest
 * Date: 7/21/11
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class Jena2Blueprints implements GraphLoader {

    /**
     *
     * @param g         e.g. GraphSail
     * @param file      e.g. ont/hornet.owl
     * @param format    e.g. OWL RDF/XML n3
     * @return
     */
    public Graph load(Graph g, String file, String format){
        //Load the model from the file.
        OntModel m = this.loadSemantic(file, format);
        //this.printModel(m);
        g = this.push(g, m);
        return g;
    }

    public static void main(String[] args) {
        System.out.println("Startup!");
        Graph graph = new TinkerGraph();
        String inputFilePath = "/workspace/Hornet/ont/hornet.owl";     //change your relative path to run this...
        String inputFileFormat = "OWL";
        Jena2Blueprints j2bp = new  Jena2Blueprints();
        Graph g = j2bp.load(graph, inputFilePath, inputFileFormat);
        Iterator i = g.getEdges().iterator();
        while(i.hasNext()){ System.err.println(i.next());}
        g.shutdown();

        System.out.println("Finished!");
    }


    /**
     * Load the SW model from a flat file into memory
     */
    public OntModel loadSemantic(String fileNamePath, String format){
        try {
            FileInputStream instream = null;
                instream = new FileInputStream(fileNamePath);
            OntModel model = ModelFactory.createOntologyModel();
            model.read(instream, null);
            instream.close();
            return model;
        } catch (FileNotFoundException ex) {
                Logger.getLogger(Jena2Blueprints.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Jena2Blueprints.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * push takes a graph, g, and a model, m, and forces every statement from the model into the graph
     * --
     * currently this is pretty basic
     */
     public Graph push(Graph g, Model model){
        // list the statements in the Model
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement s = iter.nextStatement();  // get next statement
            //printStatement(s);
            Resource  subject   = s.getSubject();     // get the subject
            Property  predicate = s.getPredicate();   // get the predicate
            RDFNode   object    = s.getObject();      // get the object
            com.tinkerpop.blueprints.pgm.Vertex v1 = this.addVertex(g,subject);
            //The object is (1) an annotation, (2) a literal or (3) a resource
            if(object.isAnon()){
                //System.out.println("Annon: " + object.toString());
                v1.setProperty(predicate.toString(), object.toString());
            }else if(object.isLiteral()){
                //System.out.println("Literal " + object.toString());
                v1.setProperty(predicate.toString(), object.toString());
            }else if(object.isResource()){
                //System.out.println("Resource " + object.toString());
                com.tinkerpop.blueprints.pgm.Vertex v2 = this.addVertex(g,subject);
                g.addEdge(null, v1, v2, predicate.toString());
            }else  {
                 printStatement(s);
            }
        }

        return g;
     }

    public com.tinkerpop.blueprints.pgm.Vertex addVertex(Graph g, Resource subject){
            com.tinkerpop.blueprints.pgm.Vertex v = null;
            if ( g.getVertex(subject.toString()) == null){
                //System.err.println(subject.toString());
                v = g.addVertex(subject.toString());
            }else {
                v = g.getVertex(subject.toString());
            }

        return v;
    }
//    public void push(Model m) throws SailException{
////        Neo4jGraph neo = new Neo4jGraph("jena/jena4neo");
////        Sail sail = new GraphSail(neo);
////
////    	CommitManager manager = TransactionalGraphHelper.createCommitManager(neo, 10000);
////        SailConnection sc = sail.getConnection();
////        ValueFactory vf = sail.getValueFactory();
//
//
//        StmtIterator iter = m.listStatements();
//        while(iter.hasNext()){
//            //Jena statement
//            Statement s = iter.nextStatement();
//            Resource  subject   = s.getSubject();     // get the subject
//            Property  predicate = s.getPredicate();   // get the predicate
//            RDFNode   object    = s.getObject();      // get the object
//            //printStatement(s);
//
//            //if(object.toString().startsWith("\"\"\"") == true){
//            if(predicate.toString().equalsIgnoreCase("http://www.compbio.ornl.gov/ontology/scenario.owl#hasSequence")){
//                if(object.toString().startsWith("http://www.biosites.org/Scenario1003.n3#Sequence1003_")){
//                //if(object.toString().matches("[ATGCatgc]*")){
//                    String type = "<http://www.w3.org/2001/XMLSchema#string>";
//                    String fixed = object.toString();
//
//                    fixed = fixed.replaceAll("\n", " ");
//                    fixed = fixed.replaceAll("\"", " ");
//                    fixed = fixed.replaceAll("  ", " ");
//                    fixed = fixed.trim();
////                              System.out.println("<" + subject.toString() + "> " +
////                                   "<" + predicate.toString() + "> " +
////                                   "\"" + fixed + "\"^^"+ type +" .");
//                }else{
////                    System.err.println("<" + subject.toString() + "> " +
////                                       "<" + predicate.toString() + "> " +
////                                       "<" + object.toString() + "> .");
//                }
//            }else if(!object.toString().startsWith("http://")){
//                String type = "<http://www.w3.org/2001/XMLSchema#string>";
//                String fixed = object.toString();
//
//                fixed = fixed.replaceAll("\n", " ");
//                fixed = fixed.replaceAll("\"", " ");
//                fixed = fixed.replaceAll("  ", " ");
//                fixed = fixed.trim();
//                if (fixed.matches("^[0123456789]*$")){
//                    type = "<http://www.w3.org/2001/XMLSchema#int>";
//                }else if(fixed.contains("http://www.w3.org/2001/XMLSchema#date")){
//                    fixed = fixed.replaceAll("http://www.w3.org/2001/XMLSchema#date", "");
//                    fixed = fixed.replaceAll("\\^", "");
//                    fixed = fixed.replaceAll(" ", "");
//                    type = "<http://www.w3.org/2001/XMLSchema#date>";
//                }else {
//                    fixed = fixed.replaceAll("http://www.w3.org/2001/XMLSchema#string", "");
//                    fixed = fixed.replaceAll("\\^", "");
//                }
//                                System.out.println("<" + subject.toString() + "> " +
//                                   "<" + predicate.toString() + "> " +
//                                   "\"" + fixed + "\"^^"+ type+" .");
//
//            }else {
////                System.out.println("<" + subject.toString() + "> " +
////                                   "<" + predicate.toString() + "> " +
////                                   "<" + object.toString() + "> .");
//            }
//
//            //Sail Statement
////            sc.addStatement(vf.createURI(subject.toString()), vf.createURI(predicate.toString()), vf.createURI(object.toString()), vf.createURI(ontologyURI));
//
//
//
//        }
////        manager.close();
////        sail.shutDown();
////        neo.shutdown();
//    }


    /**
     * input, model and subject
     * output, a label for that subject.  If there are more than one labels,
     * it will return one of them. (no guarentee which)
     * Some terms have synonyms.  That is not considered here.
     */
    private String rdfLabel = "http://www.w3.org/2000/01/rdf-schema#label";
    public String getLabel(Model m, Resource subject){
        String label = "unknown";
        Property p = m.getProperty(rdfLabel);
        StmtIterator iter = m.listStatements(subject, p, (RDFNode) null);
        while(iter.hasNext()){
            Statement s = iter.nextStatement();
            RDFNode l = s.getObject();
            //System.out.println(l.asLiteral().getLexicalForm());
            //label = l.asLiteral().getLexicalForm();    //again this does not work ???!?!?!?
            label = l.toString();
        }
        return label;
    }

    /** Render a URI */
    protected void renderURI( PrintStream out, PrefixMapping prefixes, String uri ) {
        out.print( prefixes.shortForm( uri ) );
    }


    public void printStatement(Statement stmt){
            Resource  subject   = stmt.getSubject();     // get the subject
            Property  predicate = stmt.getPredicate();   // get the predicate
            RDFNode   object    = stmt.getObject();      // get the object
            printStatement(subject, predicate, object);
    }

    public void printStatement(Resource subject, Property predicate, RDFNode object){
            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else if (object.isLiteral()){
                //System.out.print(object.asLiteral());    //some reason this is not compiling????!!??!?!
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
    }

    public void printModel(Model model){
        // list the statements in the Model
        StmtIterator iter = model.listStatements();

        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();  // get next statement
            printStatement(stmt);
        }

        return;
    }

}
