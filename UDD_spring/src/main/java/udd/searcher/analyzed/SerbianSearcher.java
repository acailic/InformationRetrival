package udd.searcher.analyzed;

import java.io.File;

import udd.analyzer.SerbianAnalyzer;

public class SerbianSearcher extends AnalyzedSearcher {
	
	public SerbianSearcher(){
		super(new SerbianAnalyzer(v));
	}
	
	public SerbianSearcher(String path){
		super(path, new SerbianAnalyzer(v));
	}
	
	public SerbianSearcher(File indexDirPath){
		super(indexDirPath, new SerbianAnalyzer(v));
	}

}
