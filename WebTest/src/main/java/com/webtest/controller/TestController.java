package com.webtest.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
 
import com.webtest.model.Test;
import com.webtest.repository.QuestionRepository; 
import com.webtest.repository.TestRepository;

@Controller
public class TestController {
	
	@Autowired private TestRepository testRepository;
	@Autowired private QuestionRepository questionRepository;
	
	
	@GetMapping("/test")
    public String viewTestPage(Model model) 
    { 
		model.addAttribute("listTest", testRepository.findAll());
    	return "admin/test/test.html";
    }
	
	@GetMapping("/test/add")
    public String viewTestAdd(Model model)
    {
		Test test = new Test();
		model.addAttribute("test", test);
    	return "admin/test/add-test.html";
    }
	
	int testID = 0; 
	@PostMapping("/test/store")
    public String viewTestStore(@ModelAttribute("test") Test test)
    { 
		try {
			test.setTotalPoints(testRepository.getPointByTestID(testID));
		} catch (Exception e) {
			test.setTotalPoints(0);
		} 
		
		testRepository.save(test);
    	return "redirect:/test";
    }
	
	@RequestMapping("/test/edit/{id}")
	public String viewTestEdit(@PathVariable(value = "id") int id, Model model) {  
		 testID = id;
		
		Test test = testRepository.getById(id); 
		model.addAttribute("test", test);
		 
		return "admin/test/edit-test.html";
	}
	
	@RequestMapping("/test/delete/{id}")
	public String viewTestDelete(@PathVariable(value = "id") int id) {  
		questionRepository.deleteAllQuestionByID(id);
		this.testRepository.deleteById(id);
		return "redirect:/test";
	}
	
	

	 
}
