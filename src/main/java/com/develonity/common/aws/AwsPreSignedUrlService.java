package com.develonity.common.aws;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsPreSignedUrlService {

  private final AmazonS3 amazonS3;

  private String uniqueFileName;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String location;


  public String getPreSignedUrl(String prefix, String fileName) {

       uniqueFileName = getUniqueFileName(fileName);

//    uniqueFileName = fileName;

    if (!prefix.equals("")) { //버킷 내 디렉토리 없으면 빈 문자니까
      uniqueFileName = prefix + "/" + uniqueFileName;
    }
    GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, uniqueFileName);
    URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    return url.toString();
  }

  private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
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

  private String getUniqueFileName(String filename){
    return UUID.randomUUID().toString()+filename;

  }

//  public String findByName(String path) { //경로
////        if (!amazonS3.doesObjectExist(bucket,editPath+ useOnlyOneFileName))
////            return "File does not exist";
//    log.info("Generating signed URL for file name {}", useOnlyOneFileName);
////        return  amazonS3.getUrl(bucket,editPath+useOnlyOneFileName).toString();
//    return "https://"+bucket+".s3."+location+".amazonaws.com/"+path+"/"+useOnlyOneFileName;
//  }
  public String findByName(String path) { //경로
//        if (!amazonS3.doesObjectExist(bucket,editPath+ useOnlyOneFileName))
//            return "File does not exist";
    log.info("Generating signed URL for file name {}", uniqueFileName);
//        return  amazonS3.getUrl(bucket,editPath+useOnlyOneFileName).toString();
    return "https://"+bucket+".s3."+location+".amazonaws.com/"+path+"/"+uniqueFileName;
  }
}

