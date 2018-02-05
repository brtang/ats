package ats.database.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
//	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//	private List<Lister> listers;
//	
//	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//	private List<Listing> listings;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "zipCode")
	private int zipCode;
	
	@Column(name = "createCode")
	private String createCode;
		
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}
	public String getCreateCode() {
		return createCode;
	}
	public void setCreateCode(String createCode) {
		this.createCode = createCode;
	}
	
}
