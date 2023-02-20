package com.develonity.board.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {

  List<String> upload(List<MultipartFile> multipartFiles, String dir) throws IOException;

  String uploadOne(MultipartFile multipartFiles, String dir) throws IOException;

  String createFileName(String fileName) throws IOException;

  String getFileExtension(String fileName) throws IOException;

  void deleteFile(String imagePath);

}
