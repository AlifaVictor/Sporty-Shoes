package com.example.demo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;  

@Data
@Entity
@Table(name= "purchases")   
public class Purchase { 


	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long ID;
	
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "date")
	private Date date;
	
	@Column(name = "gross_total")
	private BigDecimal total;

	
}
