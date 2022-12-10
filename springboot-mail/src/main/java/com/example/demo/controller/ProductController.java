package com.example.demo.controller;




import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constant.ProductCategory;
import com.example.demo.dto.ProductQueryParams;
import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.demo.util.Page;

@Validated
@RestController
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<Page<Product>> getProducts(
			//查詢條件 Filtering
			@RequestParam(required = false) ProductCategory category, //因為category並不是必選的狀態，因此required=false這樣就不會綁住了
			@RequestParam(required = false) String search,
			
			//排序 sorting
			@RequestParam(defaultValue = "created_date") String orderBy,  //判斷要用甚麼欄位排序  因為正常來講都會預設最新的在最前面
			@RequestParam(defaultValue = "desc") String sort,  //要排大還是小
			
			//分頁 Pagination
			@RequestParam(defaultValue = "5") @Max(1000)@Min(0) Integer limit, //表示要取得幾筆參數
			@RequestParam(defaultValue = "0") @Min(0) Integer offset //跳過多少筆參數
			){
		
		ProductQueryParams productQueryParams = new ProductQueryParams();
		productQueryParams.setCategory(category);
		productQueryParams.setSearch(search);
		productQueryParams.setOrderBy(orderBy);
		productQueryParams.setSort(sort);
		productQueryParams.setLimit(limit);
		productQueryParams.setOffset(offset);
		
		//取得product list
		List<Product> productList = productService.getProducts(productQueryParams);
		
		//取得product總數
		Integer total = productService.countProduct(productQueryParams);
		
		//分頁
		Page<Product> page = new Page<>();
		page.setLimit(limit);
		page.setOffset(offset);
		page.setTotal(total);
		page.setResult(productList);
		
		return ResponseEntity.status(HttpStatus.OK).body(page);
		
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
	
	@DeleteMapping("/products/deleteAll")
	public ResponseEntity<Product> deleteProductsAll(){
		productService.deleteProductAll();
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	

}
