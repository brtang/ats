package ats.database.models.ids;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;


@Embeddable
public class ListerId implements Serializable{

	private String firstName;
	private String lastName;
	private String email;
	
	public ListerId() {
		
	}
	
	public ListerId(String firstName, String lastName, String email) {
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
		if (!(o instanceof ListerId)) return false;
		ListerId that = (ListerId) o;
		return Objects.equals(getFirstName(), that.getFirstName()) &&
				Objects.equals(getLastName(), that.getLastName()) &&
				Objects.equals(getEmail(), that.getEmail());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getFirstName(), getLastName(), getEmail());
	}
	
}
