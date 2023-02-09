package com.develonity.comment.entity;

import com.develonity.board.entity.Board;
import com.develonity.comment.dto.CommentRequest;
import com.develonity.user.entity.TimeStamp;
import com.develonity.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private boolean adoptStatus;

  // point는 0부터 시작한다.
  @ColumnDefault("0")
  @Column(nullable = false)
  public int point;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOARD_ID", nullable = false)
  private Board board;

  // 좋아요는 0부터 시작한다.
  @ColumnDefault("0")
  @Column(nullable = false)
  Long commentLikes;

  // 하나의 댓글에 여러개의 좋아요를 추가할 수 있다
  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  private List<CommentLike> commentLikeList = new ArrayList<>();

  public Comment(User user, CommentRequest requestDto) {
    this.user = user;
    this.content = requestDto.getContent();
  }

  public void update(String content) {
    this.content = content;
  }

  public void addLike() {
    this.commentLikes += 1;
  }

  public void deleteLike() {
    this.commentLikes -= 1;
  }
}
