package com.webtest.model;
 
import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "test")
public class Test {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  
    
	@Column(unique = true, length = 125, nullable = false)
	private String name;
	
	@Column(nullable = true)
    private int totalPoints; 
}
