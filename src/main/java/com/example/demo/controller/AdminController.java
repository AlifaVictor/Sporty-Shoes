package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Purchase;
import com.example.demo.entity.PurchaseItem;
import com.example.demo.entity.User;
import com.example.demo.service.AdminService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.PurchaseItemService;
import com.example.demo.service.PurchaseService;
import com.example.demo.service.UserService;

@Controller
public class AdminController {
	
	@Autowired
    private AdminService adminService; 
	
	@Autowired
	private CategoryService categoryService; 

	@Autowired
	private ProductService productService; 

	@Autowired
	private PurchaseService purchaseService; 

	@Autowired
	private PurchaseItemService purchaseItemService; 
	
	@Autowired
	private UserService userService; 
	
	
	private String FOLDER = "C:\\image\\";
	
	  @RequestMapping(value = "/adminlogin", method = RequestMethod.GET)
	    public ModelAndView login(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  mv.setViewName("admin/login.jsp");
		  map.addAttribute("pageTitle", "ADMIN LOGIN");
	     return mv; 
	    }
	  
	  @RequestMapping(value = "/adminloginaction", method = RequestMethod.POST)
	    public ModelAndView loginAction(ModelMap map, javax.servlet.http.HttpServletRequest request, 
	    		 @RequestParam(value="admin_id", required=true) String adminId,
	    		 @RequestParam(value="admin_pwd", required=true) String adminPwd) 
	    {
		  ModelAndView mv =  new ModelAndView();
		  Admin admin = adminService.authenticate(adminId, adminPwd);
		  if (admin == null) { 
			  map.addAttribute("error", "Admin login failed");
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  // store admin id in session
		  HttpSession session = request.getSession();
		  session.setAttribute("admin_id", admin.getID());
		  mv.setViewName("admin/dashboard.jsp");
	        return mv;
	    }	  
	    
	  @RequestMapping(value = "/admindashboard", method = RequestMethod.GET)
	    public ModelAndView dashboard(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		   
		  map.addAttribute("pageTitle", "ADMIN DASHBOARD");
		  mv.setViewName("admin/dashboard.jsp");
	        return mv;
	    }
	  
	   
	  @RequestMapping(value = "/adminchangepassword", method = RequestMethod.GET)
	    public ModelAndView changePwd(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) 
		  {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  
		  Optional<Admin> admin = adminService.findById((Long) session.getAttribute("admin_id"));
		  
		  map.addAttribute("admin", admin);
		  map.addAttribute("pageTitle", "ADMIN CHANGE PASSWORD");
		  mv.setViewName("admin/change-password.jsp");
	        return mv;
	    }

	  
	  @RequestMapping(value = "/adminchangepwdaction", method = RequestMethod.POST)
	    public ModelAndView updatePassword(ModelMap map,  @RequestParam(value="pwd", required=true) String pwd,
	    		 @RequestParam(value="pwd2", required=true) String pwd2, 
	    		 javax.servlet.http.HttpServletRequest request)
	    {
		  ModelAndView mv = new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  
		  
		  if (pwd == null || pwd2 == null || pwd.equals("") || pwd2.equals("")) {
			  map.addAttribute("error", "Error , Incomplete passwords submitted.");
			  mv.setViewName("admin/change-password.jsp");
			  return mv;
		  }
		  if (!pwd.equals(pwd2)) {
			  map.addAttribute("error", "Error , Passwords do not match.");
			  mv.setViewName("admin/change-password.jsp");
			  return  mv;
		  }
		 Optional<Admin> admin = adminService.findById((Long) session.getAttribute("admin_id")); 
		 
		  admin.get().setPwd(pwd);
		  adminService.save(admin.get());
		   mv.setViewName("admin/dashboard.jsp");  
	        return mv;
	    }		  

	  
	  @RequestMapping(value = "/adminproducts", method = RequestMethod.GET)
	    public ModelAndView products(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if session is still alive
		  ModelAndView mv = new ModelAndView();
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  List<Product> list = productService.findAll();

		  // use a MAP to link category names to each product in list  
		  HashMap<Long, String> mapCats = new HashMap<Long, String>();
		  
		  for(Product product: list) {
			  Category category = categoryService.getCategoryById(product.getCategoryId());
			  if (category != null)
				  mapCats.put(product.getID(), category.getName());
		  }
		  map.addAttribute("list", list);
		  map.addAttribute("mapCats", mapCats);

		  map.addAttribute("pageTitle", "ADMIN SETUP PRODUCTS");
		  mv.setViewName("admin/products.jsp");
	        return mv;
	    }
	  
	  @RequestMapping(value = "/admincategories", method = RequestMethod.GET)
	    public ModelAndView categories(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
	    } 
		  
		  List<Category> list = categoryService.findAll();
		  map.addAttribute("list", list);
		  map.addAttribute("pageTitle", "ADMIN SETUP PRODUCT CATEGORIES");
	       mv.setViewName("admin/categories.jsp");
	      return mv;
	    }
	  
	  
	  @RequestMapping(value = "/adminmembers", method = RequestMethod.GET)
	    public ModelAndView members(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv= new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  List<User> list = userService.findAll();
		  
		  map.addAttribute("list", list);
		  map.addAttribute("pageTitle", "ADMIN BROWSE MEMBERS");
	        mv.setViewName("admin/members.jsp");
	        return mv;
	    }
	  
	  @RequestMapping(value = "/adminpurchases", method = RequestMethod.GET)
	    public ModelAndView purchases(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  
		  List<Purchase> list = purchaseService.findAll();
		  
		  BigDecimal total = new BigDecimal(0.0);
		  
		  for(Purchase purchase: list) {
			  total = total.add(purchase.getTotal());
		  }
		  
		  // use MAPs to mape users to each purchase and item names to each purchase item row
		  HashMap<Long, String> mapItems = new HashMap<Long, String>();
		  HashMap<Long, String> mapUsers = new HashMap<Long, String>();
		  
		  for(Purchase purchase: list) {
			  total = total.add(purchase.getTotal());
			  User user = userService.getUserById(purchase.getUserId());
			  if (user != null)
				  mapUsers.put(purchase.getID(), user.getFname() + " " + user.getLname());
			  
			  List<PurchaseItem> itemList = purchaseItemService.getAllItemsByPurchaseId(purchase.getID());
			  StringBuilder sb = new StringBuilder(""); 
			  for(PurchaseItem item: itemList) {
				  Product product = productService.getProductById(item.getProductId());
				  if (product != null)
					  sb.append(product.getName() + ", " + 
						  	item.getQty() + " units @" + item.getRate() + " = " 
						  	+ item.getPrice() + "<br>");
			  }
			  mapItems.put(purchase.getID(), sb.toString());
		  }		  
		  map.addAttribute("totalAmount", total); 
		  map.addAttribute("list", list);
		  map.addAttribute("mapItems", mapItems);
		  map.addAttribute("mapUsers", mapUsers);
		  map.addAttribute("pageTitle", "ADMIN PURCHASES REPORT");
	       mv.setViewName("admin/purchases.jsp");
		  return mv;  
	    }	  

	  @RequestMapping(value = "/admindeletecat",  method = RequestMethod.GET)
	    public ModelAndView deleteCategory(ModelMap map,  @RequestParam(value="id", required=true) String id,
	    		javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if session is still alive
		  ModelAndView mv = new ModelAndView();
		  
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  long idValue = Long.parseLong(id);
		  Category category = new Category();
		  if (idValue > 0) {
			  categoryService.deleteById(idValue);
		  }
		  mv.setViewName("admin/admincategories.jsp");
		  return mv;
	    }	
	  
	  
	  @RequestMapping(value = "/admineditcat",  method = RequestMethod.GET)
	    public ModelAndView editCategory(ModelMap map,  @RequestParam(value="id", required=true) String id,
	    		javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login.jsp");
			  return mv;
		  }
		  long idValue = Long.parseLong(id);
		  Category category = new Category();
		  if (idValue > 0) {
			  category = categoryService.getCategoryById(idValue);
		  } else {
			  category.setID(0);
		  }
		  map.addAttribute("category", category);
		  map.addAttribute("pageTitle", "ADMIN EDIT PRODUCT CATEGORY");
	      mv.setViewName("admin/edit-category.jsp");
	      return mv;
	    }		  

	  
	  @RequestMapping(value = "/admineditcataction", method = RequestMethod.POST)
	    public ModelAndView updateCategory(ModelMap map,  @RequestParam(value="id", required=true) String id,
	    		 @RequestParam(value="name", required=true) String name, 
	    		 javax.servlet.http.HttpServletRequest request)
	    {
		  long idValue = Long.parseLong(id); 
		  ModelAndView mv = new ModelAndView();
		  if (name == null || name.equals("") ) { 
			  map.addAttribute("error", "Error, A category name must be specified");
			  mv.setViewName("redirect:admineditcat?id="+id);
			  return mv;
		  }
		  	Category category = new Category();
		  	category.setID(idValue); 
		  	category.setName(name);
		  	
		  	categoryService.save(category); 
		  	
	        mv.setViewName("redirect:admincategories");
	        return mv;
	    }
	  
	  @RequestMapping(value = "/admindeleteproduct",  method = RequestMethod.GET)
	    public ModelAndView deleteProduct(ModelMap map,  @RequestParam(value="id", required=true) String id,
	    		javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  // check if session is still alive
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login");
			  return mv;
		  }
		  long idValue = Long.parseLong(id);
		  Product product = new Product();
		  if (idValue > 0) {
			  productService.deleteById(idValue);
		  }
		  mv.setViewName("redirect:adminproducts");
		  return mv;
	    }	
	  

	  
	  @RequestMapping(value = "/admineditproduct",  method = RequestMethod.GET)
	    public ModelAndView editProduct(ModelMap map,  @RequestParam(value="id", required=true) String id,
	    		javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if session is still alive
		  ModelAndView mv =  new ModelAndView();
		  HttpSession session = request.getSession();
		  if (session.getAttribute("admin_id") == null) {
			  mv.setViewName("admin/login");
			  return mv;
		  }
		  
		  long idValue = Long.parseLong(id);
		  Product product = new Product();
		  if (idValue > 0) {
			  product = productService.getProductById(idValue);
		  } else {
			  product.setID(0);
			  product.setCategoryId(0);
		  }
		  String dropDown = getCategoriesDropDown(product.getCategoryId());
		  map.addAttribute("product", product);
		  map.addAttribute("catDropdown", dropDown);
		  map.addAttribute("pageTitle", "ADMIN EDIT PRODUCT");
	        mv.setViewName("admin/edit-product.jsp");
	        return mv;
	    }	
	  
	  public String getCategoriesDropDown(long id) {
			 StringBuilder sb = new StringBuilder("");
			 List<Category> list = categoryService.findAll();
			 for(Category cat: list) {
				 if (cat.getID() == id)
					 sb.append("<option value=" + String.valueOf(cat.getID()) + " selected>" + cat.getName() + "</option>");
				 else
					 sb.append("<option value=" + String.valueOf(cat.getID()) + ">" + cat.getName() + "</option>");
					 
			 }
			 return sb.toString();
			}
	  
	  @RequestMapping(value = "/admineditproductaction", method = RequestMethod.POST)
	    public ModelAndView updateProduct(ModelMap map, javax.servlet.http.HttpServletRequest request,
	    		 @RequestParam(value="id", required=true) String id,
	    		 @RequestParam(value="name", required=true) String name,
	    		 @RequestParam(value="price", required=true) String price,
	    		 @RequestParam(value="category", required=true) String categoryId,
	    		 @RequestParam(value="filepath", required=true) MultipartFile  filepath)
	    {
		  ModelAndView mv = new ModelAndView();
		  long idValue = Long.parseLong(id); 
		  long categoryIdValue = Long.parseLong(categoryId);
		  BigDecimal priceValue = new BigDecimal(0.0);
		  
		  if (name == null || name.equals("") ) { 
			  map.addAttribute("error", "Error, A product name must be specified");
			  mv.setViewName("redirect:admineditproduct?id="+id);
			  return mv;
		  }
		  try {
			  priceValue = new BigDecimal(price);
			  
		  } catch (Exception ex) {
			  map.addAttribute("error", "Error, Price is invalid");
			  mv.setViewName("redirect:admineditproduct?id="+id);
			  return mv;
		  }
		  
		  	Product product = new Product();
		  	product.setID(idValue); 
		  	product.setCategoryId(Long.parseLong(categoryId));
		  	product.setName(name);
		  	product.setPrice(priceValue);
		  	product.setDateAdded(new Date());
		  	product.setFilepath(FOLDER + filepath.getOriginalFilename());
		  	
		  	productService.save(product); 
		  	
	        mv.setViewName("redirect:adminproducts");
	        return mv;
	    }	
	   
	  
	  
	  @RequestMapping(value = "/adminlogout", method = RequestMethod.GET)
	    public ModelAndView logout(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  ModelAndView mv = new ModelAndView();
		  	HttpSession session = request.getSession();
		  	session.invalidate();
		  	
	        mv.setViewName("admin/login.jsp");
	        return mv;
	    }
}

