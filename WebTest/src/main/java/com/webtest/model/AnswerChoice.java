package com.webtest.model;
 
import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "answer_choice")
public class AnswerChoice {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "question_id")
	private Question question;  
	
	@Column(length = 125, nullable = false)
	private String choice;
	 
	@Column(nullable = false)
    private boolean isCorrect;  
}
