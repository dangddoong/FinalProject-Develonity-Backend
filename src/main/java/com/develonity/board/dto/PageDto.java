package com.develonity.board.dto;

import java.util.Objects;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PageDto {

  @Positive // 0보다 큰수
  private int page = 1;

  private int size = 5;
  private String sortBy;
  //
//  private boolean isAsc;


  public Pageable toPageable() {
    if (Objects.isNull(sortBy)) {
      return PageRequest.of(page - 1, size);
    } else {
//      Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
      return PageRequest.of(page - 1, size, Sort.by(/*direction,*/ sortBy).descending());
    }
  }

  public Pageable toPageable(String sortBy) {

    return PageRequest.of(page - 1, size, Sort.by(sortBy).descending());
  }
}
