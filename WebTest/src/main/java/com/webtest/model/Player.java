package com.webtest.model;
 
import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "player")
public class Player {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 
 
    @ManyToOne
	@JoinColumn(nullable = false, name = "user_id")
	private User user; 
    
    @Column(nullable = false)
    private int score;
    
    @ManyToOne
	@JoinColumn(nullable = true, name = "test_id")
	private Test test;  
}
