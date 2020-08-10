package com.springboot.microservices.composite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.springboot.microservices.core.api.Product;
import com.springboot.microservices.core.api.ProductService;

@Component
public class ProductCompositeIntegration implements ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);
	private final RestTemplate restTemplate;
	private final String productServiceUrl;

	@Autowired
	public ProductCompositeIntegration(RestTemplate restTemplate,

			@Value("${app.product-service.host}") String productServiceHost,
			@Value("${app.product-service.port}") int productServicePort) {

		this.restTemplate = restTemplate;

		// productServiceUrl = "http://" + productServiceHost + ":" + productServicePort
		// + "/product/";
		//productServiceUrl = "http://product-service:" + productServicePort + "/product/";
		productServiceUrl = "http://product:"+ productServicePort +"/product/";
	}

	@Override
	public Product createProduct(Product body) {

		String url = productServiceUrl;
		LOG.debug("Will post a new product to URL: {}", url);

		Product product = restTemplate.postForObject(url, body, Product.class);
		LOG.debug("Created a product with id: {}", product.getProductId());

		return product;

	}

	@Override
	public Product getProduct(int productId) {

		String url = productServiceUrl + productId;
		LOG.debug("Will call the getProduct API on URL: {}", url);

		Product product = restTemplate.getForObject(url, Product.class);
		LOG.debug("Found a product with id: {}", product.getProductId());

		return product;

	}

	@Override
	public void deleteProduct(int productId) {

		String url = productServiceUrl + productId;
		LOG.debug("Will call the deleteProduct API on URL: {}", url);

		restTemplate.delete(url);

	}

}
