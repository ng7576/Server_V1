//package com.RestAPI.v1.Server.ServiceImpl;
//
//
//import com.RestAPI.v1.Server.Entities.userEntity;
//import com.RestAPI.v1.Server.Repositories.IuserModelRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Service
//public class UserService implements UserDetailsService {
//
//
//    @Autowired
//    private IuserModelRepository userRepo;
//
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        userEntity user = userRepo.findByEmail(username);
//        if(user == null) throw new UsernameNotFoundException("User Does not exist");
//        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
//
//    }
//}
