package ats.database.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ats.database.models.Application;

public interface ApplicationRepository extends CrudRepository<Application, Serializable> {
	
	@Query("select a from Application a where a.listing.id = :listingId")
	List<Application> findByListingId(@Param("listingId") int listingId);
	
	@Query("select a from Application a where a.user.username = :username")
	Application findByUsername(@Param("username") String username);
}
