package com.RestAPI.v1.Server.Controllers.Events;

import com.RestAPI.v1.Server.Entities.userEntity;
import com.RestAPI.v1.Server.Models.verificationTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import javax.validation.Valid;


@Getter
@Setter
public class onRegistrationCompleteEvent extends ApplicationEvent {

    private userEntity user;
    private String appUrl;

    private verificationTypeEnum reqType;
    public onRegistrationCompleteEvent(@Valid userEntity user, String appUrl, verificationTypeEnum reqType) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.reqType = reqType;
    }
}
