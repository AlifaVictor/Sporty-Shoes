package com.example.demo.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;  

@Data
@Entity
@Table(name= "purchase_items")   
public class PurchaseItem { 


	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long ID;
	
	@Column(name = "purchase_id")
	private long purchaseId;
	
	@Column(name = "product_id")
	private long productId;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "rate")
	private BigDecimal rate;

	@Column(name = "qty")
	private int qty;

	@Column(name = "price")
	private BigDecimal price;


}
