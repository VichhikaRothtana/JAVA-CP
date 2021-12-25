package com.webtest.controller;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping; 

import com.webtest.CustomUserDetails;
import com.webtest.model.AnswerChoice;
import com.webtest.model.History;
import com.webtest.model.Player; 
import com.webtest.model.Test;
import com.webtest.model.User;
import com.webtest.repository.AnswerRepository;
import com.webtest.repository.HistoryRepository;
import com.webtest.repository.PlayerRepository;
import com.webtest.repository.QuestionRepository;
import com.webtest.repository.TestRepository;
import com.webtest.repository.UserRepository;
import com.webtest.service.UserService;

@Controller
public class AppController {

	@Autowired
	private UserService service;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private TestRepository testRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private HistoryRepository historyRepository;
	@Autowired
	private UserRepository userRepository;
	int userID;
	int playerID = 0;
	List<Integer> getUnplayedTest = new ArrayList<Integer>();
	@GetMapping({ "/", "" }) 
	public String viewUserPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login.html";
		}
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userID = ((CustomUserDetails) principal).getID();
		if (((CustomUserDetails) principal).hasRole("Admin")) {
			model.addAttribute("totalTestCase", testRepository.findAll().size());
			model.addAttribute("totalUsers", userRepository.findAll().size() - 1);
			model.addAttribute("userID", userID);
			return "admin/admin.html";
		} else {

			int score = 0;
			

			try { 
				score = playerRepository.getUserScore(userID);

				System.out.println(score);
			} catch (Exception e) {
				score = 0;
			} 
			
			
			List<Test> test = testRepository.findAll(); 
			List<Player> player = playerRepository.getAllPlayerByUserID(userID); 
			List<Integer> tempTestList = new ArrayList<Integer>();
			List<Integer> tempPlayerList = new ArrayList<Integer>();
			
			for(Test t : test)
			{
				tempTestList.add(t.getId());
			}
			
			for (Player p : player)
			{
				tempPlayerList.add(p.getTest().getId());
			}
			 
			
			
			
			
			if (tempTestList.equals(tempPlayerList))
			{
				getUnplayedTest.clear(); 
			}
			else
			{
				getUnplayedTest.clear();
				
				for(int i : tempPlayerList)
				{
					
					if (tempTestList.contains(i))
					{
						getUnplayedTest.add(i);
					}
				} 
			} 

			
			model.addAttribute("all_test", testRepository.count());
			model.addAttribute("taken_test", tempPlayerList.size());
			model.addAttribute("untaken_test", getUnplayedTest.size());
			model.addAttribute("score", score);
			System.out.println(playerRepository.getListPlayer());
			model.addAttribute("listPlayers", playerRepository.getListPlayer());
			model.addAttribute("userID", userID);
			return "user/user.html";
		}

	}

	@GetMapping("/all/test")
	public String viewAllTestPage(Model model) {
		
		
		model.addAttribute("listAllTests", testRepository.findAll());
		model.addAttribute("userID", userID);
		model.addAttribute("listPlayer", playerRepository.findAll());
		
		return "user/test/all-test.html";
	}
	
	@GetMapping("/taken/test")
	public String viewTakenTestPage(Model model) {
		
		
		model.addAttribute("listTakenTests", testRepository.getTakenTest(userID));
		model.addAttribute("userID", userID); 
		
		return "user/test/taken-test.html";
	}
	
	@GetMapping("/untaken/test")
	public String viewUntakenTestPage(Model model) {

		model.addAttribute("listUnTakenTests", testRepository.getUnTakenTest(getUnplayedTest));
		model.addAttribute("userID", userID); 
		
		return "user/test/untaken-test.html";
	}

	int testID;

	@RequestMapping("/test/play/{id}")
	public String viewTestPlay(@PathVariable(value = "id") int id, Model model) {
		testID = id;
		Test test = testRepository.getById(id);
		String testName = test.getName();
		questionRepository.getAllQuestionsTest(id);
		answerRepository.getAllAnswersQuestion(testID);
		model.addAttribute("listQuestion", questionRepository.getAllQuestionsTest(id));

		answerRepository.findAll();
		model.addAttribute("listAnswer", answerRepository.findAll());
		model.addAttribute("testName", testName); 
		model.addAttribute("testID", testID);
		return "user/test/play-test.html";
	}

	List<Integer> answerTemp = new ArrayList<Integer>();

	@RequestMapping("/test/player/store")
	public String viewTestPlayerStore(HttpServletRequest req) {
		List<String> requestParameterNames = Collections.list((Enumeration<String>) req.getParameterNames());

		requestParameterNames.remove(0); // remove csrf
		List<String> values = new ArrayList<String>();
		Hashtable<Integer, List<String>> dict_value = new Hashtable<Integer, List<String>>();
		Hashtable<Integer, List<Integer>> tempCorrectAnswer = new Hashtable<Integer, List<Integer>>();
		int key = 0;
		for (String i : requestParameterNames) {
			key += 1;
			values = Arrays.asList(req.getParameterValues(i));
			dict_value.put(key, values);
		}


		playerRepository.insertPlayerAfterPlay(0, testID, userID);
		playerID = playerRepository.getPlayerLastID();
		int point = 0;
		for (Map.Entry<Integer, List<String>> entry : dict_value.entrySet()) {
//			Integer k = entry.getKey();
			List<String> val = entry.getValue();
			for (String i : val) {
				String[] getAnswerQuestion = i.split("-");
				answerTemp.add(Integer.parseInt(getAnswerQuestion[0]));
				int answerID = Integer.parseInt(getAnswerQuestion[0]);
				int questionID = Integer.parseInt(getAnswerQuestion[1]);

				try {
					historyRepository.insertPlayerHistory(answerID, playerID, questionID);

				} catch (Exception e) {
					System.out.println(e);
				}

				try {
					List<AnswerChoice> getCorrectAnswer = answerRepository.getCorrectAnswer(questionID);
					List<Integer> myTemp = new ArrayList<>();
					myTemp.clear();
					for(AnswerChoice a : getCorrectAnswer)
					{
						myTemp.add(a.getId()); 
					}
					tempCorrectAnswer.put(questionID, myTemp);
					
				} catch (Exception e) { }
			}

		} 
		for (Map.Entry<Integer, List<Integer>> entry : tempCorrectAnswer.entrySet()) {
			Integer questionID = entry.getKey();
			List<Integer> val = entry.getValue(); 
			
			List<Integer> tempAnswer = new ArrayList<Integer>();
			
			
			List<History> history = historyRepository.getHistoryPlayerQuestion(playerID, questionID);
			
			
		
			for(History h : history)
			{  
				tempAnswer.add(h.getAnswerChoice().getId()); 
			}
			
			
			
			if(val.equals(tempAnswer))
			{ 
				point += questionRepository.getPointOfQuestion(questionID);
				System.out.println("====val===");
				System.out.println(val);
				System.out.println("====tempAnswer===");
				System.out.println(tempAnswer);
				System.out.println(val.equals(tempAnswer));
			}
			else
			{
				System.out.println("false");
			}
			
		}  
		
		
		playerRepository.updateAfterTakeTest(point, playerID);

		return "redirect:/test/view/result";
	}

	@RequestMapping("/test/view/result")
	public String viewTestPlayerAfterSubmit(Model model) {
		int id = testID;
		Test test = testRepository.getById(id);
		String testName = test.getName();
		questionRepository.getAllQuestionsTest(id);
		answerRepository.getAllAnswersQuestion(testID);
		
		
		model.addAttribute("listQuestion", questionRepository.getAllQuestionsTest(id));
		model.addAttribute("listAnswer", answerRepository.findAll());
		model.addAttribute("testName", testName);
		model.addAttribute("history", historyRepository.findAll());
		model.addAttribute("testID", testID);
		model.addAttribute("playerID", playerID);

		return "user/test/view-test.html";
	}

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());

		return "register.html";
	}

	@PostMapping("/process_register")
	public String processRegister(User user) {
		service.registerDefaultUser(user);
		return "register_success.html";
	}

	@GetMapping("/403")
	public String error403() {
		return "error/403.html";
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}
}
