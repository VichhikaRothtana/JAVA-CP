package com.webtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.webtest.model.Role;
import com.webtest.repository.RoleRepository;
 
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
 
    @Autowired private RoleRepository repo;
     
    @Test
    public void testCreateRoles() {
        Role user = new Role("User");
        Role admin = new Role("Admin"); 
        List<Role> list = new ArrayList<Role>();
        list.add(user);
        list.add(admin);
        repo.saveAll(list); 
        
        List<Role> listRoles = repo.findAll();
         
        assertThat(listRoles.size()).isEqualTo(2);
    }
     
}