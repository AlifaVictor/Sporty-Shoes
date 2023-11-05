package com.example.demo.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.demo.entity.Admin;


public interface AdminService extends JpaRepository<Admin, Long>{

		
	String query4 = "select ad from Admin ad where ad.adminId=?1 and ad.pwd=?2";
	
	    @Query(query4)
		public Admin authenticate(String adminId, String pwd) ;			
				 
}
