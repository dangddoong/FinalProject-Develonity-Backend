package com.develonity.board.repository;

import static com.develonity.board.entity.QBoardLike.boardLike;
import static com.develonity.board.entity.QCommunityBoard.communityBoard;
import static com.develonity.comment.entity.QComment.comment;
import static com.develonity.comment.entity.QReplyComment.replyComment;
import static com.develonity.user.entity.QUser.user;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.PageDto;
import com.develonity.board.entity.BoardSort;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.entity.SortDirection;
import com.develonity.comment.service.CommentService;
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
public class CommunityBoardRepositoryImpl implements CommunityBoardRepositoryCustom {

  private final CommentService commentService;
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<CommunityBoardResponse> searchCommunityBoard(BoardSearchCond cond, PageDto pageDto) {
    List<CommunityBoardResponse> responses = jpaQueryFactory
        .select(
            Projections.constructor(
                CommunityBoardResponse.class,
                communityBoard.id,
                user.nickname,
                communityBoard.communityCategory,
                communityBoard.title,
                communityBoard.content,
                communityBoard.createdDate,
                communityBoard.lastModifiedDate,
                JPAExpressions
                    .select(
                        comment.countDistinct().add(replyComment.countDistinct())
                    ) // select count(*) from comment where comment.board_id = board_id
                    .from(comment, replyComment)
                    .where(
                        comment.boardId.eq(communityBoard.id),
                        replyComment.comment.boardId.eq(communityBoard.id)
                    ),
                JPAExpressions
                    .select(boardLike.countDistinct())
                    .from(boardLike)
                    .where(boardLike.boardId.eq(communityBoard.id))
            ))
        .from(communityBoard)
        .leftJoin(user).on(communityBoard.userId.eq(user.id))
        .leftJoin(boardLike).on(boardLike.boardId.eq(communityBoard.id))
        .leftJoin(comment).on(comment.boardId.eq(communityBoard.id))
//        .leftJoin(replyComment).on(replyComment.comment.boardId.eq(communityBoard.id))
//        .join(comment).on(replyComment.comment.id.eq(comment.id))
        .where(
            searchByTitle(cond.getTitle()),
            searchByContent(cond.getContent()),
            searchByCategory(cond.getCommunityCategory()),
            searchByNickname(cond.getNickname()))

        .groupBy(communityBoard.id)
        .orderBy(orderByCond(cond.getBoardSort(), cond.getSortDirection()),
            communityBoard.createdDate.desc())
        .offset(pageDto.toPageable().getOffset())
        .limit(pageDto.toPageable().getPageSize())
        .fetch();

    Long count = jpaQueryFactory
        .select(Wildcard.count)
        .from(communityBoard)
        .where(
            searchByTitle(cond.getTitle()),
            searchByContent(cond.getContent()),
            searchByCategory(cond.getCommunityCategory()))
        .fetch().get(0);

    return new PageImpl<>(responses, pageDto.toPageable(), count);
  }

  private BoardSort getSort(BoardSort sort) {
    if (Objects.isNull(sort)) {
      sort = BoardSort.EMPTY;
    } else if (sort.equals(BoardSort.LIKE)) {
      return BoardSort.LIKE;
    } else if (sort.equals(BoardSort.COMMENT)) {
      return BoardSort.COMMENT;
    }
    return sort;
  }

  private SortDirection getSortDirection(SortDirection sortDirection) {
    if (Objects.isNull(sortDirection)) {
      sortDirection = SortDirection.DESC;
    } else if (sortDirection.equals(SortDirection.ASC)) {
      return SortDirection.ASC;
    }
    return sortDirection;
  }


  private OrderSpecifier orderByCond(BoardSort sort, SortDirection sortDirection) {
    getSort(sort);
    if (getSort(sort).equals(BoardSort.LIKE) && getSortDirection(sortDirection).equals(
        SortDirection.DESC)) {
      return boardLike.countDistinct().desc();
    } else if (getSort(sort).equals(BoardSort.LIKE) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return boardLike.countDistinct().asc();

    }/*else if (getSort(sort).equals(BoardSort.EMPTY) && getSortDirection(sortDirection).equals(
        SortDirection.DESC)) {
      return communityBoard.createdDate.desc();
    }*/ else if (getSort(sort).equals(BoardSort.EMPTY) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return communityBoard.createdDate.asc();
    } else if (getSort(sort).equals(BoardSort.COMMENT) && getSortDirection(sortDirection).equals(
        SortDirection.DESC)) {
      return comment.countDistinct().add(replyComment.countDistinct()).desc();
    } else if (getSort(sort).equals(BoardSort.COMMENT) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return comment.countDistinct().add(replyComment.countDistinct()).asc();
    }
    return communityBoard.createdDate.desc();
  }

  private BooleanExpression searchByTitle(String title) {
    return Objects.nonNull(title) ? communityBoard.title.contains(title) : null;
  }

  private BooleanExpression searchByContent(String content) {
    return Objects.nonNull(content) ? communityBoard.content.contains(content) : null;
  }

  private BooleanExpression searchByCategory(CommunityCategory communityCategory) {
    return Objects.nonNull(communityCategory) ? communityBoard.communityCategory.eq(
        communityCategory) : null;
  }

  private BooleanExpression searchByNickname(String nickname) {
    return Objects.nonNull(nickname) ? user.nickname.contains(nickname) : null;
  }
}

//
//  private OrderSpecifier orderByCond(String sort) {
//    sort = Objects.isNull(sort) ? "new" : "like";
//    if (sort.equals("like")) {
//      return boardLike.countDistinct().desc();
//    } else {
//      return communityBoard.createdDate.desc();
//
//    }
//  }
/*
    List<Tuple> results = jpaQueryFactory.select/*(Projections.fields(
    communityBoard.id,
    communityBoard.title,
    communityBoard.content,
    communityBoard.communityCategory,
    ExpressionUtils.as(JPAExpressions.select(user.nickname).from(user)
    .where(user.id.eq(communityBoard.userId)), "nickname"),
    ExpressionUtils.as(JPAExpressions.select(boardLike.boardId.count()).from(boardLike)
    .where(boardLike.boardId.eq(communityBoard.id)), "boardLike"),
    communityBoard.createdDate)

    .from(communityBoard)
    .where(
    searchByTitle(cond.getTitle()),
    searchByContent(cond.getContent()),
    searchByCategory(cond.getCommunityCategory())
    )
    .leftJoin(user).on(communityBoard.userId.eq(user.id))
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .fetch();

    JPAQuery<Long> countQuery = jpaQueryFactory
    .select(communityBoard.count())
    .from(communityBoard)
    .leftJoin(user).on(communityBoard.userId.eq(user.id))
    .where(user.id.eq(communityBoard.userId))
    .where(boardLike.boardId.eq(communityBoard.id));

    List<CommunityBoardResponse> results2 = new ArrayList<>();
    for (Tuple tuple : results) {
    Long id = tuple.get(communityBoard.id);
    String nickName = tuple.get(user.nickname);
    String title = tuple.get(communityBoard.title);
    String content = tuple.get(communityBoard.content);
    Long likeCount = tuple.get(
    ExpressionUtils.as(JPAExpressions.select(boardLike.boardId.count()).from(boardLike)
    .where(boardLike.boardId.eq(communityBoard.id)), "boardLike"));
    LocalDateTime createAt = tuple.get(communityBoard.createdDate);

    List<String> imagePaths = jpaQueryFactory.select(boardImage.imagePath)
    .from(boardImage)
    .where(boardImage.boardId.eq(communityBoard.id))
    .fetch();

    JPAQuery<Long> countQuery2 = jpaQu
    ery
    CommunityBoardResponse dto = CommunityBoardResponse.builder()
    .id(id)
    .nickname(nickName)
    .title(title)
    .boardLike(likeCount)
    .content(content)
    .createdAt(createAt)
    .imagePaths(imagePaths)
    .build();

    results2.add(dto);
    }
    return PageableExecutionUtils.getPage(results2, pageable, countQuery::fetchOne);




  private Long countByUserId(Long userId) {
    return jpaQueryFactory.selectFrom(communityBoard)
        .where(
            communityBoard.userId.eq(userId)
        )
        .stream().count();
  }
 */