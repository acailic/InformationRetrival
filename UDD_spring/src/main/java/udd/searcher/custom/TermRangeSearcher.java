package udd.searcher.custom;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;

import udd.searcher.ResultRetriever;

public class TermRangeSearcher {
	protected File indexDirPath;
	protected boolean includeLower;
	protected boolean includeUpper;
	
	public TermRangeSearcher(){
		this(new File(ResourceBundle.getBundle("index").getString("index")), false, false);
	}
	
	public TermRangeSearcher(boolean includeLower, boolean includeUpper){
		this(new File(ResourceBundle.getBundle("index").getString("index")), includeLower, includeUpper);
	}
	
	public TermRangeSearcher(String path){
		this(new File(path), false, false);
	}
	
	public TermRangeSearcher(String path, boolean includeLower, boolean includeUpper){
		this(new File(path), includeLower, includeUpper);
	}
	
	public TermRangeSearcher(File indexDirPath, boolean includeLower, boolean includeUpper){
		this.indexDirPath = indexDirPath;
		this.includeLower = includeLower;
		this.includeUpper = includeUpper;
	}
	
	public List<Document> search(String fieldName, String lower, String upper){
		BytesRef lowerTerm = new BytesRef(lower);
		BytesRef upperTerm = new BytesRef(upper);
		TermRangeQuery query = new TermRangeQuery(fieldName, lowerTerm, upperTerm, this.includeLower, this.includeUpper);
		return ResultRetriever.getResults(query, indexDirPath, true);
	}

	public boolean isIncludeLower() {
		return includeLower;
	}

	public void setIncludeLower(boolean includeLower) {
		this.includeLower = includeLower;
	}

	public boolean isIncludeUpper() {
		return includeUpper;
	}

	public void setIncludeUpper(boolean includeUpper) {
		this.includeUpper = includeUpper;
	}

}
