package ats.database.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Company")
public class Company implements Serializable{
	
	// Looks like this only works if its @Id
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	@Column(name = "id", updatable = false, nullable = false)
//	private Integer id;

	@Id
	@Column(name = "name")
	private String companyName;
	
	// The mappedBy attribute spcifies that the private Company company field in User
	// owns the relationship and contains the foreign key for the query to find all users for a company
	@OneToMany(mappedBy = "company")
	private List<Lister> listers;
	
	@OneToMany(mappedBy = "company")
	private List<Listing> listings;
	
	@Column(name = "numListingsRemaining")
	private int numListingsRemaining = 10;
	
	public Company(){
		
	}
	public Company(String name) {
		this.companyName = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public int getNumListingsRemaining() {
		return numListingsRemaining;
	}
	
	public void setNumListingsRemaining(int numListingsRemaining) {
		this.numListingsRemaining = numListingsRemaining;
	}
	
}
