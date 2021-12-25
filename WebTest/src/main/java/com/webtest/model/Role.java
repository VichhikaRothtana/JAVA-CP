package com.webtest.model;
 
import javax.persistence.*;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
     
    @Column(nullable = false, length = 45)
    private String name;
 
    public Role(String name) {
        this.name = name;
    }
    
    public Role(Integer id) {
        this.id = id;
    } 
    @Override
    public String toString()
    {
    	return this.name;
    }
}
