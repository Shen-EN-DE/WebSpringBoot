package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;

public interface ProductService {
	
	List<Product> getProducts();
	
	Product getProductById(Integer productId);
	
	Integer createProduct(ProductRequest productRequest);
	
	void updateProduct(Integer productId, ProductRequest productRequest);
	
	void deleteProductById(Integer productId);
	
	

}
