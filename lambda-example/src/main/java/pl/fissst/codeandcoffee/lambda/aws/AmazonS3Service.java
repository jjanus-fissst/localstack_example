package pl.fissst.codeandcoffee.lambda.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import pl.fissst.codeandcoffee.lambda.dto.ImportDataDto;

public class AmazonS3Service {

    private final AmazonS3 amazonS3;
    private final Context context;

    public AmazonS3Service(final AmazonS3 amazonS3, final Context context) {
        this.amazonS3 = amazonS3;
        this.context = context;
    }

    public ImportDataDto retrieveObjectToImport(S3EventNotification.S3Entity s3Entity) {
        context.getLogger().log(String.format("read from s3 %s file %s", s3Entity.getBucket().getName(), s3Entity.getObject().getUrlDecodedKey()));

        String data = amazonS3.getObjectAsString(s3Entity.getBucket().getName(), s3Entity.getObject().getUrlDecodedKey());

        ImportDataDto importDataDto = new ImportDataDto();

        importDataDto.setBucketName(s3Entity.getBucket().getName());
        importDataDto.setFileName(s3Entity.getObject().getUrlDecodedKey());
        importDataDto.setContent(data);

        return importDataDto;
    }
}