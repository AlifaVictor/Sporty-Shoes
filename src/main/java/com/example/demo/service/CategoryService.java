package com.example.demo.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Category;


public interface CategoryService  extends JpaRepository<Category, Long> {

	
String query1 = "select c from Category c where c.ID=?1";


	@Query(query1)
		public Category getCategoryById(long id) ;
		
	
		/*
		 * @Transactional public String getCategoriesDropDown(long id) { StringBuilder
		 * sb = new StringBuilder(""); List<Category> list =
		 * categoryDAO.getAllCategories(); for(Category cat: list) { if (cat.getID() ==
		 * id) sb.append("<option value=" + String.valueOf(cat.getID()) + " selected>" +
		 * cat.getName() + "</option>"); else sb.append("<option value=" +
		 * String.valueOf(cat.getID()) + ">" + cat.getName() + "</option>");
		 * 
		 * } return sb.toString(); }
		 */
		

		 
}
