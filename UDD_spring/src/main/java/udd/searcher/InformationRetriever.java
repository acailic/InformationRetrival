package udd.searcher;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.spell.DirectSpellChecker;
import org.apache.lucene.search.spell.SuggestWord;
import org.apache.lucene.util.Version;

import udd.analyzer.SerbianAnalyzer;
import udd.exception.IncompleteIndexDocumentException;
import udd.indexer.IndexManager;
import udd.indexer.UDDIndexer;
import udd.model.DataHolder;
import udd.model.RequiredHighlight;

public class InformationRetriever {
	
	private static int maxHits = 10;
	private static final Version matchVersion = Version.LUCENE_4_9;
	private static Analyzer analyzer = new SerbianAnalyzer(matchVersion);
	
	public static List<DataHolder> getData(Query query, List<RequiredHighlight> requiredHighlights, Sort sorter){

		if(query == null) return null;
		//IndexManager.getIndexer();
		/*Sort sorterNull = null;
		List<Document> docsIndexOrder = ResultRetriever.getResults(query, sorterNull);
		List<Document> documentsToRemove = new ArrayList<Document>();
		Map<String, Integer> documentNumberMax = new HashMap<String, Integer>();
		for(Document doc : docsIndexOrder){
			if(!documentNumberMax.containsKey(doc.get("id")))
				documentNumberMax.put(doc.get("id"), 1);
			else{
				int num = documentNumberMax.get(doc.get("id"));
				num++;
				documentNumberMax.put(doc.get("id"), num);
			}
		}
		Map<String, Integer> documentNumber = new HashMap<String, Integer>();
		for(Document doc : docsIndexOrder){
			if(!documentNumber.containsKey(doc.get("id")))
				documentNumber.put(doc.get("id"), 1);
			else{
				int num = documentNumber.get(doc.get("id"));
				num++;
				documentNumber.put(doc.get("id"), num);
			}
			Document duplicate = IndexManager.getIndexer().extractLastFromDuplicates(doc.get("id"));
			if(duplicate != null){
				System.out.println("PUBLISH TIME : " + duplicate.get("publishTime"));
				if(!(doc.get("publishTime").equals(duplicate.get("publishTime")) && 
						doc.get("author").equals(duplicate.get("author")) && 
						doc.get("addTime").equals(duplicate.get("addTime"))&& 
						doc.get("title").equals(duplicate.get("title"))&& 
						doc.get("keywords").equals(duplicate.get("keywords"))&& 
						doc.get("tags").equals(duplicate.get("tags"))&& 
						doc.get("categories").equals(duplicate.get("categories"))&& 
						doc.get("abstract").equals(duplicate.get("abstract"))
			&& documentNumber.get(doc.get("id")) == documentNumberMax.get(doc.get("id"))))
					documentsToRemove.add(doc);
			}
		}*/
		
		/*if(!documentsToRemove.isEmpty())
			System.out.println("nije empty");*/
		List<Document> docs = ResultRetriever.getResults(query, sorter);
		//docs.removeAll(documentsToRemove);
		List<DataHolder> results = new ArrayList<DataHolder>();
		String temp;
		DataHolder data;
		Highlighter hl;
		DirectoryReader reader;
		try {
			reader = DirectoryReader.open(IndexManager.getIndexer().getIndexDir());
			/*Map<String, Integer> documentNumberMax = new HashMap<String, Integer>();
			for(Document doc : docs){
				if(!documentNumberMax.containsKey(doc.get("id")))
					documentNumberMax.put(doc.get("id"), 1);
				else{
					int num = documentNumberMax.get(doc.get("id"));
					num++;
					documentNumberMax.put(doc.get("id"), num);
				}
			}
			Map<String, Integer> documentNumber = new HashMap<String, Integer>();*/
			for(Document doc : docs){
				
				/*if(!documentNumber.containsKey(doc.get("id")))
					documentNumber.put(doc.get("id"), 1);
				else{
					int num = documentNumber.get(doc.get("id"));
					num++;
					documentNumber.put(doc.get("id"), num);
				}*/
				Document duplicate = IndexManager.getIndexer().extractLastFromDuplicates(doc.get("id"));
				if(duplicate != null){
					System.out.println("PUBLISH TIME : " + duplicate.get("publishTime"));
					if(!(doc.get("publishTime").equals(duplicate.get("publishTime")) && 
							doc.get("author").equals(duplicate.get("author")) && 
							doc.get("addTime").equals(duplicate.get("addTime"))&& 
							doc.get("title").equals(duplicate.get("title"))&& 
							doc.get("keywords").equals(duplicate.get("keywords"))&& 
							doc.get("tags").equals(duplicate.get("tags"))&& 
							doc.get("categories").equals(duplicate.get("categories"))&& 
							doc.get("abstract").equals(duplicate.get("abstract"))
							&& doc.get("modification").equals(duplicate.get("modification"))
				/*&& documentNumber.get(doc.get("id")) == documentNumberMax.get(doc.get("id"))*/))
						continue;
				}
				data = new DataHolder();
				String[] allKeywords = doc.getValues("keywords");
				temp = "";
				for(String keyword : allKeywords){
					temp += keyword + ", ";
				}
				if(!temp.equals("")){
					temp = temp.substring(0, temp.length()-2);
				}
				data.setKeywords(temp);
				
				String[] allTags = doc.getValues("tags");
				temp = "";
				for(String tag : allTags){
					temp += tag + ", ";
				}
				if(!temp.equals("")){
					temp = temp.substring(0, temp.length()-2);
				}
				data.setTags(temp);
				
				String[] allCategories = doc.getValues("categories");
				temp = "";
				for(String category : allCategories){
					temp += category + ", ";
				}
				if(!temp.equals("")){
					temp = temp.substring(0, temp.length()-2);
				}
				data.setCategories(temp);
				
				data.setTitle(doc.get("title"));
				data.setAuthor(doc.get("author"));
				data.setLocation(doc.get("location"));
				data.setFileName(doc.get("fileName"));
				data.setAbstractOfFile(doc.get("abstract"));
				data.setId(doc.get("id"));
				//sastavljanje datuma
				String publishDate = doc.get("publishTime");
				if(!publishDate.equals("null")){
					String day = publishDate.substring(6, 8);
					String month = publishDate.substring(4, 6);
					String year = publishDate.substring(0, 4);
					data.setPublishTime(day + "." + month + "." + year);
				} else
					data.setPublishTime(publishDate);
				//data.setEditor(doc.get("editor"));
				
				temp = "";
				if(requiredHighlights != null){
					for(RequiredHighlight rh : requiredHighlights){
						try{
							hl = new Highlighter(new QueryScorer(query, reader, rh.getFieldName()));
							File docFile = new File(doc.get("location"));
							String value = UDDIndexer.getHandler(docFile).getDocument(docFile, null, 0, 0).get(rh.getFieldName());
							String tempHL = hl.getBestFragment(analyzer, rh.getFieldName(), value);
							if(tempHL!=null){
								temp += rh.getFieldName() + ": " + tempHL.trim() + " ... ";
							}
						}catch(Exception e){
							
						}
					}
				}
				data.setHighlight(temp);
				results.add(data);
			}
			reader.close();
			return results;
		} catch (IOException e) {
		}
		throw new IllegalArgumentException("U prosledjenom direktorijumu ne postoje indeksi ili je direktorijum zakljucan");
	}
	
	public static List<RequiredHighlight> getSuggestions(Term term){
		if (term == null) {
			return null;
		}
		DirectoryReader reader;
		DirectSpellChecker dsc = new DirectSpellChecker();
		try {
			reader = DirectoryReader.open(IndexManager.getIndexer().getIndexDir());
			SuggestWord[] suggestions = dsc.suggestSimilar(term, maxHits, reader);
			List<RequiredHighlight> suggestedTerms = new ArrayList<RequiredHighlight>();
			for(SuggestWord sw : suggestions){
				suggestedTerms.add(new RequiredHighlight(term.field(), sw.string, null));
			}
			return suggestedTerms;
		}catch(Exception e){
		}
		
		throw new IllegalArgumentException("U prosledjenom direktorijumu ne postoje indeksi ili je direktorijum zakljucan");
	}
	
	public static List<DataHolder> getMoreLikeThis(String fileName){
		File docFile = new File(ResourceBundle.getBundle("index").getString("docs") + "/" + fileName);
		List<DataHolder> results = null;
		
		DirectoryReader dReader;
		try {
			String text = UDDIndexer.getHandler(docFile).getDocument(docFile, null, 0, 0).get("text");
			dReader = DirectoryReader.open(IndexManager.getIndexer().getIndexDir());
			
			MoreLikeThis mlt = new MoreLikeThis(dReader);
			mlt.setMinTermFreq(0);
			mlt.setMinDocFreq(0);
			mlt.setFieldNames(new String[]{"text"});
			mlt.setAnalyzer(analyzer);
			
			Reader sReader = new StringReader(text);
			Query query = mlt.like("text", sReader);
			
			results = getData(query, null, null);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(IncompleteIndexDocumentException e){
			e.printStackTrace();
		}
		
		return results;
	}

}
