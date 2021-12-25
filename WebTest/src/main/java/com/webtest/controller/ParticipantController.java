package com.webtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
 
import com.webtest.repository.UserRepository;

@Controller
public class ParticipantController {
 
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/participants")
	public String viewaPrticipants(Model model) {
		  System.out.println(userRepository.getListUser());
		model.addAttribute("listUsers", userRepository.getListUser()); 
		return "admin/participant/list-participant.html";
	}


}
