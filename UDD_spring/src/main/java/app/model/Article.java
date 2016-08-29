package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "article")
public class Article implements Serializable{

	private static final long serialVersionUID = -688651777291247293L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String title;

	@NotNull
	private String abstractOfArticle;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER, mappedBy = "article")
	private Set<ArticleCategory> articleCategories;
	
	/*@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "category", 
	             joinColumns = { @JoinColumn(name = "id") }, 
	             inverseJoinColumns = { @JoinColumn(name = "id") })
	private Set<Category> categories = new HashSet<Category>();*/
	
	@ManyToOne
	@JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
	private User user;
	
	@NotNull
	private String keywords;
	
	@NotNull
	private String tags;
	
	@NotNull
	private String author;
	
	@NotNull
	private Date addTime;
	
	@NotNull
	private Date publishTime;
	
	@NotNull
	private String editor;

	public Article() {
		super();
	}

	public Article(Long id, String title, String abstractOfArticle,
			HashSet<ArticleCategory> articleCategories, String keywords,
			String tags, String author, Date addTime,
			Date publishTime, String editor, User user) {
		super();
		this.id = id;
		this.title = title;
		this.abstractOfArticle = abstractOfArticle;
		this.articleCategories = articleCategories;
		this.keywords = keywords;
		this.tags = tags;
		this.author = author;
		this.addTime = addTime;
		this.publishTime = publishTime;
		this.editor = editor;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	@Override
	public String toString() {
		return "NewspaperArticle [id=" + id + ", title=" + title
				+ ", abstract_=" + abstractOfArticle + ", text="  +
				 ", keywords=" + keywords + ", tags=" + tags
				+ ", author=" + author + ", entryTime=" + addTime
				+ ", publishTime=" + publishTime + ", editor=" + editor + "]";
	}

	public String getAbstractOfArticle() {
		return abstractOfArticle;
	}

	public void setAbstractOfArticle(String abstractOfArticle) {
		this.abstractOfArticle = abstractOfArticle;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<ArticleCategory> getArticleCategories() {
		return articleCategories;
	}

	public void setArticleCategories(Set<ArticleCategory> articleCategories) {
		this.articleCategories = articleCategories;
	}
	
	
}
