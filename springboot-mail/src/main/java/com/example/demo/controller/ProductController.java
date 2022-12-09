package com.example.demo.controller;




import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProducts(){
		List<Product> productList = productService.getProducts();
		
		return ResponseEntity.status(HttpStatus.OK).body(productList);
		
	}
	
	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
		Product product = productService.getProductById(productId);
				
		if (product != null) {
			return ResponseEntity.status(HttpStatus.OK).body(product);
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
	}
	
	//@Valid因為有設定NotNull因此需要記得加   然後在創建一個class_productRequest是因為這樣比較有彈性，才可以自己加@Notnull等等功能
	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
		Integer productId = productService.createProduct(productRequest);
		
		Product product = productService.getProductById(productId);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}
	
	
	@PutMapping("/products/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
												 @RequestBody @Valid ProductRequest productRequest){
		
		//判斷是否有這筆
		Product product = productService.getProductById(productId);
		if(product == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		//修改商品數據
		productService.updateProduct(productId, productRequest);
		Product updateProduct = productService.getProductById(productId);
		
		return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
		
	}
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Product> deleteProduct(@PathVariable Integer productId){
		
		//判斷是否有這筆  不用寫這行是因為，目的是刪掉商品，因此有沒有存在沒有差
//		Product product = productService.getProductById(productId);
//		if(product == null) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
//		}
		
		productService.deleteProductById(productId);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
	}
	

}
