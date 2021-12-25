package com.webtest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.webtest.model.AnswerChoice; 

public interface AnswerRepository extends JpaRepository<AnswerChoice, Integer> { 
	@Query(value = "SELECT * FROM answer_choice a WHERE a.question_id = ?1", nativeQuery = true)
	public List<AnswerChoice> getAllAnswersQuestion(int questionID);
	
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO answer_choice (choice, is_correct, question_id) VALUES (?1, ?2, ?3)", nativeQuery = true) 
	public void insertAnswerChoice(String choice, boolean is_correct, Integer question_id);
	
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM answer_choice a WHERE a.question_id = ?1", nativeQuery = true) 
	public void deleteAnswerChoiceQuestionID(Integer question_id);
	
	@Query(value = "SELECT q.id, a.choice, a.is_correct, q.question_title, q.is_single_choice FROM answer_choice a INNER JOIN question q ON a.question_id= q.id WHERE q.test_id = ?1", nativeQuery = true)
	public List<String> getAllAnswerWithQuestionID(int testID);
	 
	
	
	@Query(value = "select DISTINCT  q.point  from history h join answer_choice a on h.answer_id = a.id left join question q on a.question_id = q.id join player p on h.player_id = p.id where a.is_correct = 1 and a.question_id = ?1 and q.test_id = ?2 and player_id = ?3 and h.answer_id = ?4", nativeQuery = true)
	public int getPointForCorrect(int question_id, int test_id, int player_id, int answer_id);
	
	
	@Query(value = "select * from answer_choice where is_correct = 1 and question_id = ?1", nativeQuery = true)
	public List<AnswerChoice> getCorrectAnswer(int question_id);
	
//	@Transactional
//	@Modifying
//	@Query(value = "INSERT INTO answer_choice (choice, is_correct, question_id) VALUES (?1, ?2, ?3)", nativeQuery = true) 
//	public void insertAnswerChoice(String choice, boolean is_correct, Integer question_id);
}
