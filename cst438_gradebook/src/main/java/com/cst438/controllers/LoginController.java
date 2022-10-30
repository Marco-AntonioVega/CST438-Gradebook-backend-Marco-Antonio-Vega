package com.cst438.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentRepository;

@Controller
public class LoginController {
	/*
	 * used by React Login front end component to test if user is 
	 * logged in.  
	 *   response 401 indicates user is not logged in
	 *   a redirect response take user to Gradebook or Grades front end page.
	 */
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	
	@Value("${http://localhost:3000}")
	String redirect_url;
	
	@GetMapping("/user")
	public String user (@AuthenticationPrincipal OAuth2User principal){
		// used by front end to display user name.
		String email = principal.getAttribute("email");
		if(!courseRepository.findByEmail(email).isEmpty()) {
			return "redirect:" + redirect_url + "/assignment";
		}
		if(!enrollmentRepository.findByEmail(email).isEmpty()) {
			return "redirect:" + redirect_url + "/studentGrades";
		}
		throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Invalid credentials.");
	}
}