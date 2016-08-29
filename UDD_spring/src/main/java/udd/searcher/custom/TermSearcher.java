package udd.searcher.custom;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;

import udd.searcher.ResultRetriever;

public class TermSearcher {
	protected File indexDirPath;
	
	public TermSearcher(){
		this(new File(ResourceBundle.getBundle("index").getString("index")));
	}
	
	public TermSearcher(String path){
		this(new File(path));
	}
	
	public TermSearcher(File indexDirPath){
		this.indexDirPath = indexDirPath;
	}
	
	public List<Document> search(Term term){
		TermQuery q = new TermQuery(term);
		List<Document> docs = ResultRetriever.getResults(q);
		return docs;
	}

}
