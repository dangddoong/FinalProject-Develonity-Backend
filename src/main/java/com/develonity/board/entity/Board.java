package com.develonity.board.entity;

import com.develonity.user.entity.TimeStamp;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;


@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Board extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Category category;

  @Column(nullable = false)
  @ColumnDefault("'default.jpg'")
  private String imageUrl;

//  @ManyToOne
//  @JoinColumn(name = "USER_ID", nullable = false, fetch = FetchType.LAZY)
//  private User user;

  @JoinColumn(name = "USER_ID")
  //일단 이렇게 넣어놓음
  private Long userId;

  public Board(Long userId, String title, String content, Category category,
      String imageUrl) {
    this.userId = userId;
    this.title = title;
    this.content = content;
    this.category = category;
    this.imageUrl = imageUrl;
  }

  public void updateBoard(String title, String content, Category category, String imageUrl) {
    this.title = title;
    this.content = content;
    this.category = category;
    this.imageUrl = imageUrl;
  }

  boolean isWriter(Long id) {
    return userId.equals(id);
  }


}

