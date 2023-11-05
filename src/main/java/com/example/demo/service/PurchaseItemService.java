package com.example.demo.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.demo.entity.PurchaseItem;


public interface PurchaseItemService  extends JpaRepository<PurchaseItem, Long>{

	
	
	String query1 = "select p from PurchaseItem  p where p.ID=?1";
	String query2 = "select p from PurchaseItem p where p.purchaseId=?1";
	String query3 = "delete from PurchaseItem p where p.purchaseId=?1";

	    @Query(query1)
		public PurchaseItem getItemById(long id) ;
		
		@Query(query2)
		public List<PurchaseItem> getAllItemsByPurchaseId(long purchaseId) ;	

	
    	@Query(query3)
		public void deleteAllItemsForPurchaseId(long purchaseId) ;


	 
}
