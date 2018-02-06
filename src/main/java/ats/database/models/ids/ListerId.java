package ats.database.models.ids;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import ats.database.models.Company;


public class ListerId implements Serializable{

	private String username;
	private Company companyName;
	
	public ListerId() {
		
	}
	
	public ListerId(String username, Company company) {
		this.username = username;
		this.companyName = company;
	}
	
	public boolean equals(Object object) {
        	if (object instanceof ListerId) {
        		ListerId pk = (ListerId) object;
        		return username.equals(pk.username) && companyName == pk.companyName;
        	} else {
        		return false;
        	}
	}

    public int hashCode() {
        return companyName.hashCode() + username.hashCode();
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Company getCompanyName() {
		return companyName;
	}

	public void setCompanyName(Company companyName) {
		this.companyName = companyName;
	}
    
    
	
}
