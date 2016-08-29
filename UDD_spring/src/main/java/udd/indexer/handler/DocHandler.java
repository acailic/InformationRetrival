package udd.indexer.handler;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;

import udd.exception.IncompleteIndexDocumentException;

/**
 * 
 * @author Molnar
 * Also known as WordHandler
 * @category Word 97 - Word 2003
 * @see http://poi.apache.org/text-extraction.html
 */
public class DocHandler extends DocumentHandler{

	@Override
	public Document getDocument(File file, String authorOfFile, long addTime, long idOfFile) throws IncompleteIndexDocumentException {
		Document doc = new Document();
		StringField id = new StringField("id", ""+System.currentTimeMillis(), Store.YES);
		TextField fileNameField = new TextField("fileName",	file.getName(), Store.YES);
		doc.add(id);
		doc.add(fileNameField);
		String error = "";
		
		try {
			StringField locationField = new StringField("location", file.getCanonicalPath(), Store.YES);
			doc.add(locationField);
			
			//napraviti word extractor i pomocu tog objekta izvuci tekst iz dokumenta
			WordExtractor we = new WordExtractor(new FileInputStream(file));
			String text = we.getText();
			if(text!=null && !text.trim().equals("")){
				doc.add(new TextField("text", text, Store.NO));
			}else{
				error += "Document without text\n";
			}
			
			//iz SummaryInformation objekta preuzeti sve metapodatke i dodati ih na dokument
			SummaryInformation info = we.getSummaryInformation();
			
			//na ovaj nacin se preuzimaju svi prisutni metapodaci - mana je sto im ne znamo imena i ne mozemo ni da ih saznamo
			/*
			Property[] props = info.getProperties();
			// id == 1 & type == 2  -> 65001 fiksno
			// id == 2 & type == 30 -> title
			// id == 4 & type == 30 -> author
			// id == 7 & type == 30 -> document template
			// id == 8 & type == 30 -> last saved by 
			// id == 9 & type == 30 -> revision number
			// ...
			*/
			
			String author = info.getAuthor();
			if(author!=null && !author.trim().equals("")){
				doc.add(new TextField("author", author, Store.YES));
			}else{
				error += "Document without author\n";
			}
			
			String title = info.getTitle();
			if(title!=null && !title.trim().equals("")){
				doc.add(new TextField("title", title, Store.YES));
			}else{
				error += "Document without title";
			}
			
			String keywords = info.getKeywords();
			if(keywords==null){
				keywords = title + " " + author;
			}
			
			String[] kws = keywords.trim().split(" ");
			for(String kw : kws){
				if(!kw.trim().equals("")){
					doc.add(new TextField("keyword", kw, Store.YES));
				}
			}
			
			we.close();
		} catch (Exception e) {
			System.out.println("Greksa pri konvertovanju doc dokumenta");
			error = "Document is incomplete. An exception occured";
		}
		
		if(!error.equals("")){
			throw new IncompleteIndexDocumentException(error.trim());
		}
		
		return doc;
	}

}
