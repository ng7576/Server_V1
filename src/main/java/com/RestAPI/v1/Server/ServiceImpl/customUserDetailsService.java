package com.RestAPI.v1.Server.ServiceImpl;

import com.RestAPI.v1.Server.Entities.userEntity;
import com.RestAPI.v1.Server.Repositories.IuserModelRepository;
import com.RestAPI.v1.Server.Utils.JWTUtils.JWTUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
@Slf4j
public class customUserDetailsService implements UserDetailsService {
    @Autowired
    private IuserModelRepository userRepo;

    @Autowired
    private JWTUtility jwtUtility;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userEntity user = userRepo.findByEmail(username);
        log.info("Triggered LoadUserByUsername:: "
                + user.getEmail());
        GrantedAuthority authority = new SimpleGrantedAuthority("User");
        Collection<SimpleGrantedAuthority> auth = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), auth);
    }

}
