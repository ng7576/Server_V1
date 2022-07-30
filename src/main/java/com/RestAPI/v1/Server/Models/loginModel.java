package com.RestAPI.v1.Server.Models;


import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginModel {
    private String userEmail;
    private String password;
}
