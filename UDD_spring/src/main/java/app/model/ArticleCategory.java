package app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "article_category")
public class ArticleCategory implements Serializable{
	
	private static final long serialVersionUID = 8364475429433173142L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "article", referencedColumnName = "id", nullable = false)
	private Article article;
	
	@ManyToOne
	@JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
	private Category category;

	public ArticleCategory(Article article, Category category) {
		super();
		this.article = article;
		this.category = category;
	}
	
	public ArticleCategory(){
		super();
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
