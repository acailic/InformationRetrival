package udd.searcher.custom;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;

import udd.searcher.ResultRetriever;

public class PrefixSearcher {
	protected File indexDirPath;
	
	public PrefixSearcher(){
		this(new File(ResourceBundle.getBundle("index").getString("index")));
	}

	public PrefixSearcher(String path){
		this(new File(path));
	}
	
	public PrefixSearcher(File indexDirPath){
		this.indexDirPath = indexDirPath;
	}
	
	public List<Document> search(Term prefix){
		PrefixQuery query = new PrefixQuery(prefix);
		return ResultRetriever.getResults(query, indexDirPath, true);
	}

}
