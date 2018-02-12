package ats.database.repositories;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import ats.database.models.Application;

public interface ApplicationRepository extends CrudRepository<Application, Serializable> {

}
