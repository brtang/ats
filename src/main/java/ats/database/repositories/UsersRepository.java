package ats.database.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ats.database.models.User;

public interface UsersRepository extends CrudRepository<User, Serializable>{
	User findByEmailAndUsername(String email, String username);
	
	@Query("select count(u) > 0 from User u where u.username = :username or u.email = :email")
	boolean checkUsernameOrEmailExists(@Param("username") String username, @Param("email") String email);

}
