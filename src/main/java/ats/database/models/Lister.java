package ats.database.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Lister")
public class Lister implements Serializable{
	
	@Id
	@Column(name = "email")
	private String email;
		
	// Specifies the User table does not contain a company column, but
	// a company_name column with a foreign key on the @Id attribute of Company and creates a join to lazily fetch the company.
	// Lazy fetching allows the fetching of a relationship to be deferred until it is accessed. 
	// Important to avoid unnecessary database accesses and avoid cost of building the objects if they are not needed
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="company_name")
	private Company company;
	
//	@OneToMany(fetch=FetchType.EAGER)
//	@JoinColumn(name= "id")
//	private List<Listing> listings = new ArrayList<Listing>();
//	
//	public List<Listing> getListings() {
//		return listings;
//	}
//
//	public void setListings(List<Listing> listings) {
//		this.listings = listings;
//	}

	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name="canList")
	private boolean canList = true;
	
	public Lister() {
		
	}
	
	public Lister(Company company, String email) {
		this.company = company;
		this.email = email;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isCanList() {
		return canList;
	}

	public void setCanList(boolean canList) {
		this.canList = canList;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}

