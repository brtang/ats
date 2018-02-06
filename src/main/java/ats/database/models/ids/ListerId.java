package ats.database.models.ids;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import ats.database.models.Company;


public class ListerId implements Serializable{

	private String username;
	private Company companyName;
}
