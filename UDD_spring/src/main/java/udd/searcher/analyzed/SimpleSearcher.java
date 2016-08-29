package udd.searcher.analyzed;

import java.io.File;

import org.apache.lucene.analysis.core.SimpleAnalyzer;

public class SimpleSearcher extends AnalyzedSearcher {
	
	public SimpleSearcher(){
		super(new SimpleAnalyzer(v));
	}
	
	public SimpleSearcher(String path){
		super(path, new SimpleAnalyzer(v));
	}
	
	public SimpleSearcher(File indexDirPath){
		super(indexDirPath, new SimpleAnalyzer(v));
	}

}
