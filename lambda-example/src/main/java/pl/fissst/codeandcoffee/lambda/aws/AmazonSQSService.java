package pl.fissst.codeandcoffee.lambda.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.fissst.codeandcoffee.lambda.dto.ImportDataDto;

public class AmazonSQSService {

    private final static String QUEUE_NAME = "codeandcoffee-queue-test";

    private final AmazonSQS amazonSQS;
    private final Context context;

    public AmazonSQSService(AmazonSQS amazonSQS, Context context) {
        this.amazonSQS = amazonSQS;
        this.context = context;
    }

    public void sendToSQS(ImportDataDto importDataDto) {
        context.getLogger().log(String.format("send message to queue %s", QUEUE_NAME));
        ObjectMapper objectMapper = getObjectMapper();

        try {
            amazonSQS.sendMessage(amazonSQS.getQueueUrl(QUEUE_NAME).getQueueUrl(), objectMapper.writeValueAsString(importDataDto));
        } catch (JsonProcessingException e) {
            // do nothing
        }
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}