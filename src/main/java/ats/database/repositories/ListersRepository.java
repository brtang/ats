package ats.database.repositories;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import ats.database.models.Lister;

public interface ListersRepository extends CrudRepository<Lister, Serializable>{
	Lister findByEmail(String email);
	
}
