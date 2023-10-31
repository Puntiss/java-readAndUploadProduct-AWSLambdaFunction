package com.appa.serverless.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "product")
public class Product implements Serializable {

	private static final long serialVersionUID = 1554640551199966153L;

	@DynamoDBHashKey(attributeName = "id")
	private String id;

	@DynamoDBAttribute(attributeName = "imgPath")
	private String imgPath;

	@DynamoDBAttribute(attributeName = "cost")
	private String cost;	
	
	@DynamoDBAttribute(attributeName = "discountCost")
	private String discountCost;
	
	@DynamoDBAttribute(attributeName = "name")
	private String name;	
	
	@DynamoDBAttribute(attributeName = "desc")
	private String desc;	
	
	@DynamoDBAttribute(attributeName = "category")
	private String category;
	
	@DynamoDBAttribute(attributeName = "qtyAvaiable")
	private String qtyAvaiable;
	
}
