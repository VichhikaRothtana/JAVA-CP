package com.webtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.webtest.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);
	
	@Query(value = "select * from user where email != 'admin@gmail.com';", nativeQuery = true)
	public List<User> getListUser();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE user SET first_name=?1, last_name=?2 WHERE id=?3", nativeQuery = true) 
	public void updateUserName(String firstName, String lastName, Integer id);
}
