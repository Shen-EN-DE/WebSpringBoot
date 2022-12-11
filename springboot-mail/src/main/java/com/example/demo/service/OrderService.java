package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.model.Order;

public interface OrderService {
	
	Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
	
	Order getOrderById(Integer orderId);

}
