package com.RestAPI.v1.Server.Security;



import com.RestAPI.v1.Server.ServiceImpl.customUserDetailsService;
import com.RestAPI.v1.Server.Utils.JWTUtils.JwtTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class securityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] openUrls = {
            "/register",
            "/verifyRegistration",
            "/login",

    };

    @Bean
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder(12);
    }


    @Autowired
    private  customUserDetailsService userService;

    @Autowired
    private  JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    @Override
    protected  void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(openUrls)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }





}
