package udd.indexer;

public class IndexManager {
	
	private static UDDIndexer indexer = new UDDIndexer(true);
	
	public static UDDIndexer getIndexer(){
		if(indexer == null){
			indexer = new UDDIndexer(true);
		}
		return indexer;
	}
	
	public static void restart(){
		// indexer.restart(true);
	}

}
 