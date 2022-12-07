package com.example.demo.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.example.demo.constant.ProductCategory;

public class ProductRequest {
	
	//因為productId是mySql資料庫自動生成，因此可以刪掉，時間也是，前端自己傳過來就好
	@NotNull
	private String productName;
	
	@NotNull
	private ProductCategory category;
	
	@NotNull
	private String imageUrl;
	
	@NotNull
	private Integer price;
	
	@NotNull
	private Integer stock;
	
	
	private String description;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public ProductCategory getCategory() {
		return category;
	}
	public void setCategory(ProductCategory category) {
		this.category = category;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}



}
