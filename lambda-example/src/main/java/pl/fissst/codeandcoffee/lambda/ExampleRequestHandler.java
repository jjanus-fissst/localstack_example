package pl.fissst.codeandcoffee.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import pl.fissst.codeandcoffee.lambda.aws.AWSClient;
import pl.fissst.codeandcoffee.lambda.aws.AmazonS3Service;
import pl.fissst.codeandcoffee.lambda.aws.AmazonSQSService;

public class ExampleRequestHandler implements RequestHandler<S3EventNotification, Void> {

    @Override
    public Void handleRequest(S3EventNotification input, Context context) {
        AmazonS3Service amazonS3Service = AWSClient.getAmazonS3Client(context);
        AmazonSQSService amazonSQSService = AWSClient.getAmazonSQSClient(context);

        input.getRecords().stream()
                .map(r -> amazonS3Service.retrieveObjectToImport(r.getS3()))
                .forEach(amazonSQSService::sendToSQS);

        return null;
    }
}
