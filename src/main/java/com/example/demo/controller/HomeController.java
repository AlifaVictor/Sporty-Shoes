package com.example.demo.controller;


import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
 
@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryService; 

	@Autowired
	private ProductService productService; 
	
	  @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	    public ModelAndView home(ModelMap map,  javax.servlet.http.HttpServletRequest request) 
	    {
		  
		  ModelAndView mv = new ModelAndView();
		    HttpSession session = request.getSession();
			List<Product> list = productService.findAll();
			
			// use MAP to map the category names to product rows
			 HashMap<Long, String> mapCats = new HashMap<Long, String>();
			  for(Product product: list) {
				  Category category = categoryService.getCategoryById(product.getCategoryId());
				  if (category != null)
					  mapCats.put(product.getID(), category.getName());
			  }
			  
			map.addAttribute("list", list);
			map.addAttribute("mapCats", mapCats);
		    map.addAttribute("pageTitle", "SPORTY SHOES - HOMEPAGE");
		    mv.setViewName("index.jsp");
	        return mv; 
	    }	
	  
	  
}
