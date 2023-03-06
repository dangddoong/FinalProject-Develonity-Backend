package com.develonity.board.repository;

import static com.develonity.board.entity.QBoardLike.boardLike;
import static com.develonity.board.entity.QCommunityBoard.communityBoard;
import static com.develonity.comment.entity.QComment.comment;
import static com.develonity.comment.entity.QReplyComment.replyComment;
import static com.develonity.user.entity.QUser.user;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.CommunityBoardResponse;
import com.develonity.board.dto.CommunityBoardSearchCond;
import com.develonity.board.dto.PageDto;
import com.develonity.board.entity.BoardSort;
import com.develonity.board.entity.CommunityCategory;
import com.develonity.board.entity.SortDirection;
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

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public Page<CommunityBoardResponse> searchCommunityBoard(CommunityBoardSearchCond cond, PageDto pageDto) {
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
                        comment.countDistinct()
                    )
                    .from(comment)
                    .where(
                        comment.boardId.eq(
                            communityBoard.id)
                    ),
                JPAExpressions
                    .select(replyComment.countDistinct())
                    .from(replyComment)
                    .where(replyComment.comment.boardId.eq(communityBoard.id)),
                JPAExpressions
                    .select(Wildcard.count/*boardLike.countDistinct()*/)
                    .from(boardLike)
                    .where(boardLike.boardId.eq(communityBoard.id))
            ))
        .from(communityBoard)
        .leftJoin(user).on(communityBoard.userId.eq(user.id))
        .leftJoin(boardLike).on(boardLike.boardId.eq(communityBoard.id))
        .leftJoin(comment).on(comment.boardId.eq(communityBoard.id))
//        .leftJoin(replyComment).on(replyComment.comment.boardId.eq(communityBoard.id))

        .where(
            searchByTitle(cond.getTitle()),
            searchByContent(cond.getContent()),
            searchByCategoryEqual(cond.getCommunityCategory()),
            searchByNickname(cond.getNickname()))

        .groupBy(communityBoard.id)
        .orderBy(orderByCond(cond.getBoardSort(), cond.getSortDirection()),
            communityBoard.createdDate.desc())
        .offset(pageDto.toPageable().getOffset())
        .limit(pageDto.toPageable().getPageSize())
        .fetch();

    Long count = jpaQueryFactory //where from절 맞추기..조인도..
        .select(Wildcard.count)
        .from(communityBoard)
        .leftJoin(user).on(communityBoard.userId.eq(user.id))
        .where(
            searchByTitle(cond.getTitle()),
            searchByContent(cond.getContent()),
            searchByCategoryEqual(cond.getCommunityCategory()),
            searchByNickname(cond.getNickname()))

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

    }
    else if (getSort(sort).equals(BoardSort.EMPTY) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return communityBoard.createdDate.asc();
    } else if (getSort(sort).equals(BoardSort.COMMENT) && getSortDirection(sortDirection).equals(
        SortDirection.DESC)) {
      return comment.countDistinct()/*.add(replyComment.countDistinct())*/.desc();
    } else if (getSort(sort).equals(BoardSort.COMMENT) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return comment.countDistinct()/*.add(replyComment.countDistinct())*/.asc();
    }
    return communityBoard.createdDate.desc();
  }

  private BooleanExpression searchByTitle(String title) {
    return Objects.nonNull(title) ? communityBoard.title.contains(title) : null;
  }

  private BooleanExpression searchByContent(String content) {
    return Objects.nonNull(content) ? communityBoard.content.contains(content) : null;
  }

  private BooleanExpression searchByCategoryEqual(CommunityCategory communityCategory) {
    return Objects.nonNull(communityCategory) ? communityBoard.communityCategory.eq(
        communityCategory) : null;
  }

  private BooleanExpression searchByNickname(String nickname) {
    return Objects.nonNull(nickname) ? user.nickname.contains(nickname) : null;
  }

//    @Override
//  public Page<CommunityBoardResponse> searchCommunityBoard(BoardSearchCond cond, PageDto pageDto) {
//    List<CommunityBoardResponse> responses = jpaQueryFactory.select(
//            Projections.constructor(
//                CommunityBoardResponse.class,
//                communityBoard.id,
//                user.nickname,
//                communityBoard.communityCategory,
//                communityBoard.title,
//                communityBoard.content,
//                communityBoard.createdDate,
//                communityBoard.lastModifiedDate,
//                JPAExpressions.select(boardLike.count()).from(boardLike)
//                    .where(boardLike.boardId.eq(communityBoard.id))
//            )
//        ).from(communityBoard)
//        .leftJoin(user).on(communityBoard.userId.eq(user.id))
//        .leftJoin(boardLike).on(boardLike.boardId.eq(communityBoard.id))
//        .groupBy(communityBoard.id, user.nickname, boardLike.id)
//        .having(searchByTitle(cond.getTitle()),
//            searchByContent(cond.getContent()),
//            searchByCategory(cond.getCommunityCategory()),
//            searchByNickname(cond.getNickname()))
//        .orderBy(orderByCond(cond.getBoardSort(), cond.getSortDirection()),
//            communityBoard.createdDate.desc())
//        .offset(pageDto.toPageable().getOffset())
//        .limit(pageDto.toPageable().getPageSize())
//        .fetch();
//
//    List<Long> boardIds = responses.stream().map(CommunityBoardResponse::getId).toList();
//
//    List<CommunityBoardResponse> countAll = jpaQueryFactory.select(
//            Projections.constructor(CommunityBoardResponse.class,
//                comment.boardId,
//                comment.count(),
//                JPAExpressions.select(replyComment.count()).from(replyComment).groupBy(replyComment)
//                    .having(replyComment.comment.id.eq(comment.id)))
//        ).from(comment)
//        .leftJoin(communityBoard).on(communityBoard.id.eq(comment.boardId))
//        .groupBy(communityBoard.id, comment.id)
//        .having(comment.boardId.in(boardIds))
//        .fetch();
//
//    Long count = jpaQueryFactory
//        .select(Wildcard.count)
//        .from(communityBoard)
//        .where(
//            searchByTitle(cond.getTitle()),
//            searchByContent(cond.getContent()),
//            searchByCategory(cond.getCommunityCategory()))
//        .fetch().get(0);
//
//    for (int i = 0; i < countAll.size(); i++) {
//      responses.get(i).setCountAllComments(countAll.get(i).getCountAllComments());
//    }
//
//    return new PageImpl<>(responses, pageDto.toPageable(), count);
//  }


}
