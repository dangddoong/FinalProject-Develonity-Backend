package com.develonity.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(force = true)
public class ImageNameRequest {

    private final String imageName;

    public ImageNameRequest(String imageName) {
      this.imageName = imageName;
    }
  }