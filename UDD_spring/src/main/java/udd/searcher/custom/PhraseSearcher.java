package udd.searcher.custom;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;

import udd.searcher.ResultRetriever;

public class PhraseSearcher {
	protected File indexDirPath;
	protected PhraseQuery query = new PhraseQuery();
	
	public PhraseSearcher(){
		this(new File(ResourceBundle.getBundle("index").getString("index")));
	}
	
	public PhraseSearcher(String path){
		this(new File(path));
	}
	
	public PhraseSearcher(File indexDirPath){
		this.indexDirPath = indexDirPath;
	}

	public List<Document> search(String fieldName, String... values){
		if(values.length == 0) return null;
		PhraseQuery query = new PhraseQuery();
		for(String value : values){
			query.add(new Term(fieldName, value));
		}
		return ResultRetriever.getResults(query, indexDirPath, true);
	}

}
