package com.develonity.comment.entity;

import com.develonity.user.entity.TimeStamp;
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
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COMMENT_ID")
  private Long id;

//  @Column(nullable = false)
//  private String nickname;

  @Column(nullable = false)
  private String content;

  //  @ManyToOne(fetch = FetchType.LAZY)
  @Column(name = "USER_ID")
  private Long userId;

  //  @ManyToOne(fetch = FetchType.LAZY)
  @Column(name = "BOARD_ID")
  private Long boardId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CommentStatus commentStatus = CommentStatus.NOT_ADOPTED;


  @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
  public List<ReplyComment> getReplyCommentList = new ArrayList<>();

  @Builder
  public Comment(Long userId, String content, Long questionBoardId) {
//    this.nickname = user.getNickName();
    this.content = content;
    this.userId = userId;
    this.boardId = questionBoardId;
  }


  public void update(String content) {
    this.content = content;
  }

  public void changeStatus() {
    this.commentStatus = CommentStatus.ADOPTED;
  }

  public boolean isAdopted() {
    return this.getCommentStatus().equals(CommentStatus.ADOPTED);
  }

}