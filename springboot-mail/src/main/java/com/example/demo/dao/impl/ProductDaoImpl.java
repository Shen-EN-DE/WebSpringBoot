package com.example.demo.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.ProductDao;
import com.example.demo.dto.OrderQueryParams;
import com.example.demo.dto.ProductQueryParams;
import com.example.demo.dto.ProductRequest;
import com.example.demo.model.Order;
import com.example.demo.model.Product;
import com.example.demo.service.OrderService;
import com.example.demo.service.OrderServiceImpl;
import com.example.demo.util.Page;

import rowmapper.ProductRowMapper;

@Component
public class ProductDaoImpl implements ProductDao{
	


	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private OrderService orderService;
	
	
	@GetMapping("/users/{userId}/orders")
	public ResponseEntity<Page<Order>> getOrders(
			@PathVariable Integer userId,
			@RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
			@RequestParam(defaultValue = "0") @Min(0) Integer offset
			){
		OrderQueryParams orderQueryParams = new OrderQueryParams();
		orderQueryParams.setLimit(limit);
		orderQueryParams.setOffset(offset);
		orderQueryParams.setUserId(userId);
		
		//取得order list
		List<Order> orderList = orderService.getOrders(orderQueryParams);
		
		//取得order總數
		Integer count = orderService.countOrder(orderQueryParams);
		
		//分頁
		Page<Order> page = new Page<>();
		page.setLimit(limit);
		page.setOffset(offset);
		page.setTotal(count);
		page.setResult(orderList);
		
		return ResponseEntity.status(HttpStatus.OK).body(page);
	}
	
	
	@Override
	public List<Product> getProducts(ProductQueryParams productQueryParams) {
		String sql = "SELECT product_id, product_name, category, image_url, "
				+ "price, stock, description, created_date, last_modified_date "
				+ "FROM product WHERE 1=1";
		
		Map<String, Object> map = new HashMap<>();
		
		//查詢條件
		sql = addFilteringSql(sql, map, productQueryParams); //因為getProduct和countProduct都會用到一樣的，因此可以寫一個function拆分
		
		//排序
		sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();
		
		//分頁
		sql = sql + " LIMIT :limit OFFSET :offset";
		map.put("limit", productQueryParams.getLimit());
		map.put("offset", productQueryParams.getOffset());
		
		
		List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
		
		return productList;
	}

	@Override
	public Product getProductById(Integer productId) {
		String sql = "SELECT product_id, product_name, category, image_url, "
				+ "price, stock, description, created_date, last_modified_date "
				+ "FROM product WHERE product_id = :productId";
		Map<String, Object> map = new HashMap<>();
		map.put("productId", productId);
		
		List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
		
		if (productList.size() > 0) {
			return productList.get(0);
		}else {
			return null;
		}
		
		
	}
	
	@Override
	public Integer createProduct(ProductRequest productRequest) {
		String sql = "INSERT INTO product(product_name, category, image_url, price, stock,"
				+ " description, created_date, last_modified_date) "
				+ "VALUES (:productName, :category, :imageUrl, :price, :stock, :description,"
				+ ":createDate, :lastModifiedDate)";
		Map<String, Object> map = new HashMap<>();
		map.put("productName", productRequest.getProductName());
		map.put("category", productRequest.getCategory().toString());
		map.put("imageUrl", productRequest.getImageUrl());
		map.put("price", productRequest.getPrice());
		map.put("stock", productRequest.getStock());
		map.put("description", productRequest.getDescription());
		
		Date now = new Date();
		map.put("createDate", now);
		map.put("lastModifiedDate", now);
		
		//KeyHolder 是用來自動生成productId 的值
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
		
		int productId = keyHolder.getKey().intValue();
		
		return productId;

	}
	
	@Override
	public void updateProduct(Integer productId, ProductRequest productRequest) {
		String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl"
				+ ", price = :price, stock = :stock,"
				+ " description = :description, last_modified_date = :lastModifiedDate"
				+ " WHERE product_id = :productId";
		
		Map<String, Object> map = new HashMap<>();
		map.put("productId", productId);
		map.put("productName", productRequest.getProductName());
		map.put("category", productRequest.getCategory().toString());
		map.put("imageUrl", productRequest.getImageUrl());
		map.put("price", productRequest.getPrice());
		map.put("stock", productRequest.getStock());
		map.put("description", productRequest.getDescription());	
		
		map.put("lastModifiedDate", new Date());
		
		namedParameterJdbcTemplate.update(sql, map);
		
	}

	@Override
	public void deleteProductById(Integer productId) {
		String str = "DELETE FROM product WHERE product_id = :productId";
		
		Map<String, Object> map = new HashMap<>();
		map.put("productId", productId);
		
		namedParameterJdbcTemplate.update(str, map);
	}

	@Override
	public void deleteProductAll() {
		String sql = "DELETE FROM product";
		
		Map<String, Object> map = new HashMap<>();
		
		namedParameterJdbcTemplate.update(sql, map);

	}

	@Override
	public Integer countProduct(ProductQueryParams productQueryParams) {
		String sql = "SELECT count(*) FROM product WHERE 1=1";
		
		Map<String, Object> map = new HashMap<>();
		
		//查詢條件
		sql = addFilteringSql(sql, map, productQueryParams);
		
		Integer total =  namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class); //Integer.class是因為我們要回傳的參數是要式Integer類型
		
		return total;
	}
	
	
	private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams) {
		//查詢條件
		if (productQueryParams.getCategory() != null) {
			//為甚麼用Where 1=1因為她條件一定是對的，只是單純要有一個WHERE條件而已，理由是沒有這個得話，如果多個條件，這樣每個SQL後面都要在加WHERE很麻煩
			//，當如果不等於null的話，這樣就可以直接在後面加條件，字串+字串的意思
			sql = sql + " AND category = :category"; 
			map.put("category", productQueryParams.getCategory().name()); // 因為我們是用enum為參數，因此要用.name
		}
		
		if (productQueryParams.getSearch() != null) {
			sql = sql + " AND product_name LIKE :search";
			map.put("search", "%" + productQueryParams.getSearch() + "%");  //會友百分比是因為LIKE ，因為她是只要有包含關鍵字的都抓出來，像是"蘋果%"代表的是開頭是蘋果的都抓出來
		}
		
		return sql;
	}
	@Override
	public void updateStock(Integer productId, Integer stock) {
		String sql = "UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate "
				+ "WHERE product_id = :productId";
		Map<String, Object> map = new HashMap<>();
		map.put("stock", stock);
		map.put("productId", productId);
		map.put("lastModifiedDate", new Date());
		
		namedParameterJdbcTemplate.update(sql, map);
	}
	
}
