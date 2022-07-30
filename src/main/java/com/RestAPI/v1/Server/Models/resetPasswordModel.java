package com.RestAPI.v1.Server.Models;


import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class resetPasswordModel {

    private String email;
    private String password;
}
