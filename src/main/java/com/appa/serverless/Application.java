package com.appa.serverless;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.appa.serverless.model.Product;
import com.appa.serverless.repository.ProductRepository;

@SpringBootApplication

public class Application {

	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(Application.class.getName());

	@Autowired
	private ProductRepository productRepository;

	@Value("${buckets.product}")
	private String bucketProduct;

	@Value("${buckets.backup}")
	private String bucketBuckup;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	// the handler is org.springframework.cloud.function.adapter.aws.FunctionInvoker
	Function<S3Event, String> saluti() {
		return value -> this.apply(value);
	}

	public String apply(S3Event event) {

		log.info("Application.run()");

		String bucketName = event.getRecords().get(0).getS3().getBucket().getName();
		log.info("bucketName=" + bucketName);
		log.info("bucketProduct=" + bucketProduct);

		String srcKey = event.getRecords().get(0).getS3().getObject().getKey();
		log.info("srcKey=" + srcKey);

		AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

		S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, srcKey));

		InputStream objectData = s3Object.getObjectContent();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(objectData));

			String line;

			while ((line = reader.readLine()) != null) {

				String[] Items = line.split(";");
				if (!Items[0].equals("0000"))// title id
					productRepository.manageProduct(new Product(Items[0], Items[1], Items[2], Items[3], Items[4],
							Items[5], Items[6], Items[7]));
			}

			reader.close();

			CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, srcKey, bucketBuckup, srcKey);
			s3Client.copyObject(copyObjRequest);

			s3Client.deleteObject(bucketName, srcKey);

			return "Products read from file loaded";

		} catch (Exception e) {
			return "Error:" + e.getMessage();
		}

	}

}
