package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.Purchase;
import com.example.demo.entity.PurchaseItem;
import com.example.demo.service.ProductService;
import com.example.demo.service.PurchaseItemService;
import com.example.demo.service.PurchaseService;
 
@Controller
public class CartController {

	@Autowired
	private ProductService productService; 

	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private PurchaseItemService purchaseItemService; 

	ModelAndView mv =  new ModelAndView();

	  @SuppressWarnings("unchecked")
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	    public ModelAndView cart(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if user is logged in
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  map.addAttribute("error", "Error, You need to login before adding items to cart");
		  } else {
			  // if cart is already in session then retrieve it else create a new cart list and 
			  // save it to session
			  List<CartItem> cartItems = new ArrayList<CartItem>();
			  if (session.getAttribute("cart_items") != null)
				  cartItems = (List<CartItem>) session.getAttribute("cart_items");
			  
			  // get total of all cart items
			  BigDecimal totalValue = getCartValue(cartItems);
			  map.addAttribute("cartValue", totalValue);
			  map.addAttribute("cartItems", cartItems);
 		  }
		  
		  map.addAttribute("pageTitle", "SPORTY SHOES - YOUR CART");
		  mv.setViewName("cart.jsp");
	        return mv;
	    }
	  
	  @SuppressWarnings("unchecked")
	@RequestMapping(value = "/cartadditem", method = RequestMethod.GET)
	    public String cartAddItem(ModelMap map, javax.servlet.http.HttpServletRequest request,
	    		@RequestParam(value="id", required=true) String productId) 
	    {
		  // check if user is logged in
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  map.addAttribute("error", "Error, You need to login before adding items to cart");
		  } else {
			  
			  long idValue = Long.parseLong(productId);
			  // if cart is already in session then retrieve it else create a new cart list and 
			  // save it to session
			  List<CartItem> cartItems = new ArrayList<CartItem>();
			  if (session.getAttribute("cart_items") != null)
				  cartItems = (List<CartItem>) session.getAttribute("cart_items");
			  if (isItemInCart(cartItems, idValue)) {
				  map.addAttribute("error", "This item is already in your cart");
			  } else {
				  Product product = productService.getProductById(idValue);
				  CartItem item = new CartItem();
				  item.setProductId(idValue);
				  item.setQty(1);
				  item.setRate(product.getPrice());
				  BigDecimal dprice = item.getRate().multiply(new BigDecimal(item.getQty())); 
				  item.setPrice(dprice); 
				  item.setName(product.getName()); 
				  cartItems.add(item);
				  
				  session.setAttribute("cart_items", cartItems);
			  }
		  }
		  
	        return "redirect:cart"; 
	    }	  
	  @RequestMapping(value = "/cartdeleteitem", method = RequestMethod.GET)
	    public ModelAndView cartDeleteItem(ModelMap map, javax.servlet.http.HttpServletRequest request, 
	    		@RequestParam(value="id", required=true) String id) 
	    {
		  // check if user is logged in
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  map.addAttribute("error", "Error, You need to login before deleting items from cart");
		  } else {
			  long idValue = Long.parseLong(id);
			  List<CartItem> cartItems = new ArrayList<CartItem>();
			  if (session.getAttribute("cart_items") != null)
				  cartItems = (List<CartItem>) session.getAttribute("cart_items");
			  	  
			  for(CartItem item: cartItems) {
				  if (item.getProductId() == idValue) {
					  cartItems.remove(item);
					  session.setAttribute("cart_items", cartItems);
					  break;
				  }
			   }
		  }	
		  mv.setViewName("redirect:cart");
	      return mv;
	    }	

	  @RequestMapping(value = "/checkout", method = RequestMethod.GET)
	    public ModelAndView checkout(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if user is logged in
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  map.addAttribute("error", "Error, You need to login before checking out");
		  } else {
			  List<CartItem> cartItems = new ArrayList<CartItem>();
			  if (session.getAttribute("cart_items") != null)
				  cartItems = (List<CartItem>) session.getAttribute("cart_items");
			  BigDecimal totalValue = getCartValue(cartItems);
			  map.addAttribute("cartValue", totalValue);
			  map.addAttribute("cartItems", cartItems);
		  }
		  map.addAttribute("pageTitle", "SPORTY SHOES - CHECKOUT");
	        mv.setViewName("checkout.jsp");
	        return mv;
	    }

	  @RequestMapping(value = "/completepurchase", method = RequestMethod.GET)
	    public ModelAndView completePurchase(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if user is logged in
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  map.addAttribute("error", "Error, You need to login before completing purchase");
		  } else {
			  // take items from cart and update the databae 
			  List<CartItem> cartItems = new ArrayList<CartItem>();
			  if (session.getAttribute("cart_items") != null)
				  cartItems = (List<CartItem>) session.getAttribute("cart_items");
			  BigDecimal totalValue = getCartValue(cartItems);
			  
			  long userId = (Long) session.getAttribute("user_id") ;
			  
			  Purchase purchase = new Purchase();
			  purchase.setUserId(userId);
			  purchase.setDate(Calendar.getInstance().getTime());
			  purchase.setTotal(totalValue);
			  Purchase savedpurchase = purchaseService.save(purchase);
			  long purchaseId = savedpurchase.getUserId();
			  
			  for(CartItem item: cartItems) {
				  PurchaseItem pItem = new PurchaseItem();
				  pItem.setPurchaseId(purchaseId);
				  pItem.setProductId(item.getProductId());
				  pItem.setUserId(userId);
				  pItem.setRate(item.getRate());
				  pItem.setQty(item.getQty());
				  pItem.setPrice(item.getPrice());
				  
				  purchaseItemService.save(pItem);
			  }
			  map.addAttribute("cartValue", totalValue);
			  map.addAttribute("cartItems", cartItems);

		  }
		  
	        mv.setViewName("redirect:confirm");
	        return mv;
	    }

	  @RequestMapping(value = "/gateway", method = RequestMethod.GET)
	    public ModelAndView gateway(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if user is logged in
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  map.addAttribute("error", "Error, You need to login before making payment");
		  } else {
			  List<CartItem> cartItems = new ArrayList<CartItem>();
			  if (session.getAttribute("cart_items") != null)
				  cartItems = (List<CartItem>) session.getAttribute("cart_items");
			  BigDecimal totalValue = getCartValue(cartItems);
			  map.addAttribute("cartValue", totalValue);
			  map.addAttribute("cartItems", cartItems);

		  }
		  
		  map.addAttribute("pageTitle", "SPORTY SHOES - PAYMENT GATEWAY");
	        mv.setViewName("gateway.jsp");
	        return mv;
	    }
	  
	  @RequestMapping(value = "/confirm", method = RequestMethod.GET)
	    public ModelAndView confirm(ModelMap map, javax.servlet.http.HttpServletRequest request) 
	    {
		  // check if user is logged in
		  HttpSession session = request.getSession();
		  if (session.getAttribute("user_id") == null) {
			  map.addAttribute("error", "Error, You need to login before completing the purchase");
		  } else {
			  // clear items from cart as order has completed 
			  List<CartItem> cartItems = new ArrayList<CartItem>();
			  if (session.getAttribute("cart_items") != null)
				  cartItems = (List<CartItem>) session.getAttribute("cart_items");
			  BigDecimal totalValue = getCartValue(cartItems);
			  map.addAttribute("cartValue", totalValue);			  
			  cartItems.clear();
			  session.setAttribute("cart_items", null);
		  }
		  map.addAttribute("pageTitle", "SPORTY SHOES - PURCHASE CONFIRMATION");
	         mv.setViewName("confirm.jsp");
	         return mv;
	    }	  
	  
	  /**
	   * Check if an item is already in the cart
	   * @param list
	   * @param item
	   * @return
	   */
	  private boolean isItemInCart(List<CartItem> list, long item) {
		  boolean retVal = false;
		  
		  for(CartItem thisItem: list) {
			  if (item == thisItem.getProductId()) {
				  retVal = true;
				  break;
			  }
		  }
		  return retVal;
	  } 

	  /**
	   * Get total value of items in cart
	   * @param list
	   * @return
	   */
	  private BigDecimal getCartValue(List<CartItem> list) {
		  BigDecimal total = new BigDecimal(0.0);
		  
		  for(CartItem item: list) {
			  BigDecimal dprice = item.getRate().multiply(new BigDecimal(item.getQty()));
			  total= total.add(dprice);
		   }
		  return total;
	  }

}
