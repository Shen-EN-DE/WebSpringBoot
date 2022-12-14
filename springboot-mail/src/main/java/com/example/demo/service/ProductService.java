package com.example.demo.service;

import java.util.List;

import com.example.demo.constant.ProductCategory;
import com.example.demo.dto.ProductQueryParams;
import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;

public interface ProductService {
	
	List<Product> getProducts(ProductQueryParams productQueryParams);
	
	Integer countProduct(ProductQueryParams productQueryParams);
	
	Product getProductById(Integer productId);
	
	Integer createProduct(ProductRequest productRequest);
	
	void updateProduct(Integer productId, ProductRequest productRequest);
	
	void deleteProductById(Integer productId);
	
	void deleteProductAll();
	
	

}
