package ats.database.models;

import java.io.Serializable;
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
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="company_name")
	private Company company; 
	
	// Need multiple JoinColumn's because primary key of Lister is a composite key
	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="lister")	
	@JoinColumns({
		@JoinColumn(name="lister_fname", referencedColumnName="firstName"),
		@JoinColumn(name="lister_lname", referencedColumnName="lastName"),
		@JoinColumn(name="lister_email", referencedColumnName="email")
	})
	private Lister lister;
	
	@OneToMany(mappedBy="listing")
	private List<Application> applications;

	public Listing(){
		
	}
	
}
