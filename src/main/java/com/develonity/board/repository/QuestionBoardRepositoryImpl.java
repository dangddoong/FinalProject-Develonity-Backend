package com.develonity.board.repository;

import static com.develonity.board.entity.QBoardLike.boardLike;
import static com.develonity.board.entity.QCommunityBoard.communityBoard;
import static com.develonity.board.entity.QQuestionBoard.questionBoard;
import static com.develonity.comment.entity.QComment.comment;
import static com.develonity.user.entity.QUser.user;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectFrom;

import com.develonity.board.dto.BoardSearchCond;
import com.develonity.board.dto.PageDto;
import com.develonity.board.dto.QuestionBoardResponse;
import com.develonity.board.dto.QuestionBoardSearchCond;
import com.develonity.board.entity.BoardSort;
import com.develonity.board.entity.BoardStatus;
import com.develonity.board.entity.QuestionCategory;
import com.develonity.board.entity.SortDirection;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@RequiredArgsConstructor

public class QuestionBoardRepositoryImpl implements QuestionBoardRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<QuestionBoardResponse> searchQuestionBoard(QuestionBoardSearchCond questionBoardSearchCond, PageDto pageDto) {
    List<QuestionBoardResponse> responses = jpaQueryFactory
        .select(
            Projections.constructor(
                QuestionBoardResponse.class,
                questionBoard.id,
                user.nickname,
                questionBoard.questionCategory,
                questionBoard.title,
                questionBoard.content,
                questionBoard.prizePoint,
                questionBoard.status,
                questionBoard.createdDate,
                questionBoard.lastModifiedDate,
                JPAExpressions
                    .select(
                        /*comment.count()*/
                        Wildcard.count) // select count(*) from comment where comment.board_id = board_id
                    .from(comment)
                    .where(
                        comment.boardId.eq(questionBoard.id)
                    ),
                JPAExpressions
                    .select(boardLike.countDistinct())
                    .from(boardLike)
                    .where(boardLike.boardId.eq(questionBoard.id))
            ))
        .from(questionBoard)
        .leftJoin(user).on(questionBoard.userId.eq(user.id))
        .leftJoin(boardLike).on(boardLike.boardId.eq(questionBoard.id))
        .leftJoin(comment).on(comment.boardId.eq(questionBoard.id))
        .where(
            searchByTitle(questionBoardSearchCond.getTitle()),
            searchByContent(questionBoardSearchCond.getContent()),
            searchByCategoryEqual(questionBoardSearchCond.getQuestionCategory()),
            searchByNickname(questionBoardSearchCond.getNickname()),
            searchByBoardStatusEqual(questionBoardSearchCond.getBoardStatus()))

        .groupBy(questionBoard.id)
        .orderBy(orderByCond(questionBoardSearchCond.getBoardSort(), questionBoardSearchCond.getSortDirection()),
            questionBoard.createdDate.desc())
        .offset(pageDto.toPageable().getOffset())
        .limit(pageDto.toPageable().getPageSize())
        .fetch();

    Long count = jpaQueryFactory
        .select(Wildcard.count)
        .from(questionBoard)
        .where(
            searchByTitle(questionBoardSearchCond.getTitle()),
            searchByContent(questionBoardSearchCond.getContent()),
            searchByCategoryEqual(questionBoardSearchCond.getQuestionCategory()),
            searchByBoardStatusEqual(questionBoardSearchCond.getBoardStatus()))
        .fetch().get(0);
    return new PageImpl<>(responses, pageDto.toPageable(), count);
//    JPAQuery<QuestionBoard> countQuery = jpaQueryFactory
//        .select(questionBoard)
//        .from(questionBoard)
//        .where(
//            searchByTitle(cond.getTitle()),
//            searchByContent(cond.getContent()),
//            searchByCategory(cond.getQuestionCategory()),
//            searchByStatus(cond.getBoardStatus()));
//
//    return PageableExecutionUtils.getPage(responses, pageDto.toPageable(), countQuery::fetchCount);

  }

  @Override
  public Page<QuestionBoardResponse> searchMyQuestionBoard(
      QuestionBoardSearchCond questionBoardSearchCond, PageDto pageDto, Long userId) {
    List<QuestionBoardResponse> responses = jpaQueryFactory
        .select(
            Projections.constructor(
                QuestionBoardResponse.class,
                questionBoard.id,
                user.nickname,
                questionBoard.questionCategory,
                questionBoard.title,
                questionBoard.content,
                questionBoard.prizePoint,
                questionBoard.status,
                questionBoard.createdDate,
                questionBoard.lastModifiedDate,
                JPAExpressions
                    .select(
                        /*comment.count()*/
                        Wildcard.count) // select count(*) from comment where comment.board_id = board_id
                    .from(comment)
                    .where(
                        comment.boardId.eq(questionBoard.id)
                    ),
                JPAExpressions
                    .select(boardLike.countDistinct())
                    .from(boardLike)
                    .where(boardLike.boardId.eq(questionBoard.id))
            ))
        .from(questionBoard)
        .leftJoin(user).on(questionBoard.userId.eq(user.id))
        .leftJoin(boardLike).on(boardLike.boardId.eq(questionBoard.id))
        .leftJoin(comment).on(comment.boardId.eq(questionBoard.id))
        .where(
            questionBoard.userId.eq(userId),
            searchByTitle(questionBoardSearchCond.getTitle()),
            searchByContent(questionBoardSearchCond.getContent()),
            searchByCategoryEqual(questionBoardSearchCond.getQuestionCategory()),
            searchByBoardStatusEqual(questionBoardSearchCond.getBoardStatus()))

        .groupBy(questionBoard.id)
        .orderBy(orderByCond(questionBoardSearchCond.getBoardSort(), questionBoardSearchCond.getSortDirection()),
            questionBoard.createdDate.desc())
        .offset(pageDto.toPageable().getOffset())
        .limit(pageDto.toPageable().getPageSize())
        .fetch();

    Long count = jpaQueryFactory
        .select(Wildcard.count)
        .from(questionBoard)
        .where(
            searchByTitle(questionBoardSearchCond.getTitle()),
            searchByContent(questionBoardSearchCond.getContent()),
            searchByCategoryEqual(questionBoardSearchCond.getQuestionCategory()),
            searchByBoardStatusEqual(questionBoardSearchCond.getBoardStatus()))
        .fetch().get(0);
    return new PageImpl<>(responses, pageDto.toPageable(), count);
  }

  //  좋아요 순서 질문글 조회(카테고리,상태 검색)
  public List<QuestionBoardResponse> QuestionBoardOrderByLikes(QuestionBoardSearchCond cond) {
    List<QuestionBoardResponse> responses = jpaQueryFactory
        .select(
            Projections.constructor(
                QuestionBoardResponse.class,
                questionBoard.id,
                user.nickname,
                questionBoard.questionCategory,
                questionBoard.title,
                questionBoard.content,
                questionBoard.prizePoint,
                questionBoard.status,
                questionBoard.createdDate,
                questionBoard.lastModifiedDate,
                select(
                    /*comment.count()*/
                    Wildcard.count) // select count(*) from comment where comment.board_id = board_id
                    .from(comment)
                    .where(
                        comment.boardId.eq(questionBoard.id)
                    ),
                select(boardLike.countDistinct())
                    .from(boardLike)
                    .where(boardLike.boardId.eq(questionBoard.id))
            ))
        .from(questionBoard)
        .leftJoin(user).on(questionBoard.userId.eq(user.id))
        .leftJoin(boardLike).on(boardLike.boardId.eq(questionBoard.id))
        .where(
            questionBoard.createdDate.dayOfMonth().eq(LocalDateTime.now().getDayOfMonth()),
            searchByCategoryEqual(cond.getQuestionCategory()),
            searchByBoardStatusEqual(cond.getBoardStatus()))
        .groupBy(questionBoard.id)
        .orderBy(boardLike.countDistinct().desc(), questionBoard.createdDate.desc())
        .limit(3)
        .fetch();
    return responses;
  }


  private BoardSort getSort(BoardSort sort) {
    if (Objects.isNull(sort)) {
      sort = BoardSort.EMPTY;
    } else if (sort.equals(BoardSort.LIKE)) {
      return BoardSort.LIKE;
    } else if (sort.equals(BoardSort.PRIZE)) {
      return BoardSort.PRIZE;
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

    } else if (getSort(sort).equals(BoardSort.PRIZE) && getSortDirection(sortDirection).equals(
        SortDirection.DESC)) {
      return questionBoard.prizePoint.desc();
    } else if (getSort(sort).equals(BoardSort.PRIZE) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return questionBoard.prizePoint.asc();

    } /*else if (getSort(sort).equals(BoardSort.EMPTY) && getSortDirection(sortDirection).equals(
        SortDirection.DESC)) {
      return questionBoard.createdDate.desc();
    }*/ else if (getSort(sort).equals(BoardSort.EMPTY) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return questionBoard.createdDate.asc();
    } else if (getSort(sort).equals(BoardSort.COMMENT) && getSortDirection(sortDirection).equals(
        SortDirection.DESC)) {
      return comment.countDistinct().desc();
    } else if (getSort(sort).equals(BoardSort.COMMENT) && getSortDirection(sortDirection).equals(
        SortDirection.ASC)) {
      return comment.countDistinct().asc();
    }
    return questionBoard.createdDate.desc();
  }
//  private OrderSpecifier orderByCond(BoardSort sort) {
//    getSort(sort);
//    if (getSort(sort).equals(BoardSort.LIKE)) {
//      return boardLike.countDistinct().desc();
//    } else if (getSort(sort).equals(BoardSort.PRIZE)) {
//      return questionBoard.prizePoint.desc();
//    } else if (getSort(sort).equals(BoardSort.EMPTY)) {
//      return questionBoard.createdDate.desc();
//    }
//    return questionBoard.createdDate.desc();
//  }

  private BooleanExpression searchByTitle(String title) {
    return Objects.nonNull(title) ? questionBoard.title.contains(title) : null;
  }

  private BooleanExpression searchByContent(String content) {
    return Objects.nonNull(content) ? questionBoard.content.contains(content) : null;
  }

  private BooleanExpression searchByCategoryEqual(QuestionCategory questionCategory) {
    return Objects.nonNull(questionCategory) ? questionBoard.questionCategory.eq(questionCategory)
        : null;
  }

  private BooleanExpression searchByNickname(String nickname) {
    return Objects.nonNull(nickname) ? user.nickname.contains(nickname) : null;
  }

  private BooleanExpression searchByBoardStatusEqual(BoardStatus boardStatus) {
    return Objects.nonNull(boardStatus) ? questionBoard.status.eq(boardStatus)
        : null;
  }
}
