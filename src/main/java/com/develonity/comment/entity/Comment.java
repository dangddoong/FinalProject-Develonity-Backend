package com.develonity.comment.entity;

import com.develonity.comment.dto.CommentRequest;
import com.develonity.user.entity.TimeStamp;
import com.develonity.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

//  @Column(nullable = false)
//  private String nickname;

  @Column(nullable = false)
  private String content;

//  @Column(nullable = false)
//  private boolean adoptStatus;

  // point는 0부터 시작한다.
  @ColumnDefault("0")
  @Column(nullable = false)
  public int point;

  //  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID")
  private Long userId;

  //  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID")
  private Long boardId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CommentStatus commentStatus = CommentStatus.NOT_ADOPTED;


  @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
  public List<ReplyComment> getReplyCommentList = new ArrayList<>();


  public Comment(User user, CommentRequest requestDto, Long questionBoardId) {
//    this.nickname = user.getNickName();
    this.content = requestDto.getContent();
    this.point = user.getGiftPoint();
    this.userId = user.getId();
    this.boardId = questionBoardId;
  }


  public void update(String content) {
    this.content = content;
  }

  public void changeStatus() {
    this.commentStatus = CommentStatus.ADOPTED;
  }


}
