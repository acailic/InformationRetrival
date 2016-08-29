package udd.searcher.analyzed;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import udd.searcher.ResultRetriever;

public class AnalyzedSearcher{
	protected static final Version v = Version.LUCENE_4_9;
	protected File indexDirPath;
	protected Analyzer analyzer;
	
	public AnalyzedSearcher(Analyzer analyzer){
		this(new File(ResourceBundle.getBundle("index").getString("index")), analyzer);
	}
	
	public AnalyzedSearcher(String path, Analyzer analyzer){
		this(new File(path), analyzer);
	}
	
	public AnalyzedSearcher(File indexDirPath, Analyzer analyzer){
		this.indexDirPath = indexDirPath;
		if(analyzer == null){
			throw new NullPointerException("Morate zadati analyzer");
		}
		this.analyzer = analyzer;
	}
	
	public List<Document> search(Term term) {
		QueryParser qp = new QueryParser(v, term.field(), analyzer);
		Query query;
		try {
			query = qp.parse(term.text());
			return ResultRetriever.getResults(query, this.indexDirPath);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Prosledjeni term nije u redu");
		}
	}
	
	public List<Document> search(Query query) {
		QueryParser qp = new QueryParser(v, "", analyzer);
		try {
			query = qp.parse(query.toString());
			return ResultRetriever.getResults(query, this.indexDirPath);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Prosledjeni term nije u redu");
		}
	}

}
