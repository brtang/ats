package ats.database.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ats.database.models.ids.ListerId;


@Entity
@Table(name = "Lister")
@IdClass(ListerId.class)
public class Lister implements Serializable{
			
	// Specifies the User table does not contain a company column, but
	// a company_name column with a foreign key on the @Id attribute of Company and creates a join to lazily fetch the company.
	// Lazy fetching allows the fetching of a relationship to be deferred until it is accessed. 
	// Important to avoid unnecessary database accesses and avoid cost of building the objects if they are not needed
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="companyName")
	private Company companyName;
	
	@Id
	@Column(name="username")
	private String username;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name="canList")
	private boolean canList = true;
	
	public Lister() {
		
	}
			
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Company getCompany() {
		return companyName;
	}

	public void setCompany(Company company) {
		this.companyName = company;
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

