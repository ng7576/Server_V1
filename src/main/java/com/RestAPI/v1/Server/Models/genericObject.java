package com.RestAPI.v1.Server.Models;


import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class genericObject<T, V> {
    private T type1;
    private V type2;

}
