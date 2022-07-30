package com.RestAPI.v1.Server.Repositories;

import com.RestAPI.v1.Server.Entities.verificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IverificationTokenRepository extends JpaRepository<verificationTokenEntity, Long> {
    verificationTokenEntity findByToken(String token);
}
