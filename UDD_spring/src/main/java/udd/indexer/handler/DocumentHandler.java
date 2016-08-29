package udd.indexer.handler;

import java.io.File;
import java.util.Date;

import org.apache.lucene.document.Document;

import udd.exception.IncompleteIndexDocumentException;

public abstract class DocumentHandler {
	public abstract Document getDocument(File file, String author, long addTime, long id) throws IncompleteIndexDocumentException;
}
