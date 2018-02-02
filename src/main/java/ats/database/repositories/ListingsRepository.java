package ats.database.repositories;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import ats.database.models.Listing;

public interface ListingsRepository extends CrudRepository<Listing, Serializable> {
	Listing findById(int id);
	
	
}
