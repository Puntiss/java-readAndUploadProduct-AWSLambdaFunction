# Description
This is an AWS Lambda function made using Java, if correctly configured allows you to load a product.txt file on an S3 Bucket and convert it into an insert query on AWS DynamoDB.

# Usage and Modify
**0. Prerequisites:**

- Install [SAM  1.97.0+](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html) or check if already installed with `sam --version`.
- Install [AWS CLI 2.13.20+](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html)  or check if already installed with `aws --version`.
- Install [Java SE JDK-17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or check if already installed with `java -version`.
- Install [Maven 3.8.4+](https://maven.apache.org/install.html) or check if already installed with `mvn -v`.

**1. Configure AWS User**

- Use the AWS IAM console to create a user with the AdministatorAccess policy and configure it into aws cli with `aws configure` and insert as parameters *access key*, *secret* (user > security credentials > access key), your [region](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Concepts.RegionsAndAvailabilityZones.html) and *json*. (You can test if all data are correctly configured with `aws iam get-user`)
- Use the AWS IAM console to create another user with the AmazonDynamoDBFullAccess policy and obtain the *access key* and *secret*  (user > security credentials > access key) with this user. After downloading the code, configure the *src/main/resurces/application.yml* with the new parameters.
> [!NOTE]
> Is safer to use two different users.

**2. Configure S3 Bucket**
- Create two AWS S3 BUCKETS using the user interface or with `aws s3 mb s3://NAME_OF_THE_BUCKET`. (BUCKET_PRODUCT_NAME for uploading the file `product.txt`, and BUCKET_BACKUP_NAME for storing the file after it is processed).
- Configure the *src/main/resurces/application.yml* with the name of the two buckets.

**3. Build**

- Install all project dependencies specified in the pom.xml file using `mvn clean install`, a target folder will be created.
- In the *template.yml* edit the CodeUri with the *.jar* file path.

**4. Deploy**

- Create an AWS S3 BUCKET using the user interface or with `aws s3 mb s3://NAME_OF_THE_BUCKET` to contain the code.
- In the same folder of the template.yml run `sam deploy --s3-bucket NAME_OF_THE_BUCKET_CREATED --stack-name CHOOSE_STACK_NAME --capabilities CAPABILITY_IAM`.

 **5. Grant policy**
- To configure a new event when a file is uploaded go in the BUCKET_PRODUCT_NAME buckets > Properties > Event notification > Create event with this values:
   | Property      	| Value                                 | 
   | -------------------|---------------------------------------|
   | Event Name    	| Upload                                | 
   | Prefix  	   	| *the product CSV file name or null*   |   
   | Suffix        	| *the product CSV file type or null*   |
   | Object Creation    | PUT      				|
   | Destination        | *choose your lambda function*     	|
  
- Authorize the AWS S3 BUCKET_PRODUCT_NAME to invoke the Lambda function using the command: `aws lambda add-permission --function-name LAMBDA_NAME_FUNCTION --principal s3.amazonaws.com --statement-id s3invoke --action "lambda:InvokeFunction" --source-arn arn:aws:s3:::BUCKET_PRODUCT_NAME --source-account ACCOUNT_NUMBER`.
- In the Lambda function navigate to: Configuration > Permission > Role > Add permissions > Create inline policy > add a new role using the following JSON
```json
{
	"Version": "2012-10-17",
	"Statement": [
		{
			"Sid": "VisualEditor0",
			"Effect": "Allow",
			"Action": [
				"s3:ListAllMyBuckets",
				"s3:GetBucketLocation"
			],
			"Resource": "*"
		},
		{
			"Sid": "VisualEditor1",
			"Effect": "Allow",
			"Action": "s3:*",
			"Resource": [
				"arn:aws:s3:::BUCKET_PRODUCT_NAME",
				"arn:aws:s3:::BUCKET_PRODUCT_NAME/*",
				"arn:aws:s3:::BUCKET_BACKUP_NAME/*"
			]
		}
	]
}
```

**5. Testing**
- Upload a file in the BUCKET_PRODUCT_NAME and wait until is processed and moved to the BUCKET_BACKUP_NAME
>[!WARNING]
>If pass much time and nothing happens, is possible to see log under LAMBDA_NAME_FUNCTION > Monitor > Logs

Happy coding!

 
