package com.develonity.board.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CommunityBoard")
public class CommunityBoard extends Board {

  @Builder
  public CommunityBoard(Long userId, String title, String content, Category category) {
    super(userId, title, content, category);
  }

  @Override
  public boolean isWriter(Long id) {
    return super.isWriter(id);
  }

}
