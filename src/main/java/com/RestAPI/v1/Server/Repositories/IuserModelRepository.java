package com.RestAPI.v1.Server.Repositories;


import com.RestAPI.v1.Server.Entities.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IuserModelRepository extends JpaRepository<userEntity, String> {


    userEntity findByEmail(String email);
}
