package ats.database.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ats.database.models.Lister;

public interface ListersRepository extends CrudRepository<Lister, Serializable>{
	Lister findByEmail(String email);
	
	Lister findByUsername(String username);
	
//	@Query("select l from Lister l where l.company.companyName = :companyName and l.username = :username")
//	Lister findByCompanyNameAndUsername(@Param("companyName") String companyName, @Param("username") String username);
//	
}
