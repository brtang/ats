package ats.database.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Application")
public class Application {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Integer id;
	
	@Column(name = "format")
	private String format;
	
	@Column(name = "upload_date")
	private Date upload_date = new Date();
	
	@Column(name = "resumePath")
	private String resumePath;
	
	@Column(name = "coverLetterPath")
	private String coverLetterPath;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="listing")
	private Listing listing;
	
	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="user")
//	@JoinColumns({
//		@JoinColumn(name="user_uname", referencedColumnName="username"),
//		@JoinColumn(name="user_email", referencedColumnName="email")
//	})
	@JoinColumn(name="user_uname", referencedColumnName="username")
	private User user;
	
	public Application() {
		
	}
	
	public Listing getListing() {
		return listing;
	}

	public void setListing(Listing listing) {
		this.listing = listing;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResumePath() {
		return resumePath;
	}

	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}

	public String getCoverLetterPath() {
		return coverLetterPath;
	}

	public void setCoverLetterPath(String coverLetterPath) {
		this.coverLetterPath = coverLetterPath;
	}
	
}
