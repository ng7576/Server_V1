package com.RestAPI.v1.Server.Repositories;

import com.RestAPI.v1.Server.Entities.passwordVerificationTokenEntity;
import com.RestAPI.v1.Server.Entities.userEntity;
import com.RestAPI.v1.Server.Entities.verificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IpasswordVerificationTokenRepo extends JpaRepository<passwordVerificationTokenEntity, Long> {
    passwordVerificationTokenEntity findByToken(String token);

    Optional<passwordVerificationTokenEntity> findByUser(userEntity user);
}
