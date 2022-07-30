package com.RestAPI.v1.Server.Models;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class passwordModel {
    private String email;
    private String oldPassword;
    private String newPassword;
}
