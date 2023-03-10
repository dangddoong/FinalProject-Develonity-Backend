package com.develonity.comment.repository;

import static com.develonity.comment.entity.QComment.comment;
import static com.develonity.comment.entity.QCommentLike.commentLike;
import static com.develonity.user.entity.QUser.user;

import com.develonity.comment.dto.CommentPageDto;
import com.develonity.comment.dto.CommentResponse;
import com.develonity.comment.dto.CommentSearchCond;
import com.develonity.comment.entity.CommentSort;
import com.develonity.comment.entity.CommentSortDirection;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  // 댓글 전체 조회
  @Override
  public Page<CommentResponse> searchComment(CommentPageDto commentPageDto,
      CommentSearchCond commentSearchCond) {
    List<CommentResponse> responses = jpaQueryFactory
        .select(
            Projections.constructor(
                CommentResponse.class,
                // CommentResponse의 필드(생성자) 순서 똑같이 일치시키기
                comment.id,
                user.nickname,
                comment.content,
                comment.createdDate,
                comment.lastModifiedDate,
                comment.commentStatus,
                JPAExpressions
                    // countDistinct = 카운트를 세는 기능, 좋아요의 개수를 출력해준다.
                    .select(commentLike.countDistinct())
                    .from(commentLike)
                    .where(commentLike.commentId.eq(comment.id))
//                JPAExpressions
//                    .select(replyComment)
//                    .from(replyComment)
//                    .where(replyComment.comment.id.eq(comment.id))
            ))
        .from(comment)
        .leftJoin(user).on(comment.userId.eq(user.id))
        .leftJoin(commentLike).on(commentLike.commentId.eq(comment.id))
//        .leftJoin(replyComment).on(replyComment.comment.id.eq(comment.id))
        .where(
            comment.userId.eq(user.id),
            searchByContent(commentSearchCond.getContent()),
            searchByNickname(commentSearchCond.getNickname())
        )
        .groupBy(comment.id)
        .orderBy(orderByCond(commentSearchCond.getCommentSort(),
                commentSearchCond.getCommentSortDirection()),
            comment.createdDate.desc())
        .offset(commentPageDto.toPageable().getOffset())
        .limit(commentPageDto.toPageable().getPageSize())
        .fetch();

    Long count = jpaQueryFactory
        .select(Wildcard.count)
        .from(comment)
        .leftJoin(user).on(comment.userId.eq(user.id))
        .where(
            searchByNickname(commentSearchCond.getNickname()),
            searchByContent(commentSearchCond.getContent())
        )
        .fetch().get(0);
    return new PageImpl<>(responses, commentPageDto.toPageable(), count);

  }

  // 유저가 쓴 글 전체 조회
  @Override
  public Page<CommentResponse> searchMyComment(CommentPageDto commentPageDto,
      CommentSearchCond commentSearchCond, Long userId) {
    List<CommentResponse> commentResponses = jpaQueryFactory
        .select(
            Projections.constructor(
                CommentResponse.class,
                comment.id,
                user.nickname,
                comment.content,
                comment.createdDate,
                comment.lastModifiedDate,
                comment.commentStatus,
                JPAExpressions
                    .select(commentLike.countDistinct())
                    .from(commentLike)
                    .where(commentLike.commentId.eq(comment.id))
//                JPAExpressions
//                    .select(replyComment.countDistinct())
//                    .from(replyComment)
//                    .where(replyComment.comment.id.eq(comment.id))
            ))
        .from(comment)
        .leftJoin(user).on(comment.userId.eq(user.id))
        .leftJoin(commentLike).on(commentLike.commentId.eq(comment.id))
//        .leftJoin(replyComment).on(replyComment.comment.id.eq(comment.id))
        .where(
            comment.userId.eq(userId),
            searchByContent(commentSearchCond.getContent()),
            searchByNickname(commentSearchCond.getNickname())
        )
        .groupBy(comment.id)
        .orderBy(orderByCond(commentSearchCond.getCommentSort(),
                commentSearchCond.getCommentSortDirection()),
            comment.createdDate.desc())
        .offset(commentPageDto.toPageable().getOffset())
        .limit(commentPageDto.toPageable().getPageSize())
        .fetch();

    Long count = jpaQueryFactory
        .select(Wildcard.count)
        .from(comment)
        .leftJoin(user).on(comment.userId.eq(user.id))
        .where(
            searchByNickname(commentSearchCond.getNickname()),
            searchByContent(commentSearchCond.getContent())
        )
        .fetch().get(0);
    return new PageImpl<>(commentResponses, commentPageDto.toPageable(), count);
  }

  // 아마 검색 기능을 위한 것 같음..
  private CommentSort getCommentSort(CommentSort commentSort) {
    if (Objects.isNull(commentSort)) {
      commentSort = CommentSort.EMPTY;
    } else if (commentSort.equals(CommentSort.COMMENT)) {
      return CommentSort.COMMENT;
    }
    return commentSort;
  }

  // 오름차순, 내림차순 기능
  private CommentSortDirection getcommentSortDirection(CommentSortDirection commentSortDirection) {
    if (Objects.isNull(commentSortDirection)) {
      commentSortDirection = CommentSortDirection.DESC;
    } else if (commentSortDirection.equals(CommentSortDirection.ASC)) {
      return CommentSortDirection.ASC;
    }
    return commentSortDirection;
  }

  // 정렬 기능?
  private OrderSpecifier orderByCond(CommentSort commentSort,
      CommentSortDirection commentSortDirection) {
    getCommentSort(commentSort);
    if (getCommentSort(commentSort).equals(CommentSort.EMPTY) && getcommentSortDirection(
        commentSortDirection).equals(CommentSortDirection.ASC)) {
      return comment.createdDate.asc();
    } else if (getCommentSort(commentSort).equals(CommentSort.EMPTY) && getcommentSortDirection(
        commentSortDirection).equals(CommentSortDirection.DESC)) {
      return comment.createdDate.desc();
    } else if (getCommentSort(commentSort).equals(CommentSort.COMMENT) && getcommentSortDirection(
        commentSortDirection).equals(CommentSortDirection.DESC)) {
      return comment.countDistinct().desc();
    } else if (getCommentSort(commentSort).equals(CommentSort.COMMENT) && getcommentSortDirection(
        commentSortDirection).equals(CommentSortDirection.ASC)) {
      return comment.countDistinct().asc();
    }
    return comment.createdDate.desc();
  }

  // 댓글을 찾는(검색하는) 기능
  private BooleanExpression searchByContent(String content) {
    return Objects.nonNull(content) ? comment.content.contains(content) : null;
  }

  // 닉네암으로 찾는(검색하는) 기능
  private BooleanExpression searchByNickname(String nickname) {
    return Objects.nonNull(nickname) ? user.nickname.contains(nickname) : null;
  }
}
