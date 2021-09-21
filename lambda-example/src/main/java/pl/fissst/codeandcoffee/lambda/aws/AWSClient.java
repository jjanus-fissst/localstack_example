package pl.fissst.codeandcoffee.lambda.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class AWSClient {

    private static final String RUN_PROFILE = System.getenv("RUN_PROFILE");
    private static final String LOCALSTACK_HOSTNAME = System.getenv("LOCALSTACK_HOSTNAME");
    private static final String S3_ENDPOINT = String.format("http://%s:4566", LOCALSTACK_HOSTNAME);
    private static final String AWS_REGION = System.getenv("AWS_REGION");

    private static final String LOCAL_PROFILE = "local";

    public static AmazonS3Service getAmazonS3Client(Context context) {
        AmazonS3 amazonS3 = (LOCAL_PROFILE.equalsIgnoreCase(RUN_PROFILE)) ?
                getAmazonS3ClientLocal() : getAmazonS3ClientProd();

        return new AmazonS3Service(amazonS3, context);
    }

    private AWSClient() {
        // do nothing - all methods static
    }

    public static AmazonSQSService getAmazonSQSClient(Context context) {
        AmazonSQS amazonSQS = (LOCAL_PROFILE.equalsIgnoreCase(RUN_PROFILE)) ?
                getAmazonSQSClientLocal() : getAmazonSQSClientProd();

        return new AmazonSQSService(amazonSQS, context);
    }

    private static AmazonS3 getAmazonS3ClientLocal() {
        BasicAWSCredentials credentials = new BasicAWSCredentials("foo", "bar");

        AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(S3_ENDPOINT, AWS_REGION);

        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withPathStyleAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private static AmazonS3 getAmazonS3ClientProd() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(AWS_REGION)
                .build();
    }

    private static AmazonSQS getAmazonSQSClientLocal() {
        BasicAWSCredentials credentials = new BasicAWSCredentials("foo", "bar");

        AwsClientBuilder.EndpointConfiguration config =
                new AwsClientBuilder.EndpointConfiguration(S3_ENDPOINT, AWS_REGION);

        return AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private static AmazonSQS getAmazonSQSClientProd() {
        return AmazonSQSClientBuilder.standard()
                .withRegion(AWS_REGION)
                .build();
    }
}