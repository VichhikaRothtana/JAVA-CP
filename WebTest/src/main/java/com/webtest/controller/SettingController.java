package com.webtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webtest.CustomUserDetails;
import com.webtest.model.Test;
import com.webtest.model.User;
import com.webtest.repository.UserRepository;

@Controller
public class SettingController {
 
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("/setting/{id}")
	public String viewaPrticipants(@PathVariable(value = "id") int id, Model model) { 
		model.addAttribute("user", userRepository.getById(id));
			  
		return "admin/setting/setting.html";
	}
	@PostMapping("/setting/store")
    public String viewSettingStore(@ModelAttribute("user") User user, @AuthenticationPrincipal CustomUserDetails loggedUser)
    {   
		String firstName= user.getFirstName();
		String lastName= user.getLastName();
		int id= user.getId();
		userRepository.updateUserName(firstName, lastName, id);
		loggedUser.setFirstName(firstName);
		loggedUser.setLastName(lastName);
    	return "redirect:/";
    }


}
