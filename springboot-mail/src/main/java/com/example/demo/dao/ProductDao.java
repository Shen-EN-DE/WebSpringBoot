package com.example.demo.dao;

import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;

public interface ProductDao {
	
	Product getProductById(Integer productId);
	
	Integer createProduct(ProductRequest productRequest);
	
	void updateProduct(Integer productId, ProductRequest productRequest);

}
