package com.develonity.comment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReplyComment is a Querydsl query type for ReplyComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReplyComment extends EntityPathBase<ReplyComment> {

    private static final long serialVersionUID = -746231709L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReplyComment replyComment = new QReplyComment("replyComment");

    public final com.develonity.user.entity.QTimeStamp _super = new com.develonity.user.entity.QTimeStamp(this);

    public final QComment comment;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QReplyComment(String variable) {
        this(ReplyComment.class, forVariable(variable), INITS);
    }

    public QReplyComment(Path<? extends ReplyComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReplyComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReplyComment(PathMetadata metadata, PathInits inits) {
        this(ReplyComment.class, metadata, inits);
    }

    public QReplyComment(Class<? extends ReplyComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QComment(forProperty("comment")) : null;
    }

}

