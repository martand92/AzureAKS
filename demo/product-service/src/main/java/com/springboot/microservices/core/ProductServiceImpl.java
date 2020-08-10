package com.springboot.microservices.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.DuplicateKeyException;
import com.springboot.microservices.core.api.Product;
import com.springboot.microservices.core.api.ProductService;
import com.springboot.microservices.core.persistence.ProductEntity;
import com.springboot.microservices.core.persistence.ProductRepository;

@RestController
public class ProductServiceImpl implements ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	private final ProductRepository repository;

	private final ProductMapper mapper;

	@Autowired
	public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;

	}

	@Override
	public Product createProduct(Product body) {
		try {
			
			LOG.debug("creatingProduct: entity getting created for productId: {}", body.getProductId());
			ProductEntity entity = mapper.apiToEntity(body);
			ProductEntity newEntity = repository.save(entity);

			LOG.debug("createProduct: entity created for productId: {}", body.getProductId());
			return mapper.entityToApi(newEntity);

		} catch (DuplicateKeyException dke) {
			throw new RuntimeException("Duplicate key, Product Id: " + body.getProductId());
		}
	}

	@Override
	public Product getProduct(int productId) {

		if (productId < 1)
			throw new RuntimeException("Invalid productId: " + productId);

		ProductEntity entity = repository.findByProductId(productId)
				.orElseThrow(() -> new RuntimeException("No product found for productId: " + productId));

		Product response = mapper.entityToApi(entity);
		response.setServiceAddress("aa");

		LOG.debug("getProduct: found productId: {}", response.getProductId());

		return response;
	}

	@Override
	public void deleteProduct(int productId) {
		LOG.debug("deleteProduct: tries to delete an entity with productId: {}", productId);
		repository.findByProductId(productId).ifPresent(e -> repository.delete(e));
	}
}