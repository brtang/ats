package ats.database.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ats.database.models.Listing;

public interface ListingsRepository extends CrudRepository<Listing, Serializable> {
	Listing findById(int id);
	
//	List<Listing> findByListerUsernameAndCompanyCompanyName(String username, String companyName);
}
