package com.RestAPI.v1.Server.Utils.JWTUtils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Slf4j
public class JwtTokenFilter  extends OncePerRequestFilter {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private UserDetailsService customService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {
        String auth  = req.getHeader("Authorization");
        String token = null;
        String userEmail = null;


        log.info(auth);

        if ( null != auth && auth.startsWith("Bearer "))
        {
            log.info("first if triggered");
            token = auth.substring(7);
            userEmail = jwtUtility.getEmailFromToken(token);

        }

        if ( null != userEmail && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("second if triggered");
            UserDetails userDetails = customService.loadUserByUsername(userEmail);

            if (jwtUtility.tokenValidator(token, userDetails)) {
                log.info("Inside validator :: " + String.valueOf(jwtUtility.tokenValidator(token, userDetails)));
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(req)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        chain.doFilter(req, res);

    }
}
