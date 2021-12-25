package com.webtest.controller;
 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.webtest.model.AnswerChoice;
import com.webtest.model.Question;
import com.webtest.model.Test;
import com.webtest.repository.AnswerRepository;
import com.webtest.repository.QuestionRepository; 
import com.webtest.repository.TestRepository;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
@Controller
public class QuestionController {
	
	@Autowired private TestRepository testRepository;
	@Autowired private QuestionRepository questionRepository;
	@Autowired private AnswerRepository answerRepository;
	
	
	@GetMapping("/question")
    public String viewQuestionPage(Model model) 
    {
		model.addAttribute("listQuestion", testRepository.findAll());
    	return "admin/question/question.html";
    }
	 
	
	int testID; 
	
	@RequestMapping("/test/view/{id}")
	public String viewTestView(@PathVariable(value = "id") int id, Model model) {
		testID = id;  
		try {
			System.out.println(testRepository.getPointByTestID(testID));
		} catch (Exception e) {
			System.out.println(e);
		} 
			
			
		Test test = testRepository.getById(id);
		String testName = test.getName();
		
		List<Question> question = questionRepository.getAllQuestionsTest(id); 
		 
		model.addAttribute("listQuestion", question); 
		model.addAttribute("testName", testName);
		System.out.println(testID);
		model.addAttribute("testID", testID); 
		return "admin/test/view-test.html";
	} 
	
	@GetMapping("/question/add/{id}")
    public String viewQuestionAdd(@PathVariable(value = "id") int id, Model model)
    {  
		testID = id;
		Question question = new Question();
		model.addAttribute("question", question);
		model.addAttribute("testID", id); 
    	return "admin/question/add-question.html";
    }
	 
	
	@PostMapping("/question/store")
    public String viewQuestionStore(@RequestParam("fileImage") MultipartFile multipartFile, @RequestParam("is_single") boolean isSingle, @ModelAttribute("question") Question question) throws IOException
    {   
		Test test = testRepository.getById(testID);
		System.out.println(testID);
		question.setTest(test);  
		question.setSingleChoice(isSingle);  
		System.out.println(question);
		System.out.println(multipartFile);
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		if (fileName.length() > 0)
		{ 
			question.setImage(fileName);
			Question saveQuestion =  questionRepository.save(question);
			String uploadDir = "./question-images/" + saveQuestion.getId();
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath))
			{
				Files.createDirectories(uploadPath);
			}
			try
			{
			InputStream inputStream = multipartFile.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(IOException e)
			{
				throw new IOException("could not save uploaded image" + fileName);
			}
		}	
		else
		{   
			question.setImage(null);  
			questionRepository.save(question);
		}
		
		try {
			testRepository.updateTotalPoints(testRepository.getPointByTestID(testID), testID);
		} catch (Exception e) {
			System.out.println(e);
		}  
		
	 
    	return "redirect:/test/view/" + testID;
    }
	
	
	
	@RequestMapping("/question/edit/{id}")
	public String viewQuestionEdit(@PathVariable(value = "id") int id, Model model) {  
		 
		Question question = questionRepository.getById(id);
		model.addAttribute("question", question);
		 
		return "admin/question/edit-question.html";
	}
	
	
	@PostMapping("/question/edit/store")
    public String viewQuestionEditStore(@RequestParam("fileImage") MultipartFile multipartFile, 
    		@RequestParam("is_single") boolean isSingle, 
    		@RequestParam("point") int point,  
    		@RequestParam("question_title") String question_title, 
    		@RequestParam("id") Integer id, 
    		@ModelAttribute("question") Question question) throws IOException
    {   


		  
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

		if (fileName.length() > 0)
		{ 
			Test test = testRepository.getById(testID); 
			question.setTest(test);  
			question.setSingleChoice(isSingle); 
			question.setImage(fileName);
			Question saveQuestion =  questionRepository.save(question);
			String uploadDir = "./question-images/" + saveQuestion.getId();
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath))
			{
				Files.createDirectories(uploadPath);
			}
			try
			{
			InputStream inputStream = multipartFile.getInputStream();
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			catch(IOException e)
			{
				throw new IOException("could not save uploaded image" + fileName);
			}
		}	
		else
		{   

			questionRepository.updateWithoutImage(isSingle, point, question_title, id);
		}
		
		try {
			testRepository.updateTotalPoints(testRepository.getPointByTestID(testID), testID);
		} catch (Exception e) {
			System.out.println(e);
		} 
		
		
    	return "redirect:/test/view/" + testID;
    }
	
	@RequestMapping("/question/delete/{id}")
	public String viewQuestionDelete(@PathVariable(value = "id") int id) {  
		answerRepository.deleteAnswerChoiceQuestionID(id);
		this.questionRepository.deleteById(id); 
		try {
			testRepository.updateTotalPoints(testRepository.getPointByTestID(testID), testID);
		} catch (Exception e) {
			testRepository.updateTotalPoints(0, testID);
		}  
		return  "redirect:/test/view/" + testID;
	} 

	int quesetionID;

	@RequestMapping("/question/view/{id}")
	public String viewQuestionView(@PathVariable(value = "id") int id, Model model) {
		quesetionID = id;
		List<AnswerChoice> answerChoices = answerRepository.getAllAnswersQuestion(id);
		if(answerChoices.size() < 1)
		{
			answerRepository.insertAnswerChoice("", false, quesetionID);
			answerChoices.clear();
			answerChoices = answerRepository.getAllAnswersQuestion(id);
		}
		
		
		
		Question question = questionRepository.getById(id); 
		System.out.println(answerChoices.size()); 
		System.out.println(answerChoices); 
		model.addAttribute("listChoices", answerChoices);
		model.addAttribute("questionName", question.getQuestion_title());
		model.addAttribute("isSingleChoice", question.isSingleChoice());
		return "admin/answer/answer.html";
	}
 
	@PostMapping("/answer/store")
	public String viewAnswerAddStore(@RequestParam("get_choices") ArrayList<String> getChoices, @RequestParam("choice") ArrayList<String> choices) {
		answerRepository.deleteAnswerChoiceQuestionID(quesetionID); 
		System.out.println(getChoices) ;
		System.out.println(choices) ;

		for (String temp : getChoices) { 
			System.out.println(temp);
			System.out.println(temp.getClass());
			if(choices.contains(temp) && temp.length() > 0)
			{ 
				answerRepository.insertAnswerChoice(temp, true, quesetionID);
			}
			else 
			{
				if(temp.length() > 0)
				{
					answerRepository.insertAnswerChoice(temp, false, quesetionID);
				}
			}
		}  
		return "redirect:/test/view/" + testID;
	}


	 
}
