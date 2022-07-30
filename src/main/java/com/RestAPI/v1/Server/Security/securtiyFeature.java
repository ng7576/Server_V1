//package com.RestAPI.v1.Server.Security;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class securtiyFeature {
//
//    private static final String[] openUrls = {
//            "/test",
//            "/register"
//    };
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder(12);
//    }
//
//    @Bean
//    SecurityFilterChain openApiPaths(HttpSecurity url) throws Exception {
//        url.cors()
//                .and()
//                .csrf()
//                .disable()
//                .authorizeHttpRequests()
//                .antMatchers(openUrls)
//                .permitAll();
//        return url.build();
//    }
//}
