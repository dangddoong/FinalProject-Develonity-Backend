package com.develonity.common.aws.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.develonity.common.aws.dto.PreSignedUrlResponse;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsPreSignedUrlService {

  private final AmazonS3 amazonS3;

  private String saveFileName;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String location;

  @Value(value = "${cloud.aws.credentials.accessKey}")
  private String accessKey;

  @Value(value = "${cloud.aws.credentials.secretKey}")
  private String secretKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  public PreSignedUrlResponse requestPreSignedUrl(String prefix, String fileName) {

    String uniqueFileName = getUniqueFileName(fileName);

    if (!prefix.equals("")) { //버킷 내 디렉토리 없으면 빈 문자니까
      uniqueFileName = prefix + "/" + uniqueFileName;
    }
    saveFileName = uniqueFileName;
    GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket,
        uniqueFileName);
    URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    String stringUrl = url.toString();
    return PreSignedUrlResponse.builder()
        .objectKey(uniqueFileName)
        .preSignedURL(stringUrl).build();
//    return url.toString();
  }

  private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket,
      String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(getPreSignedUrlExpiration());
    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL, //권한설정
        CannedAccessControlList.PublicRead.toString()); //제한x

    return generatePresignedUrlRequest;
  }

  //조회
  public String getPreSignedUrl(String objectKey) {

//    S3Presigner preSigner = getPreSigner();
    String bucketName = bucket;

    java.util.Date expiration = new java.util.Date();
    long expTimeMillis = Instant.now().toEpochMilli();
    expTimeMillis += 1000 * 60 * 60;
    expiration.setTime(expTimeMillis);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, objectKey)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);
    URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

//    URL imgurl = amazonS3.getUrl(bucketName, objectKey);
    return url.toString();
  }

  private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest2(String bucket,
      String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.GET)
            .withExpiration(getPreSignedUrlExpiration());
    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL, //권한설정
        CannedAccessControlList.PublicRead.toString()); //제한x
    return generatePresignedUrlRequest;
  }

  private Date getPreSignedUrlExpiration() {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 60; //일단 1시간 연습용
    expiration.setTime(expTimeMillis);
    log.info(expiration.toString());
    return expiration;
  }

  private String getUniqueFileName(String filename) {
    return UUID.randomUUID().toString() + filename;

  }


  public String findByName() { //경로

    return "https://" + bucket + ".s3." + location + ".amazonaws.com/"/* + path + "/"
     */ + saveFileName;
  }

  //  //PresignedURL 조회
//  public String getGetPreSignedURL(String fileName) {
//
//    S3Presigner preSigner = getPreSigner();
//    String bucketName = "";
//    String objectKey = fileName; //S3에 올릴 오브젝트 키
//
//    // 미리 지정된 URL이 60분 후에 만료되도록 설정합니다.
//    java.util.Date expiration = new java.util.Date();
//    long expTimeMillis = Instant.now().toEpochMilli();
//    expTimeMillis += 1000 * 60 * 60;
//    expiration.setTime(expTimeMillis);
//
//    try {
//      // 미리 서명된 URL을 생성합니다.
//      GeneratePresignedUrlRequest generatePresignedUrlRequest =
//          new GeneratePresignedUrlRequest(bucketName, objectKey)
//              .withMethod(HttpMethod.GET)
//              .withExpiration(expiration);
//      URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
//      String preSignedURL = url.toString();
//
//      URL imgurl = amazonS3.getUrl(bucketName, objectKey);
//      System.out.println("img URL: " + imgurl.toString());
//      return preSignedURL;
//
//    } catch (AmazonServiceException e) {
//      // 호출이 성공적으로 전송되었지만 Amazon S3에서 처리할 수 없습니다.
//      // 오류 응답을 반환했습니다.
//      e.printStackTrace();
//    }
//    return null;
//  }
//
//
  public S3Presigner getPreSigner() {
    AwsCredentialsProvider awsCredentialsProvider;
    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
    awsCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);

    return S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(awsCredentialsProvider)
        .build();
  }


}

