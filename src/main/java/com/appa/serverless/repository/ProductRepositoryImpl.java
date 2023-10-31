package com.appa.serverless.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.appa.serverless.model.Product;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

	private static final java.util.logging.Logger log = java.util.logging.Logger
			.getLogger(ProductRepositoryImpl.class.getName());

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	@Override
	public void manageProduct(Product product) {

		log.info("Saving product n: " + product.getId());
//		Product p = dynamoDBMapper.load(Product.class, product.getId());
//		if (p != null) {
//			dynamoDBMapper.delete(p);
//		}

		dynamoDBMapper.save(product);
	}

}
