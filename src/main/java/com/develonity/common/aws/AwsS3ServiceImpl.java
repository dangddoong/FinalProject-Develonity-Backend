package com.develonity.common.aws;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.develonity.common.exception.CustomException;
import com.develonity.common.exception.ExceptionStatus;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {


  private final AmazonS3 s3Client;
  @Value("${cloud.aws.credentials.accessKey}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secretKey}")
  private String secretKey;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;

//  @PostConstruct
//  public AmazonS3Client amazonS3Client() {
//    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
//        .withRegion(region)
//        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//        .build();
//  }

  //다중파일 업로드
  public List<String> upload(List<MultipartFile> multipartFiles, String dir) throws IOException {
    List<String> imgPaths = new ArrayList<>();

    for (MultipartFile file : multipartFiles) {
      String fileName = createFileName(file.getOriginalFilename());
      ObjectMetadata objectMetadata = new ObjectMetadata();
      objectMetadata.setContentLength(file.getSize());
      objectMetadata.setContentType(file.getContentType());

      try (InputStream inputStream = file.getInputStream()) {
        s3Client.putObject(
            new PutObjectRequest(bucket + dir, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        imgPaths.add(s3Client.getUrl(bucket + dir, fileName).toString());
      } catch (IOException e) {
        throw new IOException();
      }
    }
    return imgPaths;
  }

  //단일파일업로드
  @Override
  public String uploadOne(MultipartFile multipartFile, String dir) throws IOException {
    String imgPath;

    String fileName = createFileName(multipartFile.getOriginalFilename());
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentLength(multipartFile.getSize());
    objectMetadata.setContentType(multipartFile.getContentType());

    try (InputStream inputStream = multipartFile.getInputStream()) {
      s3Client.putObject(
          new PutObjectRequest(bucket + dir, fileName, inputStream, objectMetadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));
      imgPath = s3Client.getUrl(bucket + dir, fileName).toString();
    } catch (IOException e) {
      throw new IOException();
    }
    return imgPath;
  }

  public void deleteFile(String imagePathLong) {
    try {
      String imagePath = imagePathLong.substring(54);
      s3Client.deleteObject(
          new com.amazonaws.services.s3.model.DeleteObjectRequest(bucket, imagePath));
    } catch (
        AmazonServiceException e) {
      e.printStackTrace();
    }
  }


  public String createFileName(String fileName) throws IOException {
    return UUID.randomUUID().toString().concat(getFileExtension(fileName));
  }

  public String getFileExtension(String fileName) throws IOException {
    if (fileName.length() == 0) {
      throw new IOException();
    }
    ArrayList<String> fileExtensionValidate = new ArrayList<>();
    fileExtensionValidate.add(".jpg");
    fileExtensionValidate.add(".jpeg");
    fileExtensionValidate.add(".png");
    fileExtensionValidate.add(".JPG");
    fileExtensionValidate.add(".JPEG");
    fileExtensionValidate.add(".PNG");
    String fileExtension = fileName.substring(fileName.lastIndexOf("."));
    if (!fileExtensionValidate.contains(fileExtension)) {
      throw new CustomException(ExceptionStatus.IS_NOT_CORRECT_FORMAT);
    }
    return fileName.substring(fileName.lastIndexOf("."));
  }
}

