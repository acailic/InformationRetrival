package udd.indexer.handler;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;

import udd.exception.IncompleteIndexDocumentException;

public class PDFHandler extends DocumentHandler {

	@SuppressWarnings("deprecation")
	@Override
	public Document getDocument(File file, String authorOfFile, long addTime, long updateTime) throws IncompleteIndexDocumentException {
		Document doc = new Document();
		TextField fileNameField = new TextField("fileName",	file.getName(), Store.YES);
		doc.add(fileNameField);
		StringField idField = new StringField("id", ""+System.currentTimeMillis(), Store.YES);
		doc.add(idField);
		String error = "";
		try {
			StringField locationField = new StringField("location", file.getCanonicalPath(), Store.YES);
			doc.add(locationField);
			//napraviti pdf parser
			PDFParser parser = new PDFParser(new FileInputStream(file));
			//izvrsiti parsiranje
			parser.parse();
			
			//od parsera preuzeti parsirani pdf dokument (PDDocument)
			PDDocument pdf = parser.getPDDocument();
			
			//Upotrebiti text stripper klasu za ekstrahovanje teksta sa utf-8 kodnom stranom (PDFTextStripper)
			PDFTextStripper stripper = new PDFTextStripper("utf-8");
			String text = stripper.getText(pdf);
			if(text!=null && !text.trim().equals("")){
				doc.add(new TextField("text", text, Store.NO));
			}else{
				error += "Document without text\n";
			}
			
			//iz dokumenta izvuci objekat u kojem su svi metapodaci (PDDocumentInformation)
			PDDocumentInformation info = pdf.getDocumentInformation();
			
			//iz tog objekta preuzeti sto vise metapodataka i sve ih dodati na dokument
			/*String id = info.getCustomMetadataValue("id");
			if(id != null && !id.trim().equals("")){
				StringField idField = new StringField("id", ""+System.currentTimeMillis(), Store.YES);
				doc.add(idField);
			}*/
			
			String publishTimeOfArticle = doc.get("publishTime");
			if(publishTimeOfArticle!=null && !publishTimeOfArticle.trim().equals("")){
				doc.add(new TextField("publishTime", publishTimeOfArticle, Store.YES));
			} else
				doc.add(new TextField("publishTime", "null", Store.YES));
			
			String editor = info.getCustomMetadataValue("editor");
			if(editor!=null && !editor.trim().equals("")){
				doc.add(new TextField("editor", editor, Store.YES));
			}
			
			String author = null;
			if(authorOfFile != null){
				doc.add(new TextField("author", authorOfFile, Store.YES));
				info.setAuthor(authorOfFile);
				author = authorOfFile;
			}
			else {
				String authorInfo = info.getAuthor();
				if(authorInfo!=null && !authorInfo.trim().equals("")){
					doc.add(new TextField("author", authorInfo, Store.YES));
				}else{
					/*if(authorOfFile != null)
						doc.add(new TextField("author", authorOfFile, Store.YES));
					else*/
						error += "Document without author\n";
				}
			}
			
			//add time
			String addTimeOfArticle = null;
			if(addTime != 0){
				//doc.add(new TextField("addTime", addTime.toString(), Store.YES));
				info.setCustomMetadataValue("addTime", DateTools.dateToString(new Date(addTime), Resolution.SECOND));
				addTimeOfArticle = DateTools.dateToString(new Date(addTime), Resolution.SECOND);
				/*doc.add(new Field("addTime",
				        DateTools.dateToString(addTime, DateTools.Resolution.HOUR),
				        Field.Store.YES, Field.Index.ANALYZED));*/
				doc.add(new TextField("addTime", DateTools.dateToString(new Date(addTime), Resolution.SECOND), Store.YES));
				//doc.add(new LongField());
			}
			else {
				addTimeOfArticle = info.getCustomMetadataValue("addTime");
				if(addTimeOfArticle!=null && !addTimeOfArticle.trim().equals("")){
					doc.add(new TextField("addTime", addTimeOfArticle, Store.YES));
				}else{
					error += "Document without add time\n";
				}
			}
			
			String title = info.getTitle();
			if(title!=null && !title.trim().equals("")){
				doc.add(new TextField("title", title, Store.YES));
			}else{
				error += "Document without title\n";
			}
			
			String keywords = info.getKeywords();
			if(keywords == null){
				//keywords = title + " " + author;
				error += "Document without keywords\n";
			} else {
				doc.add(new TextField("keywords", keywords, Store.YES));
			}
			//CUSTOM FIELD-OVI
			//tagovi
			String tags = info.getCustomMetadataValue("tags");
			if(tags == null){
				//tags = title + " " + author;
				error += "Document without tags\n";
			} else {
				doc.add(new TextField("tags", tags, Store.YES));
			}
			
			//apstrakt
			String abstractOfArticle = info.getCustomMetadataValue("abstract");
			if(abstractOfArticle!=null && !abstractOfArticle.trim().equals("")){
				doc.add(new TextField("abstract", abstractOfArticle, Store.YES));
			}else{
				error += "Document without abstract\n";
			}
			
			//kategorije
			String categories = info.getCustomMetadataValue("categories");
			if(categories == null){
				error += "Document without categories\n";
			} else {
				doc.add(new TextField("categories", categories, Store.YES));
			}
			
			//zatvoriti pdf dokument
			pdf.setDocumentInformation(info);
			pdf.save(file);
			pdf.close();
		} catch (Exception e) {
			System.out.println("Error with converting pdf.");
			error = "Document is incomplete. An exception occured";
		}
		
		if(!error.equals("")){
			throw new IncompleteIndexDocumentException(error.trim());
		}
		
		return doc;
	}

}
