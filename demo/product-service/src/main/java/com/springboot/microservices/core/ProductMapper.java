package com.springboot.microservices.core;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.springboot.microservices.core.api.Product;
import com.springboot.microservices.core.persistence.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	
	 @Mappings({
	        @Mapping(target = "serviceAddress", ignore = true)
	    })
	    Product entityToApi(ProductEntity entity);

	    @Mappings({
	        @Mapping(target = "id", ignore = true),
	        @Mapping(target = "version", ignore = true)
	    })
	    ProductEntity apiToEntity(Product api);

}
