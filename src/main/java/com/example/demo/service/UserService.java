package com.example.demo.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.demo.entity.User;


public interface UserService extends JpaRepository<User, Long> {

	String query1 = "select u from User u where u.emailId=?1 and u.pwd=?2";
	String query2= "select u from User u where u.ID=?1";
	String query3 = "select u from User u where u.emailId=?1";
	
		@Query(query1)
		public User authenticate(String userId, String pwd) ;
	
		@Query(query2)
		public User getUserById(long id) ;
	 
		@Query(query3)
		public User getUserByEmailId(String emailId) ;
	   
}
