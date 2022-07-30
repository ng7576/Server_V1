package com.RestAPI.v1.Server.Controllers;


import com.RestAPI.v1.Server.Controllers.Events.onRegistrationCompleteEvent;

import com.RestAPI.v1.Server.Entities.userEntity;
import com.RestAPI.v1.Server.Entities.verificationTokenEntity;
import com.RestAPI.v1.Server.IService.IuploadService;
import com.RestAPI.v1.Server.IService.IuserService;
import com.RestAPI.v1.Server.Models.*;
import com.RestAPI.v1.Server.ServiceImpl.customUserDetailsService;
import com.RestAPI.v1.Server.Utils.JWTUtils.JWTUtility;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.http.HttpResponse;


@RestController
@Slf4j

public class userController {


    @Autowired
    private IuserService userService;

    @Autowired
    private customUserDetailsService customService;

    @Autowired
    private IuploadService uploadService;

    @Autowired
    private AuthenticationManager authenticationManager;



    @Autowired
    private JWTUtility jwtUtility;


    @Autowired
    private ApplicationEventPublisher publisher;

    private final Logger LOGGER = LoggerFactory.getLogger(userController.class);


    //Utility Methods

    private String appUrl(HttpServletRequest req) {
        return "http://" + req.getServerName() + ":" +  req.getServerPort() + req.getContextPath();
    }
    //GENERATE VERIFICATION EMAIL AFTER CREATION
    @GetMapping("/verifyRegistration")
    public responseModel<Boolean> verifyRegistration (@RequestParam("token") String token) {
        Boolean state = userService.validateToken(token, verificationTypeEnum.RESEND_VERIFICATION_EMAIL);
        responseModel<Boolean> res = new responseModel<>();
        if (state) {
            res.setStatus(HttpStatus.ACCEPTED);
            res.setMessage("The Email has been verified");
            res.setResponseObj(true);
        } else {
            res.setStatus(HttpStatus.BAD_REQUEST);
            res.setMessage("The Email cannot be verified or is already verified");
            res.setResponseObj(false);
        }
        return res;
    }

    @GetMapping("/resetPasswordUrl")
    public responseModel<String> resetPasswordVerification(@RequestParam("token") String token, @RequestBody  resetPasswordModel model) {
        Boolean state = userService.validateToken(token, verificationTypeEnum.PASSWORD_RESET);
        Boolean passwordResetState = userService.resetPassword(model.getEmail(), model.getPassword(),token);
        if(state && passwordResetState) {

            return new responseModel<>("Password Reset verified",HttpStatus.ACCEPTED, model.getEmail());
        }
        return new responseModel<>("Could not verify token or User Not found",HttpStatus.ACCEPTED, model.getEmail());


    }
    //RESEND VERIFICATION TOKEN
    @GetMapping("/resendVerificationToken")
    public responseModel<String> resendVerificationToken(@RequestBody emailModel email, HttpServletRequest req) {
        log.info(String.valueOf(email));
        verificationTokenEntity vToken = userService.generationNewVerificationToken(email.getEmail());
        responseModel<String> responseObj = new responseModel<>();
        if (vToken != null) {
            userEntity user = vToken.getUser();
            resendVerificationTokenHelper(user, appUrl(req), vToken.getToken(), verificationTypeEnum.RESEND_VERIFICATION_EMAIL);
            responseObj.setStatus(HttpStatus.ACCEPTED);
            responseObj.setMessage("The verification email has been sent");
            responseObj.setResponseObj("Provided email: " + email.getEmail());
            return responseObj;

        }

        responseObj.setStatus(HttpStatus.BAD_REQUEST);
        responseObj.setMessage("The email does not exist");
        return responseObj;
    }

    @GetMapping("/resetPassword")
    public responseModel<String> resetPassword(@RequestBody emailModel email, HttpServletRequest req) {
        log.info(email.getEmail());
        genericObject<userEntity, String>  object= userService.resetPasswordToken(email.getEmail());

        if (object == null) {
           return new responseModel<String>("The user cannot be found for provided email",
                   HttpStatus.BAD_REQUEST,
                   email.getEmail());
        }
        resendVerificationTokenHelper(object.getType1(), appUrl(req), object.getType2(),verificationTypeEnum.PASSWORD_RESET);
        return new responseModel<String>("The user reset email has been sent",
                HttpStatus.ACCEPTED,
                email.getEmail());


    }

    //CHANGE PASSWORD
    @PostMapping("/user/changePassword")
    public responseModel<String> changePassword(@RequestBody passwordModel password) {
        String res = userService.updateUserPassword(password);
        if(res == null) return new responseModel<>("The email or Old password do not match", HttpStatus.BAD_REQUEST, null);

        return new responseModel<>("The password has been successfully changed", HttpStatus.ACCEPTED, password.getEmail());



    }

    private void resendVerificationTokenHelper(userEntity user, String appUrl, String token, verificationTypeEnum reqType) {
        switch (reqType) {
            case RESEND_VERIFICATION_EMAIL: {
                String url = appUrl + "/verifyRegistration?token=" + token;
                log.info(url);
                break;}
            case  PASSWORD_RESET: {
                String url = appUrl + "/resetPasswordUrl?token=" + token;
                log.info(url);
                break;
            }
        }


    }


    //GET REQUESTS
    @GetMapping("/test")
    public responseModel<String> test() {
        responseModel<String> res = new responseModel<>();
        res.setMessage("This is a test case");
        res.setStatus(HttpStatus.ACCEPTED);
        res.setResponseObj("This is a response object");
        return res;

    }

    @PostMapping("/register")
    public responseModel<String> registerUser(@Valid @RequestBody userEntity user, final HttpServletRequest req) {
        String avatarUrl = uploadService.dummyFileUpload(user.getAvatar());
        userEntity model = userService.register(user, avatarUrl);
        responseModel<String> res = new responseModel<>();
        if (model == null) {
            res.setStatus(HttpStatus.CONFLICT);
            res.setMessage("The Email is already taken");
            return res;
        }
        publisher.publishEvent(new onRegistrationCompleteEvent(user, appUrl(req),verificationTypeEnum.RESEND_VERIFICATION_EMAIL));

        res.setStatus(HttpStatus.CREATED);
        res.setMessage("User Has bee created");
        res.setResponseObj(user.getEmail());

        return res;

    }

    //LOGIN
    @PostMapping("/login")
    public responseModel<String>  userLogin(@RequestBody loginModel model) throws Exception {
        try {
            log.info("try block in login:: "+ model.getUserEmail());
            log.info(String.valueOf(new UsernamePasswordAuthenticationToken(
                    model.getUserEmail(),
                    model.getPassword()
            )));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            model.getUserEmail(),
                            model.getPassword()
                    )

            );
            log.info("end of try block in login");

        } catch (BadCredentialsException e) {
            log.info("Exception was thrown in Login");
            log.info(String.valueOf(e));
            log.info(String.valueOf( new UsernamePasswordAuthenticationToken(
                    model.getUserEmail(),
                    model.getPassword()
            )));
            throw new Exception("INVALID LOGIN", e);
        }

        final UserDetails userDetails = customService.loadUserByUsername(model.getUserEmail());

        final String token = jwtUtility.generateToken(userDetails);

        return new responseModel<>("",HttpStatus.ACCEPTED, token);
    }

    //DELETE USER
    //REGISTERED ROUTE
    @DeleteMapping("/user/{email}")
    public responseModel<String> deletUserById(@RequestParam("email") emailModel email) {
        return new responseModel<String>();
    }



}
