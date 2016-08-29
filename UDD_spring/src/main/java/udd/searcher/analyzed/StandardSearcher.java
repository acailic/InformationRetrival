package udd.searcher.analyzed;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class StandardSearcher extends AnalyzedSearcher {
	
	public StandardSearcher(){
		super(new StandardAnalyzer(v));
	}
	
	public StandardSearcher(String path){
		super(path, new StandardAnalyzer(v));
	}
	
	public StandardSearcher(File indexDirPath){
		super(indexDirPath, new StandardAnalyzer(v));
	}

}
