package com.example.demo.controller;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.service.OrderService;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/users/{userId}/orders")  //要先有帳號才可以下訂單
	public ResponseEntity<?> createOrder(@PathVariable Integer userId,
										 @RequestBody @Valid CreateOrderRequest createOrderRequest){
		
		Integer orderId = orderService.createOrder(userId, createOrderRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
		
	}

}
