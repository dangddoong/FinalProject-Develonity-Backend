package com.develonity.board.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface BoardImageService {

  void constructor();

  void upload(List<MultipartFile> multiFileList, String filename);

  void deleteFile(String filename);

}
