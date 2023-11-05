package com.example.demo.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Purchase;


public interface PurchaseService  extends JpaRepository<Purchase, Long>
{

	String query1 = "select p from Purchase p where p.ID=?1";
	String query2 = "select p from Purchase p where p.userId=?1";

		@Query(query1)
		public Purchase getPurchaseById(long id) ;
				
		
		@Query(query2)
		public List<Purchase> getAllItemsByUserId(long userId) ;
				 
}
