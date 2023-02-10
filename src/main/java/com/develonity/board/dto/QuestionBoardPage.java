package com.develonity.board.dto;

import com.develonity.board.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class QuestionBoardPage {

  private String title;
  private Category category;
  private int page;
  private int size;

  public Pageable toPageable() {
    Sort sort = Sort.by(Sort.Direction.DESC, "id");
    return PageRequest.of(page - 1, size, sort);
  }
}
