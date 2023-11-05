package com.example.demo.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.PurchaseItem; 

@Repository
@Component
public class PurchaseItemDAO {

	@Autowired
    private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public PurchaseItem getItemById(long id) {
		String strId = String.valueOf(id);
		List<PurchaseItem> list = this.sessionFactory.getCurrentSession().createQuery("from PurchaseItem where id=" + strId).list();
		if (list.size() > 0)
			return (PurchaseItem) list.get(0);
		else
			return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PurchaseItem> getAllItemsByPurchaseId(long purchaseId) {
		String strId = String.valueOf(purchaseId);
		List<PurchaseItem> list = this.sessionFactory.getCurrentSession().createQuery("from PurchaseItem where purchase_id=" + strId).list();
		return list;
	}	
	
	

	
}
