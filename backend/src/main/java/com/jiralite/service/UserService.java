package com.jiralite.service;

import com.jiralite.config.SecurityConfig;
import com.jiralite.model.User;
import com.jiralite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    @Autowired
    private  UserRepository  userRepository;
    @Autowired
    private SecurityConfig securityConfig ;

    public User  CreateUser(String  username, String email, String password) {
        User  user1  = new User();
        user1.setUsername(username);
        user1.setEmail( email);
        String   encodedPassword=  securityConfig.passwordEncoder().encode(password);
        user1.setPassword(encodedPassword);


        return userRepository.save(user1);
    }

}
