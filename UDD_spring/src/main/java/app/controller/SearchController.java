package app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.CurrentUser;
import app.util.Role;
import udd.model.DataHolder;
import udd.model.RequiredHighlight;
import udd.model.SearchType;
import udd.model.SearchType.Type;
import udd.query.QueryBuilder;
import udd.searcher.InformationRetriever;

@Controller
@RequestMapping("/finder")
public class SearchController {
	
	private static final String HOME_URL = "http://localhost:8080/";
	
	@Autowired
	private Environment env;
	
	@RequestMapping("/search")
	public String searchRedirect(ModelMap model) {
		List<String> occures = new ArrayList<String>();
		for(Occur o : Occur.values()){
			occures.add(o.toString());
		}
		model.addAttribute("occures", occures);
		model.addAttribute("searchTypes", SearchType.getMessages());
		return "search";
	}
	
	@RequestMapping("/simpleSearch")
	public String simpleSearchRedirect(ModelMap model) {
		List<String> occures = new ArrayList<String>();
		for(Occur o : Occur.values()){
			occures.add(o.toString());
		}
		model.addAttribute("occures", occures);
		model.addAttribute("searchTypes", SearchType.getMessages());
		return "simpleSearch";
	}
	
	@RequestMapping(value = "multisearch", method = RequestMethod.POST)
	public String multiSearch(HttpServletRequest request,
			ModelMap model, Authentication authentication) {
		
		String text = request.getParameter("text");
		String textst = request.getParameter("textst");
		SearchType.Type textSearchType = SearchType.getType(textst);
		String textsc = request.getParameter("textsc");
		Occur textOccur = getOccur(textsc);
		
		String kw = request.getParameter("kw");
		String kwst = request.getParameter("kwst");
		SearchType.Type kwSearchType = SearchType.getType(kwst);
		String kwsc = request.getParameter("kwsc");
		Occur kwOccur = getOccur(kwsc);
		
		String ct = request.getParameter("ct");
		String ctst = request.getParameter("ctst");
		SearchType.Type ctSearchType = SearchType.getType(ctst);
		String ctsc = request.getParameter("ctsc");
		Occur ctOccur = getOccur(ctsc);
		
		String title = request.getParameter("title");
		String titlest = request.getParameter("titlest");
		SearchType.Type titleSearchType = SearchType.getType(titlest);
		String titlesc = request.getParameter("titlesc");
		Occur titleOccur = getOccur(titlesc);
		
		String author = request.getParameter("author");
		String authorst = request.getParameter("authorst");
		SearchType.Type authorSearchType = SearchType.getType(authorst);
		String authorsc = request.getParameter("authorsc");
		Occur authorOccur = getOccur(authorsc);
		
		String abstractOfFile = request.getParameter("abstractOfFile");
		String abstractst = request.getParameter("abstractst");
		SearchType.Type abstractSearchType = SearchType.getType(abstractst);
		String abstractsc = request.getParameter("abstractsc");
		Occur abstractOccur = getOccur(abstractsc);
		
		String publishDate = request.getParameter("publishDate");
		String publishDatest = request.getParameter("publishDatest");
		SearchType.Type publishDateSearchType = SearchType.getType(publishDatest);
		String publishDatesc = request.getParameter("publishDatesc");
		Occur publishDateOccur = getOccur(publishDatesc);
		
		try {
			BooleanQuery bquery = new BooleanQuery();
			List<RequiredHighlight> rhs = new ArrayList<RequiredHighlight>();
			RequiredHighlight titlerh = null;
			RequiredHighlight textrh = null;
			RequiredHighlight kwrh = null;
			RequiredHighlight ctrh = null;
			RequiredHighlight authorrh = null;
			RequiredHighlight abstractrh = null;
			RequiredHighlight publishDaterh = null;
			
			if(!(title == null || title.equals(""))){
				Query query = QueryBuilder.buildQuery(titleSearchType, "title", title);
				bquery.add(query, titleOccur);
				titlerh = new RequiredHighlight("title", title, null);
				rhs.add(titlerh);
			}
			
			if(!(kw == null || kw.equals(""))){
				Query query = QueryBuilder.buildQuery(kwSearchType, "keywords", kw);
				bquery.add(query, kwOccur);
				kwrh = new RequiredHighlight("keywords", kw, null);
				rhs.add(kwrh);
			}
			
			if(!(ct == null || ct.equals(""))){
				Query query = QueryBuilder.buildQuery(ctSearchType, "categories", ct);
				bquery.add(query, ctOccur);
				ctrh = new RequiredHighlight("categories", ct, null);
				rhs.add(ctrh);
			}
			
			if(!(text == null || text.equals(""))){
				Query query = QueryBuilder.buildQuery(textSearchType, "text", text);
				bquery.add(query, textOccur);
				textrh = new RequiredHighlight("text", text, null);
				rhs.add(textrh);
			}
			
			if(!(author == null || author.equals(""))){
				Query query = QueryBuilder.buildQuery(authorSearchType, "author", author);
				bquery.add(query, authorOccur);
				authorrh = new RequiredHighlight("author", author, null);
				rhs.add(authorrh);
			}
			
			if(!(abstractOfFile == null || abstractOfFile.equals(""))){
				Query query = QueryBuilder.buildQuery(abstractSearchType, "abstract", abstractOfFile);
				bquery.add(query, abstractOccur);
				abstractrh = new RequiredHighlight("abstract", abstractOfFile, null);
				rhs.add(abstractrh);
			}
			
			if(!(publishDate == null || publishDate.equals(""))){
				Query query = QueryBuilder.buildQuery(publishDateSearchType, "publishTime", publishDate);
				bquery.add(query, publishDateOccur);
				authorrh = new RequiredHighlight("publishTime", publishDate, null);
				rhs.add(publishDaterh);
			}
			
			if(authentication == null){
				long currentTime = System.currentTimeMillis();
				long oneDay = 1000L*60*60*24;
				// kao ne starije od sedam dana
				String lowerDate = DateTools.dateToString(new Date(currentTime - 7*oneDay), Resolution.SECOND);
				String upperDate = DateTools.dateToString(new Date(currentTime), Resolution.SECOND);
				Query query = QueryBuilder.buildQuery(Type.range, "publishTime",
						lowerDate + " " + upperDate);
				bquery.add(query, Occur.MUST);
			} else {
				CurrentUser user = (CurrentUser) authentication
						.getPrincipal();
				BooleanQuery journalistQuery = new BooleanQuery();
				if(user.getRole().equals(Role.JOURNALIST)){
					String email = user.getUser().getEmail();
					int indexOfAt = email.indexOf("@");
					int indexOfDot = email.indexOf(".");
					String firstPart = email.substring(0, indexOfAt);
					String secondPart = email.substring(indexOfAt + 1, indexOfDot);
					String thirdPart = email.substring(indexOfDot + 1, email.length());
					String queryMail = firstPart + "AT" + secondPart + "DOT" + thirdPart;
					Query query = QueryBuilder.buildQuery(Type.regular, "author",
							queryMail);
					journalistQuery.add(query, Occur.SHOULD);
				}
				long currentTime = System.currentTimeMillis();
				long oneDay = 1000L*60*60*24;
				String lowerDate = DateTools.dateToString(new Date(currentTime - 7*oneDay), Resolution.SECOND);
				String upperDate = DateTools.dateToString(new Date(currentTime), Resolution.SECOND);
				Query query = QueryBuilder.buildQuery(Type.range, "publishTime",
						lowerDate + " " + upperDate);
				journalistQuery.add(query, Occur.SHOULD);	
				bquery.add(journalistQuery, Occur.MUST);
			}
			
			List<RequiredHighlight> suggestions = new ArrayList<RequiredHighlight>();
			if(titlerh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(titlerh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "multisearch?title="+rhl.getValue()+"&titlesc="+titlesc+"&titlest="+titlest
							+"&kw="+kw+"&kwst="+kwst+"&kwsc="+kwsc
							+"&text="+text+"&textst="+textst+"&textsc="+textsc+"&author="+author+"&authorst="+authorst+
							"&authorsc="+authorsc+"&abstract="+abstractOfFile+"&abstractst="+abstractst+
							"&abstractsc="+abstractsc+"&ct="+ct+"&ctst="+ctst+
							"&ctsc="+ctsc;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(authorrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(authorrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "multisearch?title="+title+"&titlesc="+titlesc+"&titlest="+titlest
							+"&kw="+kw+"&kwst="+kwst+"&kwsc="+kwsc
							+"&text="+text+"&textst="+textst+"&textsc="+textsc+"&author="+rhl.getValue()+"&authorst="+authorst+
							"&authorsc="+authorsc+"&abstract="+abstractOfFile+"&abstractst="+abstractst+
							"&abstractsc="+abstractsc+"&ct="+ct+"&ctst="+ctst+
							"&ctsc="+ctsc;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(abstractrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(abstractrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "multisearch?title="+title+"&titlesc="+titlesc+"&titlest="+titlest
							+"&kw="+kw+"&kwst="+kwst+"&kwsc="+kwsc
							+"&text="+text+"&textst="+textst+"&textsc="+textsc+"&author="+author+"&authorst="+authorst+
							"&authorsc="+authorsc+"&abstract="+rhl.getValue()+"&abstractst="+abstractst+
							"&abstractsc="+abstractsc+"&ct="+ct+"&ctst="+ctst+
							"&ctsc="+ctsc;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(ctrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(ctrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "multisearch?title="+title+"&titlesc="+titlesc+"&titlest="+titlest
							+"&kw="+kw+"&kwst="+kwst+"&kwsc="+kwsc
							+"&text="+text+"&textst="+textst+"&textsc="+textsc+"&author="+author+"&authorst="+authorst+
							"&authorsc="+authorsc+"&abstract="+abstractOfFile+"&abstractst="+abstractst+
							"&abstractsc="+abstractsc+"&ct="+rhl.getValue()+"&ctst="+ctst+
							"&ctsc="+ctsc;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(kwrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(kwrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "multisearch?title="+title+"&titlesc="+titlesc+"&titlest="+titlest
							+"&kw="+rhl.getValue()+"&kwst="+kwst+"&kwsc="+kwsc
							+"&text="+text+"&textst="+textst+"&textsc="+textsc+"&author="+author+"&authorst="+authorst+
							"&authorsc="+authorsc+"&abstract="+abstractOfFile+"&abstractst="+abstractst+
							"&abstractsc="+abstractsc+"&ct="+ct+"&ctst="+ctst+
							"&ctsc="+ctsc;
					rhl.setSearchLink(searchLink);
				}
				suggestions.addAll(list);
			}
			if(textrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(textrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "multisearch?title="+title+"&titlesc="+titlesc+"&titlest="+titlest
							+"&kw="+kw+"&kwst="+kwst+"&kwsc="+kwsc
							+"&text="+rhl.getValue()+"&textst="+textst+"&textsc="+textsc+"&author="+author+"&authorst="+authorst+
							"&authorsc="+authorsc+"&abstract="+abstractOfFile+"&abstractst="+abstractst+
							"&abstractsc="+abstractsc+"&ct="+ct+"&ctst="+ctst+
							"&ctsc="+ctsc;
					rhl.setSearchLink(searchLink);
				}
				suggestions.addAll(list);
			}
			
			/*
			//moze i ovako, ali je malo veci problem napraviti link
			for(RequiredHighlight rhl : rhs){
				suggestions.addAll(InformationRetriever.getSuggestions(rhl.getTerm()));
			}
			*/
			
			if(suggestions.size()==0){
				suggestions = null;
			}
			
			model.addAttribute("suggest", suggestions);
			model.addAttribute("data", InformationRetriever.getData(bquery, rhs, null));
			return "results";
		} catch (IllegalArgumentException e) {
			return "redirect:" + HOME_URL + "/document/search?QueryError";
		} catch (ParseException e) {
			return "redirect:" + HOME_URL + "/document/search?QueryError";
		}
	}
	
	@RequestMapping(value = "singlesearch", method = RequestMethod.POST)
	public String singleSearch(HttpServletRequest request,
			ModelMap model, Authentication authentication) {
		
		String text = request.getParameter("text");
		String textst = request.getParameter("textst");
		SearchType.Type textSearchType = SearchType.getType(textst);
		String textsc = request.getParameter("textsc");
		Occur textOccur = getOccur("*");
		
		try {
			BooleanQuery bquery = new BooleanQuery();
			List<RequiredHighlight> rhs = new ArrayList<RequiredHighlight>();
			RequiredHighlight textrh = null;
			RequiredHighlight titlerh = null;
			RequiredHighlight kwrh = null;
			RequiredHighlight ctrh = null;
			RequiredHighlight tgrh = null;
			RequiredHighlight authorrh = null;
			RequiredHighlight abstractrh = null;
			
			if(!(text == null || text.equals(""))){
				Query queryText = QueryBuilder.buildQuery(textSearchType, "text", text);
				bquery.add(queryText, textOccur);
				Query queryTitle = QueryBuilder.buildQuery(textSearchType, "title", text);
				bquery.add(queryTitle, textOccur);
				Query queryKeywords = QueryBuilder.buildQuery(textSearchType, "keywords", text);
				bquery.add(queryKeywords, textOccur);
				Query queryCategories = QueryBuilder.buildQuery(textSearchType, "categories", text);
				bquery.add(queryCategories, textOccur);
				Query queryTags = QueryBuilder.buildQuery(textSearchType, "tags", text);
				bquery.add(queryTags, textOccur);
				Query queryAuthor = QueryBuilder.buildQuery(textSearchType, "author", text);
				bquery.add(queryAuthor, textOccur);
				Query queryAbstract = QueryBuilder.buildQuery(textSearchType, "abstract", text);
				bquery.add(queryAbstract, textOccur);
				textrh = new RequiredHighlight("text", text, null);
				rhs.add(textrh);
				titlerh = new RequiredHighlight("title", text, null);
				rhs.add(titlerh);
				authorrh = new RequiredHighlight("author", text, null);
				rhs.add(authorrh);
				kwrh = new RequiredHighlight("keywords", text, null);
				rhs.add(kwrh);
				ctrh = new RequiredHighlight("categories", text, null);
				rhs.add(ctrh);
				tgrh = new RequiredHighlight("tags", text, null);
				rhs.add(tgrh);
				abstractrh = new RequiredHighlight("abstract", text, null);
				rhs.add(abstractrh);
			}
			BooleanQuery finalQuery = new BooleanQuery();
			finalQuery.add(bquery, Occur.MUST);
			
			if(authentication == null){
				long currentTime = System.currentTimeMillis();
				long oneDay = 1000L*60*60*24;
				String lowerDate = DateTools.dateToString(new Date(currentTime - 7*oneDay), Resolution.SECOND);
				String upperDate = DateTools.dateToString(new Date(currentTime), Resolution.SECOND);
				Query query = QueryBuilder.buildQuery(Type.range, "publishTime",
						lowerDate + " " + upperDate);
				finalQuery.add(query, Occur.MUST);
			} else {
				CurrentUser user = (CurrentUser) authentication
						.getPrincipal();
				BooleanQuery journalistQuery = new BooleanQuery();
				if(user.getRole().equals(Role.JOURNALIST)){
					String email = user.getUser().getEmail();
					int indexOfAt = email.indexOf("@");
					int indexOfDot = email.indexOf(".");
					String firstPart = email.substring(0, indexOfAt);
					String secondPart = email.substring(indexOfAt + 1, indexOfDot);
					String thirdPart = email.substring(indexOfDot + 1, email.length());
					String queryMail = firstPart + "AT" + secondPart + "DOT" + thirdPart;
					Query query = QueryBuilder.buildQuery(Type.regular, "author",
							queryMail);
					journalistQuery.add(query, Occur.SHOULD);
				}
				long currentTime = System.currentTimeMillis();
				long oneDay = 1000L*60*60*24;
				String lowerDate = DateTools.dateToString(new Date(currentTime - 7*oneDay), Resolution.SECOND);
				String upperDate = DateTools.dateToString(new Date(currentTime), Resolution.SECOND);
				Query query = QueryBuilder.buildQuery(Type.range, "publishTime",
						lowerDate + " " + upperDate);
				journalistQuery.add(query, Occur.SHOULD);	
				finalQuery.add(journalistQuery, Occur.MUST);
			}
			
			List<RequiredHighlight> suggestions = new ArrayList<RequiredHighlight>();
			if(titlerh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(titlerh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "singlesearch?text="+rhl.getValue()+"&textsc="+textsc+"&textst="+textst;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(authorrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(authorrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "singlesearch?text="+rhl.getValue()+"&textsc="+textsc+"&textst="+textst;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(abstractrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(abstractrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "singlesearch?text="+rhl.getValue()+"&textsc="+textsc+"&textst="+textst;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(ctrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(ctrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "singlesearch?text="+rhl.getValue()+"&textsc="+textsc+"&textst="+textst;
					rhl.setSearchLink(searchLink);
				}
				
				suggestions.addAll(list);
			}
			if(kwrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(kwrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "singlesearch?text="+rhl.getValue()+"&textsc="+textsc+"&textst="+textst;
					rhl.setSearchLink(searchLink);
				}
				suggestions.addAll(list);
			}
			if(textrh != null){
				List<RequiredHighlight> list = InformationRetriever.getSuggestions(textrh.getTerm());
				String searchLink = "";
				for(RequiredHighlight rhl : list){
					searchLink = "singlesearch?text="+rhl.getValue()+"&textsc="+textsc+"&textst="+textst;
					rhl.setSearchLink(searchLink);
				}
				suggestions.addAll(list);
			}
			
			/*
			//moze i ovako, ali je malo veci problem napraviti link
			for(RequiredHighlight rhl : rhs){
				suggestions.addAll(InformationRetriever.getSuggestions(rhl.getTerm()));
			}
			*/
			
			if(suggestions.size()==0){
				suggestions = null;
			}
			
			model.addAttribute("suggest", suggestions);
			model.addAttribute("data", InformationRetriever.getData(finalQuery, rhs, null));
			return "results";
		} catch (IllegalArgumentException e) {
			return "redirect:" + HOME_URL + "/finder/simpleSearch?QueryError";
		} catch (ParseException e) {
			return "redirect:" + HOME_URL + "/finder/simpleSearch?QueryError";
		}
	}
	
	@RequestMapping(value = "moreLikeThis", method = RequestMethod.GET)
	public String getMoreLikeThis(HttpServletRequest request,
			ModelMap model) {
		String fileName = request.getParameter("file");
		
		model.addAttribute("suggest", null);
		model.addAttribute("data", InformationRetriever.getMoreLikeThis(fileName));
		return "results";
	}

	private Occur getOccur(String value){
		if(value.equals("+")){
			return Occur.MUST;
		}else if(value.equals("-")){
			return Occur.MUST_NOT;
		}else{
			return Occur.SHOULD;
		}
	}
}
