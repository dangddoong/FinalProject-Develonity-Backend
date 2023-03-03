package com.develonity.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardLike is a Querydsl query type for BoardLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardLike extends EntityPathBase<BoardLike> {

    private static final long serialVersionUID = 1554367464L;

    public static final QBoardLike boardLike = new QBoardLike("boardLike");

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QBoardLike(String variable) {
        super(BoardLike.class, forVariable(variable));
    }

    public QBoardLike(Path<? extends BoardLike> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardLike(PathMetadata metadata) {
        super(BoardLike.class, metadata);
    }

}

