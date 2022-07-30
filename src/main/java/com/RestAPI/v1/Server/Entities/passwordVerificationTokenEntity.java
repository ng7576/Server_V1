package com.RestAPI.v1.Server.Entities;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;


@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
public class passwordVerificationTokenEntity {


    private static final int EXP_TIME = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;



    private Date timeTillExp;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "email",
            nullable = false,
            foreignKey = @ForeignKey(name="REGISTRATION_TOKEN"))
    private userEntity user;

    public passwordVerificationTokenEntity(userEntity user, String token) {
        this.user = user;
        this.token = token;

        this.timeTillExp = countDownTill();
    }
    public passwordVerificationTokenEntity(String token) {
        super();
        this.token = token;
        this.timeTillExp = countDownTill();
    }



    private Date countDownTill() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, passwordVerificationTokenEntity.EXP_TIME);
        return new Date(cal.getTime().getTime());
    }



}
