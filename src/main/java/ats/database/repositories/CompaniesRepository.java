package ats.database.repositories;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import ats.database.models.Company;

public interface CompaniesRepository extends CrudRepository<Company, Serializable>{
	
	Company findByCompanyName(String name);
}
