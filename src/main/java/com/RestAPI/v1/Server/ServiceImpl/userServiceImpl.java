package com.RestAPI.v1.Server.ServiceImpl;

import com.RestAPI.v1.Server.Entities.passwordVerificationTokenEntity;
import com.RestAPI.v1.Server.Entities.userEntity;
import com.RestAPI.v1.Server.Entities.verificationTokenEntity;
import com.RestAPI.v1.Server.IService.IuserService;
import com.RestAPI.v1.Server.Models.genericObject;
import com.RestAPI.v1.Server.Models.passwordModel;
import com.RestAPI.v1.Server.Models.verificationTypeEnum;
import com.RestAPI.v1.Server.Repositories.IpasswordVerificationTokenRepo;
import com.RestAPI.v1.Server.Repositories.IuserModelRepository;
import com.RestAPI.v1.Server.Repositories.IverificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class userServiceImpl implements IuserService  {
    @Autowired
    private IuserModelRepository userRepo;

    @Autowired
    private IverificationTokenRepository verifyTokenRepo;
    @Autowired
    private IpasswordVerificationTokenRepo passwordTokenrepo;
    @Autowired
    private PasswordEncoder encoder;





    @Override
    public userEntity register(userEntity user, String avatarUrl) {
        if( userRepo.findByEmail(user.getEmail()) != null) {return  null;} ;
        user.setPassword(encoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        user.setDateOfCreation(new Date());
        user.setAvatar(avatarUrl);
        user.setUserRole("User");

        return userRepo.save(user);
    }

    @Override
    public Boolean validateToken(String token, verificationTypeEnum reqType) {
        switch (reqType) {
            case RESEND_VERIFICATION_EMAIL: {
                verificationTokenEntity vToken = verifyTokenRepo.findByToken(token);
                if (vToken == null) return false;
                userEntity user = vToken.getUser();
                Calendar cal = Calendar.getInstance();

                if(user.getIsVerified()) return false;

                if (vToken.getTimeTillExp().getTime() - cal.getTime().getTime() <= 0) verifyTokenRepo.delete(vToken);

                user.setIsVerified(true);
                userRepo.save(user);
                return true;
            }
            case PASSWORD_RESET:{
                passwordVerificationTokenEntity vToken =  passwordTokenrepo.findByToken(token);
                if (vToken == null) return false;
                userEntity user = vToken.getUser();
                Calendar cal = Calendar.getInstance();

                if (vToken.getTimeTillExp().getTime() - cal.getTime().getTime() <= 0) passwordTokenrepo.delete(vToken);

                userRepo.save(user);
                return true;
            }
        }

        return false;

    }

    @Override
    public void saveVerificationToken(userEntity user, String token, verificationTypeEnum reqType) {
        switch (reqType) {
            case RESEND_VERIFICATION_EMAIL: {
                verificationTokenEntity vToken = new verificationTokenEntity(user, token);
                verifyTokenRepo.save(vToken);
                break;
            }
            case PASSWORD_RESET: {
                passwordVerificationTokenEntity vToken = new passwordVerificationTokenEntity(user, token);
                passwordTokenrepo.save(vToken);
                break;
            }
        }
    }

    @Override
    public verificationTokenEntity generationNewVerificationToken(String email) {
        userEntity model = userRepo.findByEmail(email);
        if (model == null) return null;
       Optional<verificationTokenEntity> vToken = verifyTokenRepo.findById(model.getId());
        if (vToken.isPresent()) {
            verificationTokenEntity token = vToken.get();
            token.setToken(UUID.randomUUID().toString());
            verifyTokenRepo.save(token);
            return token;
        }
        return null;
    }

    @Override
    public genericObject<userEntity, String> resetPasswordToken(String email) {
        userEntity user = userRepo.findByEmail(email);
        if (user == null) return null;
        Optional<passwordVerificationTokenEntity> token = passwordTokenrepo.findByUser(user);
        if (token.isEmpty()) {
            log.info("The user was empty");
            var newToken = new passwordVerificationTokenEntity(user, UUID.randomUUID().toString());
            passwordTokenrepo.save(newToken);
            return new genericObject<>(user,newToken.getToken());

        }
        log.info(user.toString());
        var pToken = token.get();
        pToken.setToken(UUID.randomUUID().toString());
        passwordTokenrepo.save(pToken);
        return new genericObject<userEntity, String>(user, pToken.getToken());
    }

    @Override
    public String updateUserPassword(passwordModel password) {
        userEntity user = userRepo.findByEmail(password.getEmail());
        if (user ==  null) return null;
        if(encoder.matches(password.getOldPassword(), user.getPassword())) {
            user.setPassword(encoder.encode(password.getNewPassword()));
            userRepo.save(user);
            return "Password Has been Changed";
        }
        return null;
    }

    @Override
    public Boolean resetPassword(String email, String password, String token) {
        userEntity userBody = userRepo.findByEmail(email);
        passwordVerificationTokenEntity userToken = passwordTokenrepo.findByToken(token);
        if(userToken == null) return false;
        if (userBody == userToken.getUser()) {
            passwordTokenrepo.deleteById(userToken.getId());
            userBody.setPassword(encoder.encode(password));
            userRepo.save(userBody);
            return true;
        }
        return false;

    }

}
