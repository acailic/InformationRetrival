package app.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import udd.analyzer.filter.CyrillicLatinConverter;
import udd.exception.IncompleteIndexDocumentException;
import udd.indexer.IndexManager;
import udd.model.DataHolder;
import udd.model.SearchType.Type;
import udd.query.QueryBuilder;
import udd.searcher.InformationRetriever;
import app.model.CurrentUser;

@Controller
@RequestMapping("/document")
@SuppressWarnings({"serial", "unchecked"})
public class DocumentController {
	
	private static final String HOME_URL = "http://localhost:8080/";
	
	@Autowired
	private Environment env;
	
	@Autowired
    JavaMailSender mailSender;
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping("/upload")
	public String uploadRedirect(ModelMap model,
			Authentication authentication) {
		return "upload";
	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public String uploadFile(
			@RequestParam("uploadfile") MultipartFile uploadfile,
			ModelMap model, Authentication authentication) {

		if (authentication != null) {
			CurrentUser journalist = (CurrentUser) authentication
					.getPrincipal();

			try {
				String storagePath = ResourceBundle.getBundle("index")
						.getString("docs");
				String fileName = uploadfile.getOriginalFilename();
				if (!(fileName.endsWith("pdf")))
					return "redirect:" + HOME_URL + "/document/upload?error";

				fileName = CyrillicLatinConverter.cir2lat(fileName);
				fileName = System.currentTimeMillis() + "_" + fileName;
				File storage = new File(storagePath);
				File uploadedFile = new File(storage, fileName);
				//in case of exception
				model.addAttribute("uploadFilePath", uploadedFile);

				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(uploadedFile));
				stream.write(uploadfile.getBytes());
				stream.close();
				String email = journalist.getUser().getEmail();
				int indexOfAt = email.indexOf("@");
				int indexOfDot = email.indexOf(".");
				String firstPart = email.substring(0, indexOfAt);
				String secondPart = email.substring(indexOfAt + 1, indexOfDot);
				String thirdPart = email.substring(indexOfDot + 1, email.length());
				String queryMail = firstPart + "AT" + secondPart + "DOT" + thirdPart;
				System.out.println(queryMail);
				IndexManager.getIndexer().index(uploadedFile,
						queryMail, System.currentTimeMillis(),
						System.currentTimeMillis());

				return "redirect:" + HOME_URL + "/document/upload?success";
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
				if (e.getMessage().contains("Document without")){
					System.out.println("Usao u if");
					model.addAttribute("title", false);
					model.addAttribute("abstractText", false);
					model.addAttribute("categories", false);
					model.addAttribute("tags", false);
					model.addAttribute("keywords", false);
					
					String [] messages = e.getMessage().split("\n");
					for(String msg : messages){
						switch(msg){
						case "Document without title" : model.addAttribute("title", true); break;
						case "Document without abstract" : model.addAttribute("abstractText", true); break;
						case "Document without categories" : model.addAttribute("categories", true); break;
						case "Document without tags" : model.addAttribute("tags", true); break;
						case "Document without keywords" : model.addAttribute("keywords", true); 
						}
					}
					return "updateDocumentMetadata";
				}
				else
					return "redirect:" + HOME_URL + "/document/upload?error";
			}
		} else
			return "redirect:" + HOME_URL + "/document/upload?error";
	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "updateMetadata", method = RequestMethod.POST)
	public String updateMetadata(
			 HttpServletRequest request, ModelMap model, Authentication authentication) {
		
		if (authentication != null) {
			CurrentUser journalist = (CurrentUser) authentication
					.getPrincipal();
			String filePath = request.getParameter("uploadFile");
			String categories = request.getParameter("categoriesOfFile");
			String keywords = request.getParameter("keywordsOfFile");
			String tags = request.getParameter("tagsOfFile");
			String title = request.getParameter("titleOfFile");
			String abstractOfFile = request.getParameter("abstractOfFile");
			File storedFile = new File(filePath);
			
			try {
				PDFParser parser = new PDFParser(
						new FileInputStream(storedFile));
				parser.parse();
				PDDocument pdf = parser.getPDDocument();
				PDDocumentInformation info = pdf.getDocumentInformation();

				if (filePath == null)
					return "redirect:" + HOME_URL + "/document/upload?error";
				if (categories == null && keywords == null && tags == null
						&& title == null && abstractOfFile == null)
					return "redirect:" + HOME_URL + "/document/upload?error";
				if (categories != null)
					info.setCustomMetadataValue("categories", categories);
				if (tags != null)
					info.setCustomMetadataValue("tags", tags);
				if (keywords != null)
					info.setKeywords(keywords);
				if (abstractOfFile != null)
					info.setCustomMetadataValue("abstract",
							abstractOfFile);
				if (title != null)
					info.setTitle(title);
				
				pdf.setDocumentInformation(info);
				pdf.save(storedFile);
				String email = journalist.getUser().getEmail();
				int indexOfAt = email.indexOf("@");
				int indexOfDot = email.indexOf(".");
				String firstPart = email.substring(0, indexOfAt);
				String secondPart = email.substring(indexOfAt + 1, indexOfDot);
				String thirdPart = email.substring(indexOfDot + 1, email.length());
				String queryMail = firstPart + "AT" + secondPart + "DOT" + thirdPart;
				IndexManager.getIndexer().index(storedFile,
						queryMail, System.currentTimeMillis(),
						0);
				//IndexManager.
				return "redirect:" + HOME_URL + "/document/upload?success";
			} catch (IncompleteIndexDocumentException e) {
				return "redirect:" + HOME_URL + "/document/upload?success";
			} catch (COSVisitorException e){
				return "redirect:" + HOME_URL + "/document/upload?error";
			} catch (IOException e){
				return "redirect:" + HOME_URL + "/document/upload?error";
			}
		} else 
			return "redirect:" + HOME_URL + "/document/upload?error";
	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "/myArticles")
	public String listMyArticles(ModelMap model, Authentication authentication) {
		if (authentication != null) {
			CurrentUser journalist = (CurrentUser) authentication
					.getPrincipal();
			try{
				List<DataHolder> results = new ArrayList<DataHolder>();
				String email = journalist.getUser().getEmail();
				int indexOfAt = email.indexOf("@");
				int indexOfDot = email.indexOf(".");
				String firstPart = email.substring(0, indexOfAt);
				String secondPart = email.substring(indexOfAt + 1, indexOfDot);
				String thirdPart = email.substring(indexOfDot + 1, email.length());
				String queryMail = firstPart + "AT" + secondPart + "DOT" + thirdPart;
				Query query = QueryBuilder.buildQuery(Type.regular, "author",
						queryMail);
				System.out.println(query.toString());
				results = InformationRetriever.getData(query, null, null);
				model.addAttribute("results", results);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return "myArticles";
	}

	@RequestMapping(value = "/allArticles")
	public String listAllArticles(ModelMap model) {
		List<DataHolder> results = new ArrayList<DataHolder>();
		try {
			long currentTime = System.currentTimeMillis();
			long oneDay = 1000L*60*60*24;
			String lowerDate = DateTools.dateToString(new Date(currentTime - 7*oneDay), Resolution.SECOND);
			String upperDate = DateTools.dateToString(new Date(currentTime), Resolution.SECOND);
			Query query = QueryBuilder.buildQuery(Type.range, "publishTime",
					lowerDate + " " + upperDate);
			Sort sorter = new Sort(); 
			String field = "publishTime"; 
			boolean descending = true;
			SortField sortField = new SortField(field, SortField.Type.STRING, descending);
			sorter.setSort(sortField); 
			results = InformationRetriever.getData(query, null, sorter);
			model.addAttribute("results", results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "allArticles";
	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "edit")
	public String editArticle(HttpServletRequest request, Model model) {
		List<DataHolder> results = new ArrayList<DataHolder>();
		String id = request.getParameter("id").toString();
		Query query = null;
		try {
			query = QueryBuilder.buildQuery(Type.regular, "id",
					id);
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
		} catch ( ParseException e){
			e.printStackTrace();
		}
		results = InformationRetriever.getData(query, null, null);
		model.addAttribute("editArticle", results.get(0));
		return "articleUpdate";
	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateArticle(HttpServletRequest request) {
		String id = request.getParameter("id").toString();
		String categories = request.getParameter("categories");
		String keywords = request.getParameter("keywords");
		String tags = request.getParameter("tags");
		String title = request.getParameter("title");
		String abstractOfFile = request.getParameter("abstractOfArticle");
		
		IndexableField [] indexableFields = new IndexableField[6];
		int index = 0;
		
		if(title != null && !title.equals("")){
			indexableFields[index] = new TextField("title", title, Store.YES);
			index++;
		}
		if(abstractOfFile != null && !abstractOfFile.equals("")){
			indexableFields[index] = new TextField("abstract", abstractOfFile, Store.YES);
			index++;
		}
		if(keywords != null && !keywords.equals("")){
			indexableFields[index] = new TextField("keywords", keywords, Store.YES);
			index++;
		}
		if(tags != null && !tags.equals("")){
			indexableFields[index] = new TextField("tags", tags, Store.YES);
			index++;
		}
		if(categories != null && !categories.equals("")){
			indexableFields[index] = new TextField("categories", categories, Store.YES);
			index++;
		}
		indexableFields[index] = new TextField("modification", DateTools.dateToString(new Date(System.currentTimeMillis()), 
				Resolution.SECOND), Store.YES);
		Document [] documents = IndexManager.getIndexer().getAllDocuments();
		Document documentToUpdate = null;
		for(int i = 0; i < documents.length; ++i){
			if(documents[i].get("id").equals(id)){
				documentToUpdate = documents[i];
				break;
			}
		}
		
		if(documentToUpdate != null)
			IndexManager.getIndexer().updateDocument(documentToUpdate, indexableFields);
		return "redirect:" + HOME_URL + "/document/myArticles";

	}
	
	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "/unpublishedArticles")
	public String listEditorUnpublishedArticles(ModelMap model, Authentication authentication) {
		if (authentication != null) {
			CurrentUser editor = (CurrentUser) authentication
					.getPrincipal();
			try{
				List<DataHolder> unpublishedArticles = new ArrayList<DataHolder>();
				Query query = QueryBuilder.buildQuery(Type.regular, "publishTime",
						"null");
				unpublishedArticles = InformationRetriever.getData(query, null, null);
				model.addAttribute("unpublishedArticles", unpublishedArticles);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return "unpublishedArticles";
	}
	
	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "/editorArticles")
	public String listEditorPublishedArticles(ModelMap model, Authentication authentication) {
		if (authentication != null) {
			CurrentUser editor = (CurrentUser) authentication
					.getPrincipal();
			try{
				List<DataHolder> publishedArticles = new ArrayList<DataHolder>();
				Query query = QueryBuilder.buildQuery(Type.regular, "editor",
						editor.getUser().getEmail());
				publishedArticles = InformationRetriever.getData(query, null, null);
				model.addAttribute("publishedArticles", publishedArticles);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return "editorArticles";
	}
	
	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "publish")
	public String publishArticle(HttpServletRequest request, Model model, Authentication authentication) {
		CurrentUser editor = (CurrentUser) authentication
				.getPrincipal();
		String id = request.getParameter("id").toString();
		
		Document [] documents = IndexManager.getIndexer().getAllDocuments();
		Document documentToPublish = null;
		for(int i = 0; i < documents.length; ++i){
			if(documents[i].get("id").equals(id)){
				documentToPublish = documents[i];
				break;
			}
		}
		
		IndexableField [] indexableFields = new IndexableField[3];
		indexableFields[0] = new TextField("editor", editor.getUser().getEmail(), Store.YES);
		indexableFields[1] = new TextField("publishTime", DateTools.dateToString(new Date(System.currentTimeMillis()), 
				Resolution.SECOND), Store.YES);
		indexableFields[2] = new TextField("modification", DateTools.dateToString(new Date(System.currentTimeMillis()), 
				Resolution.SECOND), Store.YES);
		
		if(documentToPublish != null)
			IndexManager.getIndexer().updateDocument(documentToPublish, indexableFields);
		return "redirect:" + HOME_URL + "/document/editorArticles";
		
		/*List<Document> resultDocuments = new ArrayList<Document>();
		Query query = null;
		try {
			query = QueryBuilder.buildQuery(Type.regular, "id",
					id);
		} catch (IllegalArgumentException | ParseException e) {
			e.printStackTrace();
		}
		resultDocuments = ResultRetriever.getResults(query);
		model.addAttribute("publishedArticles", resultDocuments.get(0));*/
		//return "articleUpdate";
	}
	
	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "remove")
	public String removeArticle(HttpServletRequest request, Model model) {
		List<DataHolder> results = new ArrayList<DataHolder>();
		String id = request.getParameter("id").toString();
		Document [] documents = IndexManager.getIndexer().getAllDocuments();
		Document documentToRemove = null;
		for(int i = 0; i < documents.length; ++i){
			if(documents[i].get("id").equals(id)){
				documentToRemove = documents[i];
				break;
			}
		}
		
		IndexableField [] indexableFields = new IndexableField[3];
		indexableFields[0] = new TextField("editor", "null", Store.YES);
		indexableFields[1] = new TextField("publishTime", "null", Store.YES);
		indexableFields[2] = new TextField("modification", DateTools.dateToString(new Date(System.currentTimeMillis()), 
				Resolution.SECOND), Store.YES);
		
		if(documentToRemove != null)
			IndexManager.getIndexer().updateDocument(documentToRemove, indexableFields);
		return "redirect:" + HOME_URL + "/document/editorArticles";
	}
	
	@PreAuthorize("hasAuthority('EDITOR')")
	@RequestMapping(value = "notify")
	public String notifyJournalist(HttpServletRequest request, Model model) {
		String emailOfAuthor = request.getParameter("author").toString();
		String id = request.getParameter("id").toString();
		String title = request.getParameter("title").toString();
		SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailOfAuthor);
        email.setSubject("Modify your article for publishing");
        email.setText("Please modify the article with title " + title + " and ID " + id + " for publishing purposes.");
         
        // sends the e-mail
        mailSender.send(email);
		return "redirect:" + HOME_URL + "/document/editorArticles";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "showPDF", method = RequestMethod.GET)
	public String getPDF(HttpServletRequest request,
			ModelMap model) {
		String filePath = request.getParameter("file");
		com.aspose.pdf.Document pdfDocument = new com.aspose.pdf.Document(filePath);
		String htmlPath = "D:\\Fakultet\\MASTER\\Upravljanje_digitalnim_dokumentima\\ilic\\UddProjekat_springBoot\\src\\main\\webapp\\WEB-INF\\jsp\\myPdf.html";
		pdfDocument.save(htmlPath, com.aspose.pdf.SaveFormat.Html);
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(htmlPath));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		model.addAttribute("content", content);
		return "editArticle";
	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "updateText", method = RequestMethod.POST)
	public String updateArticleText(HttpServletRequest request) {
		String id = request.getParameter("id").toString();
		String filePath =  request.getParameter("location");
		String text = request.getParameter("text");
		
		IndexableField [] indexableFields = new IndexableField[5];
		int index = 0;
		
		if(text != null && !text.equals("")){
			indexableFields[index] = new TextField("text", text, Store.YES);
			index++;
		}
		
		Document [] documents = IndexManager.getIndexer().getAllDocuments();
		Document documentToUpdate = null;
		for(int i = 0; i < documents.length; ++i){
			if(documents[i].get("id").equals(id)){
				documentToUpdate = documents[i];
				break;
			}
		}
		
		try {
			File storedFile = new File(filePath);
			PDFParser parser = new PDFParser(new FileInputStream(storedFile));
			parser.parse();
			PDDocument pdf = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper("utf-8");
			String textOfPDF = stripper.getText(pdf);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(documentToUpdate != null)
			IndexManager.getIndexer().updateDocument(documentToUpdate, indexableFields);
		return "redirect:" + HOME_URL + "/document/myArticles";

	}
	
	@PreAuthorize("hasAuthority('JOURNALIST')")
	@RequestMapping(value = "/editArticle")
	public String editArticle(ModelMap model, Authentication authentication) {
		return "editArticle";

	}

	public static String parseEmail(String email)
	{
		String parsedMail=null;
		if(email.contains("@")){
			parsedMail =email.replace("@", "AT");
		}
		if(email.contains(".")){
			parsedMail+=email.replace(".", "DOT");
			}
		return parsedMail;
	}
}
