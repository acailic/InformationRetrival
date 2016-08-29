package udd.indexer.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import udd.exception.IncompleteIndexDocumentException;

public class TextDocHandler extends DocumentHandler {

	@Override
	public Document getDocument(File file, String authorOfFile, long addTime,
			long idOfFile) throws IncompleteIndexDocumentException {
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			/*
			 * forma: prvi red naslov, drugi kljucne reci. 
			 * Naslov
			 *  kljucna rec
			 * jedan dva tri tu ide tekst
			 *  i opet tekst hvala
			 *   svima
			 */
			String title = reader.readLine();

			TextField titleField = new TextField("title", title, Store.YES);
			TextField authorField = new TextField("author", "student",
					Store.YES);
			TextField fileNameField = new TextField("fileName", file.getName(),
					Store.YES);
			StringField locationField = new StringField("location",
					file.getCanonicalPath(), Store.YES);
			StringField id = new StringField("id", ""
					+ System.currentTimeMillis(), Store.YES);

			Document doc = new Document();
			doc.add(id);
			doc.add(titleField);
			doc.add(authorField);
			doc.add(fileNameField);
			doc.add(locationField);

			String secondLine = reader.readLine();
			String[] keywords = secondLine.split(" ");
			for (String kw : keywords) {
				doc.add(new TextField("keyword", kw, Store.YES));
			}

			String text = "";
			String read = "";
			while (true) {
				try {
					read = reader.readLine();
					if (read != null) {
						text += "\n" + read;
					} else {
						break;
					}
				} catch (Exception e) {
					break;
				}
			}
			doc.add(new TextField("text", text, Store.NO));

			return doc;
		} catch (FileNotFoundException fnfe) {
			return null;
		} catch (IOException ioe) {
			return null;
		}
	}

}
