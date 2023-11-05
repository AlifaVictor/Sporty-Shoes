package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
 
@Controller
public class MemberController {

	@Autowired
	private UserService userService; 
	
	  @RequestMapping(value = "/login", method = RequestMethod.GET)
	    public ModelAndView login(ModelMap map) 
	    {
		  
		  ModelAndView mv  = new ModelAndView();
		  mv.setViewName("login.jsp");
		  map.addAttribute("pageTitle", "SPORTY SHOES - MEMBER LOGIN");
	        return mv;
	    }	
	  
	  @RequestMapping(value = "/loginaction", method = RequestMethod.POST)
	    public ModelAndView loginAction(ModelMap map,  javax.servlet.http.HttpServletRequest request,
	    		@RequestParam(value="email_id", required=true) String emailId,
	    		 @RequestParam(value="pwd", required=true) String pwd) 
	    {
		  ModelAndView mv  = new ModelAndView();
		  User user = userService.authenticate(emailId, pwd);
		  if (user == null) { 
			  map.addAttribute("error", "Login failed");
			  mv.setViewName("login.jsp");
			  return mv;
		  }
		  HttpSession session = request.getSession();
		  session.setAttribute("user_id", user.getID());
		  session.setAttribute("user_name", user.getFname());
		  mv.setViewName("dashboard.jsp");
		  return mv;
	    }		  
	  
	  	  
	  @RequestMapping(value = "/signup", method = RequestMethod.GET)
	    public ModelAndView signup(ModelMap map) 
	    {
		  ModelAndView mv = new ModelAndView();
		  mv.setViewName("register.jsp");
		  map.addAttribute("pageTitle", "SPORTY SHOES - MEMBER REGISTRATION");
	      return mv; 
	    }	
	  
	  @RequestMapping(value = "/signupaction", method = RequestMethod.POST)
	    public ModelAndView signupAction(ModelMap map,  javax.servlet.http.HttpServletRequest request,
	    		@RequestParam(value="email_id", required=true) String emailId,
	    		 @RequestParam(value="pwd", required=true) String pwd,
	    		 @RequestParam(value="pwd2", required=true) String pwd2,
	    		 @RequestParam(value="fname", required=true) String fname,
	    		 @RequestParam(value="lname", required=true) String lname,
	    		 @RequestParam(value="age", required=true) String age,
	    		 @RequestParam(value="address", required=true) String address) 
	    {
		  ModelAndView mv = new ModelAndView();
		 
		  if (emailId == null || emailId.equals("")) {
			  map.addAttribute("error", "Email id is required.");
			  mv.setViewName("register.jsp");
			  return mv;
		  }

		  if (pwd == null || pwd2 == null || pwd.equals("") || pwd2.equals("")) {
			  map.addAttribute("error", "Error , Incomplete passwords submitted.");
			  mv.setViewName("register.jsp");
			  return mv;
		  }
		  
		  if (!pwd.equals(pwd2)) {
			  map.addAttribute("error", "Error , Passwords do not match.");
			  mv.setViewName("register.jsp");
			  return mv;
		  }
		  
		  if (fname == null || fname.equals("")) {
			  map.addAttribute("error", "First name is required.");
			  mv.setViewName("register.jsp");
			  return mv;
		  }

		  if (lname == null || lname.equals("")) {
			  map.addAttribute("error", "Last name is required.");
			  mv.setViewName("register.jsp");
			  return mv;
		  }
		  if (age == null || age.equals("")) {
			  age = "0";
		  }
		  
		  
		  User user = userService.getUserByEmailId(emailId);
		  if (user != null) { 
			  map.addAttribute("error", "This email id already exists.");
			  mv.setViewName("register.jsp");
		  }
		  user = new User();
		  user.setID(0);
		  user.setEmailId(emailId);
		  user.setFname(fname);
		  user.setLname(lname);
		  user.setAge(Integer.parseInt(age));
		  user.setAddress(address);
		  user.setPwd(pwd);
		  
		  userService.save(user);	  
		  
		  mv.setViewName("register-confirm.jsp");
		  return mv;
	    }
	  
	  
	  @RequestMapping(value = "/registerconfirm", method = RequestMethod.GET)
	    public ModelAndView registerConfirm(ModelMap map) 
	    {
		  ModelAndView mv = new ModelAndView();
		  mv.setViewName("register-confirm.jsp");
	        return mv;
	    }		 		  
	  @RequestMapping(value = "/editprofile", method = RequestMethod.GET)
	    public ModelAndView editProfile(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv =  new ModelAndView();
		  mv.setViewName("login.jsp");
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			 return mv;
		  }
		  User user = userService.getUserById((Long) session.getAttribute("user_id"));

		  map.addAttribute("pageTitle", "SPORTY SHOES - MEMBER EDIT PROFILE");
		  map.addAttribute("user", user);
		  mv.setViewName("edit-profile.jsp");
	        return mv;
	    }		 	  


	  @RequestMapping(value = "/editprofileaction", method = RequestMethod.POST)
	    public ModelAndView editProfileAction(ModelMap map,
	    		javax.servlet.http.HttpServletRequest request, 
	    		@RequestParam(value="user_id", required=true) String userId,
	    		 @RequestParam(value="pwd", required=true) String pwd,
	    		 @RequestParam(value="pwd2", required=true) String pwd2,
	    		 @RequestParam(value="fname", required=true) String fname,
	    		 @RequestParam(value="lname", required=true) String lname,
	    		 @RequestParam(value="age", required=true) String age,
	    		 @RequestParam(value="address", required=true) String address) 
	    {
		  ModelAndView mv = new ModelAndView();
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  mv.setViewName("login.jsp");
			  return mv;
		  }
		  
		  User user = userService.getUserById((Long) session.getAttribute("user_id"));
		  map.addAttribute("user", user);
		  
		  if (pwd == null || pwd2 == null || pwd.equals("") || pwd2.equals("")) {
			  map.addAttribute("error", "Error , Incomplete passwords submitted.");
			 
			  mv.setViewName("edit-profile.jsp");
			  return mv;
		  }
		  
		  if (!pwd.equals(pwd2)) {
			  map.addAttribute("error", "Error , Passwords do not match.");
			  
			  mv.setViewName("edit-profile.jsp");
			  return mv;
		  }
		  
		  if (fname == null || fname.equals("")) {
			  map.addAttribute("error", "First name is required.");
			  
			  mv.setViewName("edit-profile.jsp");
			  return mv;
		  }

		  if (lname == null || lname.equals("")) {
			  map.addAttribute("error", "Last name is required.");
			  
			  mv.setViewName("edit-profile.jsp");
			  return mv;
		  }
		  if (age == null || age.equals("")) {
			  age = "0";
		  }
		  
		  
		
		  user.setFname(fname);
		  user.setLname(lname);
		  user.setAge(Integer.parseInt(age));
		  user.setAddress(address);
		  user.setPwd(pwd);
		  
		  userService.save(user);
		        
	        mv.setViewName("dashboard.jsp");
		    return mv;
	    }

	  @RequestMapping(value = "/logout", method = RequestMethod.GET)
	    public ModelAndView logout(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  	HttpSession session = request.getSession();
		  	session.invalidate();
		  	mv.setViewName("dashboard.jsp");
	        return mv;
	    }

}
