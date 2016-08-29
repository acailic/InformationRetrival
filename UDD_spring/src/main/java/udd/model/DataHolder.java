package udd.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class DataHolder implements Serializable{
	
	private String id;
	private String title;
	private String keywords;
	private String author;
	private String fileName;
	private String location;
	private String highlight;
	private String editor;
	private String abstractOfFile;
	private String tags;
	private String categories;
	private String publishTime;

	public DataHolder() {
		super();
	}
	
	public DataHolder(String id, String title, String keywords, String author,
			String fileName, String location, String highlight, String editor,
			String abstractOfFile, String tags, String categories, String publishTime) {
		super();
		this.id = id;
		this.title = title;
		this.keywords = keywords;
		this.author = author;
		this.fileName = fileName;
		this.location = location;
		this.highlight = highlight;
		this.editor = editor;
		this.abstractOfFile = abstractOfFile;
		this.tags = tags;
		this.categories = categories;
		this.publishTime = publishTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHighlight() {
		return highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getAbstractOfFile() {
		return abstractOfFile;
	}

	public void setAbstractOfFile(String abstractOfFile) {
		this.abstractOfFile = abstractOfFile;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
}
