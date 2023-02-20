package com.develonity.comment.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class CommentList {

  private int page;
  private int size = 5;
  private boolean isAsc;

  public Pageable toPageable() {
    page -= 1;
    page = Math.max(page, 0);
    Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//    Sort sort = Sort.by(Sort.Direction.DESC, "id");
    Sort sort = Sort.by(direction, "id");

    return PageRequest.of(page, size, sort);
  }


}
