package pl.fissst.codeandcoffee.lambda.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import pl.fissst.codeandcoffee.lambda.dto.ImportDataDto;

import java.io.IOException;

public class AmazonS3Service {

    private final AmazonS3 amazonS3;
    private final Context context;

    public AmazonS3Service(final AmazonS3 amazonS3, final Context context) {
        this.amazonS3 = amazonS3;
        this.context = context;
    }

    public ImportDataDto retrieveObjectToImport(S3EventNotification.S3Entity s3Entity) {
        context.getLogger().log(String.format("read from s3 %s file %s", s3Entity.getBucket().getName(), s3Entity.getObject().getUrlDecodedKey()));

        S3Object obj = amazonS3.getObject(new GetObjectRequest(s3Entity.getBucket().getName(), s3Entity.getObject().getUrlDecodedKey()));

        ImportDataDto importDataDto = new ImportDataDto();

        try {
            importDataDto.setBucketName(s3Entity.getBucket().getName());
            importDataDto.setFileName(s3Entity.getObject().getUrlDecodedKey());
            importDataDto.setContent(String.valueOf(obj.getObjectContent().readAllBytes()));
        } catch (IOException e) {
            // do nothing
        }

        return importDataDto;
    }
}