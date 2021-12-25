package com.webtest.model;
 
import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question")
public class Question {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "test_id")
	private Test test;  
	
	@Column(length = 125, nullable = false)
	private String question_title;
	
	@Column(length = 250, nullable = true)
	private String image;
	
	@Column(nullable = false)
    private int point;
	
	@Column(nullable = false)
    private boolean isSingleChoice;  
	
	@Transient
	public String getImageQuestionPath()
	{
		if (image == null || id == null) return null;
		return "/question-images/" + id + "/" + image;
	}
}
