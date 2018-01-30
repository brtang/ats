package ats.database.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "User")
public class User implements Serializable{
	
	
	// Looks like this only works if its @Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	@Column(name = "id", updatable = false, nullable = false)
//	private Integer id;
	
	@EmbeddedId
	public UserId userId;
		
	@Column(name = "address")
	private String address;
	
	@Column(name = "phone")
	private Integer phone;
	
	@OneToMany(mappedBy="user")
	private List<Application> applications;
	
	public User() {
		
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	
}

@Embeddable
class UserId implements Serializable{
	
	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	public UserId() {
		
	}
	
	public UserId(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserId)) return false;
		UserId that = (UserId) o;
		return Objects.equals(getFirstName(), that.getFirstName()) &&
				Objects.equals(getLastName(), that.getLastName()) &&
				Objects.equals(getEmail(), that.getEmail());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getFirstName(), getLastName(), getEmail());
	}
	
	
}
