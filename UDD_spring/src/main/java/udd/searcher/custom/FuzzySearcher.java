package udd.searcher.custom;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;

import udd.searcher.ResultRetriever;

public class FuzzySearcher {
	protected File indexDirPath;
	protected float similarity;
	
	public FuzzySearcher(float similarity){
		this(new File(ResourceBundle.getBundle("index").getString("index")), similarity);
	}
	
	public FuzzySearcher(String path, float similarity){
		this(new File(path), similarity);
	}
	
	public FuzzySearcher(File indexDirPath, float similarity){
		if(similarity <= 0 || similarity > 1){
			throw new IllegalArgumentException("Slicnost mora biti broj iz opsega (0,1]");
		}
		this.similarity = similarity;
		this.indexDirPath = indexDirPath;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(float similarity) {
		if(similarity <= 0 || similarity > 1){
			throw new IllegalArgumentException("Slicnost mora biti broj iz opsega (0,1]");
		}
		this.similarity = similarity;
	}
	
	public List<Document> search(Term term){
		return this.search(term, similarity);
	}
	
	public List<Document> search(Term term, float similarity){
		if(similarity <= 0 || similarity > 1){
			throw new IllegalArgumentException("Slicnost mora biti broj iz opsega (0,1]");
		}
		int maxEdits = (int) Math.floor((1 - similarity)*term.field().length());
		return search(term, maxEdits);
	}
	
	public List<Document> search(Term term, int maxEdits){
		if(term == null) return null;
		FuzzyQuery query = new FuzzyQuery(term, maxEdits);
		return ResultRetriever.getResults(query, indexDirPath, true);
	}

}
