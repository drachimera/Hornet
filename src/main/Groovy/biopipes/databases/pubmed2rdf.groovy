package biopipes

import com.tinkerpop.pipes.AbstractPipe
import com.tinkerpop.blueprints.pgm.Edge
import com.tinkerpop.blueprints.pgm.Graph

public class PubmedPipe<S> extends AbstractPipe<S, S> {

    //def eUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=11877539,11822933,11871444&retmode=xml"
    protected S processNextStart() {
        return fetch(this.starts.next());

    }
    private String fetch(String id){
        def eUrl = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id="+ id +"&retmode=xml"
        def doc = new XmlSlurper().parse(new URL(eUrl).openStream());

        for (article in doc.PubmedArticle) {
            println "pmid: ${article.MedlineCitation.PMID.text()} title: ${article.MedlineCitation.Article.ArticleTitle.text()} \n"
            return "pmid: " + article.MedlineCitation.PMID.text();
        };

    }

}

//println "PubmedPipe Loaded."

//PubmedPipe e = new PubmedPipe();
//e.fetch();

