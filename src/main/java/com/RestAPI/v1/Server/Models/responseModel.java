package com.RestAPI.v1.Server.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class responseModel<T> {

    private String message;
    private HttpStatus status;
    private T responseObj;
}
