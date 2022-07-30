package com.RestAPI.v1.Server.Utils;


import com.RestAPI.v1.Server.Controllers.Events.onRegistrationCompleteEvent;
import com.RestAPI.v1.Server.Entities.userEntity;
import com.RestAPI.v1.Server.IService.IuserService;
import com.RestAPI.v1.Server.Models.verificationTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class registrationCompleteListener implements ApplicationListener<onRegistrationCompleteEvent> {

    @Autowired
    private IuserService userService;

    @Override
    public void onApplicationEvent(onRegistrationCompleteEvent event) {
        userEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        verificationTypeEnum reqType = event.getReqType();
        userService.saveVerificationToken(user, token, reqType);
        String url = event.getAppUrl() + "/verifyRegistration?token=" + token;
        log.info(url);
        //Implement sentRegistrationEmail() here




    }

    public void sendRegistrationEmail() {
        //TBD
    }

}
