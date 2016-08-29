package udd.searcher.custom;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

import udd.searcher.ResultRetriever;

public class BooleanSearcher {
	protected File indexDirPath;
	private BooleanQuery query = new BooleanQuery();
	
	public BooleanSearcher(){
		this.indexDirPath = new File(ResourceBundle.getBundle("index").getString("index"));
	}
	
	public BooleanSearcher(String path){
		this.indexDirPath = new File(path);
	}
	
	public BooleanSearcher(File indexDirPath){
		this.indexDirPath = indexDirPath;
	}
	
	public void addClauses(BooleanClause... clauses){
		for(BooleanClause clause : clauses){
			this.query.add(clause);
		}
	}
	
	public void addClause(Query query, Occur occur){
		this.query.add(query, occur);
	}
	
	public void removeLastClause(){
		int index = this.query.clauses().size() - 1; 
		this.query.clauses().remove(index);
	}
	
	public void resetQuery(){
		this.query = new BooleanQuery();
	}
	
	public BooleanQuery getQuery() {
		return query;
	}

	public List<Document> search(boolean resetQuery){
		List<Document> list = ResultRetriever.getResults(this.query, indexDirPath, true);
		if(resetQuery){
			query = new BooleanQuery();
		}
		return list;
	}
	
	public List<Document> search(BooleanClause... clauses){
		BooleanQuery query = new BooleanQuery();
		for(BooleanClause clause : clauses){
			query.add(clause);
		}
		return ResultRetriever.getResults(query, indexDirPath, true);
	}

}
