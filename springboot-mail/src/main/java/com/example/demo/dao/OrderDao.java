package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.OrderItem;

public interface OrderDao {
	
	Integer createOrder(Integer userId, Integer totalAmount);
	
	void createOrderItems(Integer userId, List<OrderItem> orderItemList);
	

}
