package com.develonity.board.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("CommunityBoard")
public class CommunityBoard extends Board {

  @Column
  @Enumerated(EnumType.STRING)
  private SubCategory.CommunityCategory communityCategory;

  @Builder
  public CommunityBoard(Long userId, String title, String content, Category category,
      SubCategory.CommunityCategory communityCategory) {
    super(userId, title, content, category);
    this.communityCategory = communityCategory;
  }

//  @Override
//  public Boolean isWriter(Long id) {
//    return super.isWriter(id);
//  }

}
