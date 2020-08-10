package com.springboot.microservices.composite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.microservices.composite.api.ProductCompositeService;
import com.springboot.microservices.core.api.Product;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

	private ProductCompositeIntegration integration;

	@Autowired
	public ProductCompositeServiceImpl(ProductCompositeIntegration integration) {
		this.integration = integration;
	}

	@Override
	public void createCompositeProduct(Product body) {

		LOG.debug("createCompositeProduct: creates a new composite entity for productId: {}", body.getProductId());

		Product product = new Product(body.getProductId(), body.getName(), body.getWeight(), null);
		integration.createProduct(product);
		LOG.debug("createCompositeProduct: composite entites created for productId: {}", body.getProductId());
	}

	@Override
	public Product getCompositeProduct(int productId) {
		LOG.debug("getCompositeProduct: lookup a product aggregate for productId: {}", productId);

		Product product = integration.getProduct(productId);
		if (product == null)
			throw new RuntimeException("No product found for productId: " + productId);

		LOG.debug("getCompositeProduct: aggregate entity found for productId: {}", productId);

		return product;
	}

	@Override
	public void deleteCompositeProduct(int productId) {

		LOG.debug("deleteCompositeProduct: Deletes a product aggregate for productId: {}", productId);

		integration.deleteProduct(productId);

		LOG.debug("getCompositeProduct: aggregate entities deleted for productId: {}", productId);
	}

}