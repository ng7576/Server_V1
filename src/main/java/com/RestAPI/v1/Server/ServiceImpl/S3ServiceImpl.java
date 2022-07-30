package com.RestAPI.v1.Server.ServiceImpl;

import com.RestAPI.v1.Server.IService.IuploadService;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;


@Service

@Data
@NoArgsConstructor
public class S3ServiceImpl implements IuploadService {


//    private final AmazonS3Client awsClient;
    private static final String BUCKET_NAME = "YourBucketName";




    @Override
    public String uploadFile(String imgString ) {
        //This will be implemented only when all the aws settings are configured properly in application properties

        var decodedBytes = Base64.getDecoder().decode(imgString);
        File avatar = new File("imgUserAvatar");
        try {
            FileUtils.writeByteArrayToFile(avatar, decodedBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//
//       var key = UUID.randomUUID().toString() + "imgUserAvatar";
//       var metaData = new ObjectMetadata();
//
//
//       metaData.setContentLength(avatar.getSize());
//       metaData.setContentType(avatar.getContentType());
//
//        try {
//            awsClient.putObject(BUCKET_NAME, key, avatar.getInputStream(), metaData);
//        } catch (IOException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"File could not be uploaded");
//        }
//
//        awsClient.setObjectAcl(BUCKET_NAME, key, CannedAccessControlList.AuthenticatedRead);
//
//        return  awsClient.getResourceUrl(BUCKET_NAME, key);

        return "TBD";

    }
    @Override
    public String dummyFileUpload(String imgAsString) {


        if (imgAsString == null) return "Default Avatar Selected";

        return UUID.randomUUID().toString() + imgAsString;
    }
}
