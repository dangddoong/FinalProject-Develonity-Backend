package com.develonity.comment.entity;

import com.develonity.board.entity.Board;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.user.entity.TimeStamp;
import com.develonity.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nickName;

  @Column(nullable = false)
  private String content;

//  @Column(nullable = false)
//  private boolean adoptStatus;

  // point는 0부터 시작한다.
  @ColumnDefault("0")
  @Column(nullable = false)
  public int point;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID")
  private Board board;


  public Comment(User user, CommentRequest requestDto) {
    this.nickName = user.getNickname();
    this.content = requestDto.getContent();
    this.point = user.getGiftPoint();
    this.user = user;
  }

  public void update(String content) {
    this.content = content;
  }


}
