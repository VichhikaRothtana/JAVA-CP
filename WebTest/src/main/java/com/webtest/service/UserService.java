package com.webtest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 
import com.webtest.model.Role;
import com.webtest.model.User;
import com.webtest.repository.PlayerRepository;
import com.webtest.repository.RoleRepository;
import com.webtest.repository.UserRepository;

@Service
public class UserService {
 
    @Autowired
    private UserRepository userRepo;
     
    @Autowired RoleRepository roleRepo;
    @Autowired PlayerRepository playerRepository;
     
    @Autowired PasswordEncoder passwordEncoder;
     
    public void registerDefaultUser(User user) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
          
        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);
        
        userRepo.save(user);
        
        
    }
    
    public List<User> listAll()
    {
    	return userRepo.findAll();
    }
    
     
}