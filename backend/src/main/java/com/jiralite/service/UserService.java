package com.jiralite.service;

import com.jiralite.config.SecurityConfig;
import com.jiralite.enums.Role;
import com.jiralite.model.User;
import com.jiralite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private  UserRepository  userRepository;
    @Autowired
    private PasswordEncoder  psswordEncoder;



    public  User CreatUser(String  username, String email, String password, Role role) {
        User user = new User();

        String   encodedPassword = psswordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        return  userRepository.save(user);
    }

}
