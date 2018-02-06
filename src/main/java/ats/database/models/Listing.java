package ats.database.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Listing")
public class Listing implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Integer id;
	
	@Column(name = "post_date")
	private Date post_date = new Date();
	
	@Column(name = "end_date")
	private Date end_date;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "isActive")
	private boolean isActive = true;
		
	@ManyToOne(fetch=FetchType.LAZY)	
//	@JoinColumn(name="username") 
	@JoinColumns({
		@JoinColumn(name = "username", referencedColumnName="username"),
		@JoinColumn(name = "companyName", referencedColumnName="companyName") })
	private Lister lister;
	
	@OneToMany(mappedBy="listing")
	private List<Application> applications;

	public Listing(){
		
	}
	
	ArrayList<String> keyWords = new ArrayList<String>();
	
	
	public ArrayList<String> getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(ArrayList<String> keyWords) {
		this.keyWords = keyWords;
	}

//	public Listing(Company company) {
//		this.company = company;
//	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPost_date() {
		return post_date;
	}

	public void setPost_date(Date post_date) {
		this.post_date = post_date;
	}

//	public Company getCompany() {
//		return company;
//	}
//
//	public void setCompany(Company company) {
//		this.company = company;
//	}

	public Lister getLister() {
		return lister;
	}

	public void setLister(Lister lister) {
		this.lister = lister;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	
}
