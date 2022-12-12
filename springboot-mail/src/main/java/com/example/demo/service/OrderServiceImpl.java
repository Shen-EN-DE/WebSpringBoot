package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dao.OrderDao;
import com.example.demo.dao.ProductDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dto.BuyItem;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.dto.OrderQueryParams;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;

import ch.qos.logback.classic.Logger;

@Component
public class OrderServiceImpl implements OrderService{
	
	private final static Logger log = (Logger) LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private UserDao userDao;
	
	@Transactional
	@Override
	public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
		// 檢查 user是否存在
		User user = userDao.getUserById(userId);
		
		if(user == null) {
			log.warn("該userId {} 不存在", userId);
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		
		
		
		int totalAmount = 0;
		List<OrderItem> orderItemList = new ArrayList<>();
		
		for(BuyItem buyItem : createOrderRequest.getBuyItemList()) {
			Product product = productDao.getProductById(buyItem.getProductId()); //取得式商品的id
			
			//檢查product 是否存在、庫存是否足夠
			if(product == null) {
				log.warn("商品{} 不存在", buyItem.getProductId());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}else if (product.getStock() < buyItem.getQuantity()) {
				log.warn("商品{} 庫存數量不足，無法購馬，剩餘庫存{}，欲購買數量 {}",
						buyItem.getProductId(),product.getStock(), buyItem.getQuantity());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				
			}
			
			productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());
			
			//計算總價錢
			int amount = buyItem.getQuantity() * product.getPrice();
			totalAmount = totalAmount + amount;
			
			//轉換buyItem to OrderItem
			OrderItem orderItem = new OrderItem();
			orderItem.setProductId(buyItem.getProductId());
			orderItem.setQuantity(buyItem.getQuantity());
			orderItem.setAmount(amount);
			
			orderItemList.add(orderItem);
		}
		
		
		
		//創建訂單
		Integer orderId = orderDao.createOrder(userId, totalAmount);
		
		orderDao.createOrderItems(orderId, orderItemList);
		
		return orderId;
	}

	@Override
	public Order getOrderById(Integer orderId) {
		Order order = orderDao.getOrderById(orderId);
		
		List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);
		order.setOrderItemList(orderItemList);
		
		return order;
	}

	@Override
	public Integer countOrder(OrderQueryParams orderQueryParams) {
		return orderDao.countOrder(orderQueryParams);
	}

	@Override
	public List<Order> getOrders(OrderQueryParams orderQueryParams) {
		List<Order> orderList = orderDao.getOrders(orderQueryParams);
		
		for(Order order: orderList) {
			List<OrderItem> orderItemsList = orderDao.getOrderItemsByOrderId(order.getOrderId());
			
			order.setOrderItemList(orderItemsList);
		}
		
		return orderList;
	}
	
	

}
