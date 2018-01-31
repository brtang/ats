package ats.database.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ats.database.models.ids.ListerId;

@Entity
@Table(name = "Lister")
public class Lister {
	
	@EmbeddedId
	public ListerId listerId;
	
	// Specifies the User table does not contain a company column, but
	// a company_name column with a foreign key on the @Id attribute of Company and creates a join to lazily fetch the company.
	// Lazy fetching allows the fetching of a relationship to be deferred until it is accessed. 
	// Important to avoid unnecessary database accesses and avoid cost of building the objects if they are not needed
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="company_name")
	private Company company;
	
	@OneToMany(mappedBy="lister")
	private List<Listing> listings;
	
	public Lister() {
		
	}
	
	public ListerId getListerId() {
		return listerId;
	}

	public void setListerId(ListerId listerId) {
		this.listerId = listerId;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	
	
}

//@Embeddable
//class ListerId implements Serializable{
//	
//	@Column(name = "firstName")
//	private String firstName;
//	
//	@Column(name = "lastName")
//	private String lastName;
//	
//	@Column(name = "email")
//	private String email;
//	
//	public ListerId() {
//		
//	}
//	
//	public ListerId(String firstName, String lastName, String email) {
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.email = email;
//	}
//	
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//	
//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (!(o instanceof ListerId)) return false;
//		ListerId that = (ListerId) o;
//		return Objects.equals(getFirstName(), that.getFirstName()) &&
//				Objects.equals(getLastName(), that.getLastName()) &&
//				Objects.equals(getEmail(), that.getEmail());
//	}
//	
//	@Override
//	public int hashCode() {
//		return Objects.hash(getFirstName(), getLastName(), getEmail());
//	}
//		
//}
