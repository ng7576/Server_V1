package com.RestAPI.v1.Server.IService;

import com.RestAPI.v1.Server.Entities.userEntity;
import com.RestAPI.v1.Server.Entities.verificationTokenEntity;
import com.RestAPI.v1.Server.Models.genericObject;
import com.RestAPI.v1.Server.Models.passwordModel;
import com.RestAPI.v1.Server.Models.verificationTypeEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface IuserService {
    userEntity register(userEntity user, String avatarUrl);

    Boolean validateToken(String token, verificationTypeEnum reqType);

    void saveVerificationToken(userEntity user, String token,verificationTypeEnum reqType);


    verificationTokenEntity generationNewVerificationToken(String email);

    genericObject<userEntity, String> resetPasswordToken(String email);

    String updateUserPassword(passwordModel password);

    Boolean resetPassword(String email, String password, String token);


}
