package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.constant.ProductCategory;
import com.example.demo.dao.ProductDao;
import com.example.demo.dto.ProductQueryParams;
import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;

@Component
public class ProductServiceImpl implements ProductService{
	




	@Autowired
	private ProductDao productDao;
	
	@Override
	public Product getProductById(Integer productId) {
		
		return productDao.getProductById(productId);
		
		
	}
	
	@Override
	public Integer createProduct(ProductRequest productRequest) {
		return productDao.createProduct(productRequest);
	}
	
	@Override
	public void updateProduct(Integer productId, ProductRequest productRequest) {
		
		productDao.updateProduct(productId, productRequest);
	}

	@Override
	public void deleteProductById(Integer productId) {
		
		productDao.deleteProductById(productId);
		
	}

	@Override
	public List<Product> getProducts(ProductQueryParams productQueryParams) {
		return productDao.getProducts(productQueryParams);
	}

	@Override
	public void deleteProductAll() {
		productDao.deleteProductAll();
	}

	@Override
	public Integer countProduct(ProductQueryParams productQueryParams) {
		return productDao.countProduct(productQueryParams);
	}
	
	
	
	

	
}
