package ats.database.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ats.database.models.Listing;

public interface ListingsRepository extends CrudRepository<Listing, Serializable> {
	Listing findById(Integer id);
	
	@Query("select l from Listing l where l.lister.username = :username and l.lister.companyName.companyName = :companyName ")
	List<Listing> findByListerUsernameAndCompanyName(@Param("username") String username, @Param("companyName") String companyName);
}
