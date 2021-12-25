package com.webtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.webtest.model.History; 

public interface HistoryRepository extends JpaRepository<History, Integer> { 
 
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO history ( answer_id, player_id, question_id) VALUES (?1, ?2, ?3)", nativeQuery = true) 
	public void insertPlayerHistory(int answerID, int playerID, int questionID);


	@Query(value = "select score from player where test_id = ?1 and user_id = ?2", nativeQuery = true) 
	public String getPlayerEarnedPointsForTest(int testID, int user_id);
	
	@Query(value = "SELECT * FROM history WHERE player_id = ?1 AND question_id = ?2", nativeQuery = true) 
	public List<History> getHistoryPlayerQuestion(int playerID, int questionID);
	
	
	
	
}
