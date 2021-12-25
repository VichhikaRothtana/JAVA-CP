package com.webtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
 
import com.webtest.model.Player; 

public interface PlayerRepository extends JpaRepository<Player, Integer> {
	@Query(value = "SELECT * FROM player q WHERE q.user_id = ?1", nativeQuery = true)
	public Player getPlayByUserID(int userID);
	
	
	@Query(value = "SELECT sum(score) AS score FROM player WHERE user_id = ?1", nativeQuery = true)
	public int getUserScore(Integer userID);
	
	@Query(value = "SELECT id, sum(score) score, test_id, user_id FROM player GROUP BY user_id ORDER BY score DESC", nativeQuery = true)
	public List<Player> getListPlayer();  
	
	@Query(value = "SELECT MAX(id) last_id FROM player", nativeQuery = true)
	public int getPlayerLastID();
	
	
	@Query(value = "SELECT count(distinct t.id) FROM player p INNER JOIN test t ON p.test_id= t.id", nativeQuery = true)
	public int getPlayerTakenTest();
	
	
	@Query(value = "SELECT count(*) FROM test t LEFT JOIN player p ON p.test_id = t.id WHERE p.test_id IS NULL", nativeQuery = true)
	public int getPlayerUnTakenTest();
	
	
	@Query(value = "SELECT * FROM player p WHERE user_id = ?1", nativeQuery = true)
	public List<Player> getAllPlayerByUserID(int userID);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO player (score, test_id, user_id) VALUES (?1, ?2, ?3)", nativeQuery = true) 
	public void insertPlayerAfterPlay(int score, Integer test_id,  Integer user_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE player p SET p.score = ?1  WHERE (p.id = ?2)", nativeQuery = true) 
	public void updateAfterTakeTest(int score, int play_id);
	
	
	@Query(value = "SELECT * FROM player WHERE test_id = ?1 AND user_id = ?2", nativeQuery = true) 
	public String checkIfUserPlayTest(int testID, int userID);
	/*
	 * 
	 * SELECT count(t.id)
FROM player p
INNER JOIN test t ON p.test_id= t.id;
	 * */
//	
//	@Transactional
//	@Modifying
//	@Query(value = "UPDATE test SET total_points=?1 WHERE id=?2", nativeQuery = true) 
//	public void updateTotalPoints(int total_points, Integer id);

}
