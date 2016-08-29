package udd.indexer.handler;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import udd.exception.IncompleteIndexDocumentException;

/**
 * 
 * @author Molnar
 * Also known as Word2007Hanlder
 * @category Word 2007 and later
 * @see http://poi.apache.org/text-extraction.html
 */
public class DocxHandler extends DocumentHandler{

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
			
			// kreirati XWPFDocument objekat koji predstavlja docx dokument
			XWPFDocument docx = new XWPFDocument(new FileInputStream(file));
			
			// kreirati XWPFWordExtractor i iz njega preuzeti text
			XWPFWordExtractor we = new XWPFWordExtractor(docx);
			String text = we.getText();
			if(text!=null && !text.trim().equals("")){
				doc.add(new TextField("text", text, Store.NO));
			}else{
				error += "Document without text\n";
			}
			
			// iz dokumenta preuzeti objekat POIXMLProperties u kojem su svi metapodaci
			CoreProperties props = docx.getProperties().getCoreProperties(); //u core properties se nalaze samo najbitniji metapodaci
			
			//moze da se upotrebi i POIXMLPropertiesTextExtractor klasa
			//POIXMLPropertiesTextExtractor pt = new POIXMLPropertiesTextExtractor(docx);
			
			// preuzeti sto vise metapodataka i dodati ih na dokument
			String author = props.getCreator();
			if(author!=null && !author.trim().equals("")){
				doc.add(new TextField("author", author, Store.YES));
			}else{
				error += "Document without author\n";
			}

			String title = props.getTitle();
			if(title!=null && !title.trim().equals("")){
				doc.add(new TextField("title", title, Store.YES));
			}else{
				error += "Document without title";
			}
			
			String keywords = props.getKeywords();
			if(keywords==null){
				keywords = title + " " + author;
			}
			
			String[] kws = keywords.trim().split(" ");
			for(String kw : kws){
				if(!kw.trim().equals("")){
					doc.add(new TextField("keyword", kw, Store.YES));
				}
			}
			
		} catch (Exception e) {
			System.out.println("Greska pri konvertovanju docx dokumenta");
			error = "Document is incomplete. An exception occured";
		}
		
		
		if(!error.equals("")){
			throw new IncompleteIndexDocumentException(error.trim());
		}
		
		return doc;
	}

}
