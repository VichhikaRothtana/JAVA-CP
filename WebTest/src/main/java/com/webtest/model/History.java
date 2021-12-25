package com.webtest.model;
 
import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "history")
public class History {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
 
	@ManyToOne
	@JoinColumn(nullable = false, name = "player_id")
	private Player player; 
	
    @ManyToOne
	@JoinColumn(nullable = false, name = "question_id")
	private Question question;  
    
    @ManyToOne
	@JoinColumn(nullable = false, name = "answer_id")
	private AnswerChoice answerChoice;  
}
