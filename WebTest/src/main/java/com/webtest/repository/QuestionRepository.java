package com.webtest.repository;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.webtest.model.Question; 

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	@Query(value = "SELECT * FROM question q WHERE q.test_id = ?1", nativeQuery = true)
	public List<Question> getAllQuestionsTest(int testID);
	
	@Query(value = "SELECT point FROM  question WHERE id = ?1", nativeQuery = true)
	public int getPointOfQuestion(int questionID);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE Question SET is_single_choice=?1, point=?2, question_title=?3 WHERE id=?4") 
	public void updateWithoutImage(boolean isSingle, int point, String question_title, Integer id);
	
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Question a WHERE a.test_id = ?1", nativeQuery = true) 
	public void deleteAllQuestionByID(int testID);
}
