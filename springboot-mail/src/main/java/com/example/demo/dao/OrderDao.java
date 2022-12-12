package com.example.demo.dao;

import java.util.List;

import com.example.demo.dto.OrderQueryParams;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;

public interface OrderDao {
	
	Integer createOrder(Integer userId, Integer totalAmount);
	
	void createOrderItems(Integer userId, List<OrderItem> orderItemList);
	
	Order getOrderById(Integer orderId);
	
	List<OrderItem> getOrderItemsByOrderId(Integer orderId);
	
	Integer countOrder(OrderQueryParams orderQueryParams);
	
	List<Order> getOrders(OrderQueryParams orderQueryParams);
	
	

}
