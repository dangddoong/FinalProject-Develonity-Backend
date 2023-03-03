package com.develonity.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentLike is a Querydsl query type for CommentLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentLike extends EntityPathBase<CommentLike> {

    private static final long serialVersionUID = 2037209224L;

    public static final QCommentLike commentLike = new QCommentLike("commentLike");

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCommentLike(String variable) {
        super(CommentLike.class, forVariable(variable));
    }

    public QCommentLike(Path<? extends CommentLike> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentLike(PathMetadata metadata) {
        super(CommentLike.class, metadata);
    }

}

