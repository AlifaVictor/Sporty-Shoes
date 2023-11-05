package com.example.demo.entity;

import java.math.BigDecimal;

import lombok.Data;


/**
 * This is NOT a Hibernate class or a table class. This is a POJO which is used internally within the app
 * @author oem
 *
 */
@Data
public class CartItem { 

	private long productId;
	private String name;
	private BigDecimal rate;
	private BigDecimal price;
	private int qty;	
}
