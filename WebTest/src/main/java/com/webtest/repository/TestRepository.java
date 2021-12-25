package com.webtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.webtest.model.Test; 

public interface TestRepository extends JpaRepository<Test, Integer> {
	@Query(value = "select sum(point) from question q where q.test_id = ?1", nativeQuery = true)
	public int getPointByTestID(int testID);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE test SET total_points=?1 WHERE id=?2", nativeQuery = true) 
	public void updateTotalPoints(int total_points, Integer id);
	
	
	@Query(value = "SELECT t.id, t.name, t.total_points FROM test AS t LEFT JOIN player AS p ON p.test_id = t.id WHERE user_id = ?1", nativeQuery = true) 
	public List<Test> getTakenTest(int userID);
	
//	@Query(value = "SELECT t.id, t.name, t.total_points FROM test AS t LEFT JOIN player AS p ON p.test_id = t.id WHERE p.test_id IS NULL AND user_id = ?1", nativeQuery = true) 
//	public List<Test> getUnTakenTest(int userID);
//	
	
	@Query(value = "SELECT * FROM test WHERE test.id not in ?1", nativeQuery = true) 
	public List<Test> getUnTakenTest(List<Integer> testID);

}
