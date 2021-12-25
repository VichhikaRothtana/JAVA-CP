package com.webtest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.webtest.model.Role;
import com.webtest.model.User;
import com.webtest.repository.RoleRepository;
import com.webtest.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
    private TestEntityManager entityManager;
     
    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private RoleRepository roleRepo;
    
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("ravikumar@gmail.com");
        user.setPassword("ravi2020");
        user.setFirstName("Ravi");
        user.setLastName("Kumar");
         
        User savedUser = userRepo.save(user);
         
        User existUser = entityManager.find(User.class, savedUser.getId());
         
        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
         
    }
    
    @Test
    public void testAddRoleToNewAdminUser() { 
        User user = new User();
        user.setEmail("admin@gmail.com");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("admin");
        user.setPassword(encodedPassword); 
        user.setFirstName("rothtana");
        user.setLastName("danuth");
        
        Role roleAdmin = roleRepo.findByName("Admin");
        System.out.println(roleAdmin.toString());
        user.addRole(roleAdmin);       
         
        User savedUser = userRepo.save(user);
         
        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }
    
    @Test
    public void testAddRoleToExistingUser() {
        User user = userRepo.findById(1).get();
        Role roleUser = roleRepo.findByName("User");
        Role roleAdmin = new Role(2);
         
        user.addRole(roleUser);
        user.addRole(roleAdmin);
         
        User savedUser = userRepo.save(user);
         
        assertThat(savedUser.getRoles().size()).isEqualTo(2);      
    }
}
