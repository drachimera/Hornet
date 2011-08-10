package loaders;

import com.tinkerpop.pipes.AbstractPipe;
import edu.upc.dama.dex.core.Graph;
import loaders.GraphLoader;
import org.apache.commons.digester.plugins.strategies.LoaderSetProperties;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.compound.AminoAcidCompound;
import org.biojava3.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava3.core.sequence.io.FastaReader;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.GenericFastaHeaderParser;
import org.biojava3.core.sequence.io.ProteinSequenceCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import util.md5util;

/**
 * @author Daniel Quest
 * Date: 7/21/11
 * Time: 1:17 PM
 * This filer takes the name of a fasta file (in the future hopefully also network sockets streaming fasta records)
 * and it transforms them into blueprints verticies that it emits
 *
 * each vertex looks like this:
 * hornet:fastaRecord12345 a hornet:fastaRecord
 * hornet:fastaRecord12345 hornet:hasSequence """ATACCAGCCAGC"""^^String
 * hornet:fastaRecord12345 hornet:hasHeader """Some Sequence"""
 */
public class fasta2blueprints implements GraphLoader {

	public static void main(String[] args) throws Exception{

        String filepath = "/workspace/Hornet/Data/EcoliPlasmids.fa";
		/*
		 * Method 1: With the FastaReaderHelper
		 */
		//Try with the FastaReaderHelper
		LinkedHashMap<String, ProteinSequence> a = FastaReaderHelper.readFastaProteinSequence(new File(filepath));
		//FastaReaderHelper.readFastaDNASequence for DNA sequences

		for (  Entry<String, ProteinSequence> entry : a.entrySet() ) {
			System.out.println( entry.getValue().getOriginalHeader() + "=" + entry.getValue().getSequenceAsString() );
		}


	}

    public com.tinkerpop.blueprints.pgm.Graph FastaReader(com.tinkerpop.blueprints.pgm.Graph g, String filepath) throws Exception {
        //First add a vertex to the graph representing the Fasta file.
        com.tinkerpop.blueprints.pgm.Vertex v =  g.addVertex(filepath);
        v.setProperty("hasFile",filepath);
        v.setProperty("hasFormat", "fasta");
        /*
		 * BioJavaMethod 2: With the FastaReader Object
		 */
		//Try reading with the FastaReader
		FileInputStream inStream = new FileInputStream( filepath );
		FastaReader<ProteinSequence,AminoAcidCompound> fastaReader =
			new FastaReader<ProteinSequence,AminoAcidCompound>(
					inStream,
					new GenericFastaHeaderParser<ProteinSequence,AminoAcidCompound>(),
					new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet()));
		LinkedHashMap<String, ProteinSequence> b = fastaReader.process();
		for (  Entry<String, ProteinSequence> entry : b.entrySet() ) {
			//System.out.println( entry.getValue().getOriginalHeader() + "=" + entry.getValue().getSequenceAsString() );
            String md5 = new md5util().getmd5(entry.getValue().getSequenceAsString());
            com.tinkerpop.blueprints.pgm.Vertex sequence =  g.addVertex( md5 );
            sequence.setProperty("header",entry.getValue().getOriginalHeader());
            sequence.setProperty("rawsequence",entry.getValue().getSequenceAsString());
		}
        return g;
    }

    public com.tinkerpop.blueprints.pgm.Graph load(com.tinkerpop.blueprints.pgm.Graph g, String filename, String format) {
        try {
            g = FastaReader(g, filename);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return g;
    }
}