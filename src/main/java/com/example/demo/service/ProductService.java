package com.example.demo.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Product;


public interface ProductService extends JpaRepository<Product,Long> {


        String Query1 = "select p from Product p where p.ID=?1";
		
        
        @Query(Query1)
		public Product getProductById(long id) ;
			
	 
//		@Transactional
//		public List<Object> getAllProductsWithJoin() {
//			return productDAO.getAllProductsWithJoin();
//		}
	 		
}
