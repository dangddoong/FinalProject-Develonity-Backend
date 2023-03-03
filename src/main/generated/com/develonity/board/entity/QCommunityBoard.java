package com.develonity.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityBoard is a Querydsl query type for CommunityBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityBoard extends EntityPathBase<CommunityBoard> {

    private static final long serialVersionUID = 1361099538L;

    public static final QCommunityBoard communityBoard = new QCommunityBoard("communityBoard");

    public final QBoard _super = new QBoard(this);

    public final EnumPath<CommunityCategory> communityCategory = createEnum("communityCategory", CommunityCategory.class);

    //inherited
    public final StringPath content = _super.content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    //inherited
    public final StringPath title = _super.title;

    //inherited
    public final NumberPath<Long> userId = _super.userId;

    public QCommunityBoard(String variable) {
        super(CommunityBoard.class, forVariable(variable));
    }

    public QCommunityBoard(Path<? extends CommunityBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityBoard(PathMetadata metadata) {
        super(CommunityBoard.class, metadata);
    }

}

