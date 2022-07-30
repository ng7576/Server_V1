package com.RestAPI.v1.Server.Entities;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
public class verificationTokenEntity {

    private static final int EXP_TIME = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;



    private Date timeTillExp;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "email",
                nullable = false,
                foreignKey = @ForeignKey(name="PASSWORD_RESET_TOKEN"))
    private userEntity user;

    public verificationTokenEntity(userEntity user, String token) {
        this.user = user;
        this.token = token;

        this.timeTillExp = countDownTill();
    }
    public verificationTokenEntity(String token) {
        super();
        this.token = token;
        this.timeTillExp = countDownTill();
    }



    private Date countDownTill() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, verificationTokenEntity.EXP_TIME);
        return new Date(cal.getTime().getTime());
    }
}

