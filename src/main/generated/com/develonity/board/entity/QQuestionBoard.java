package com.develonity.board.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQuestionBoard is a Querydsl query type for QuestionBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoard extends EntityPathBase<QuestionBoard> {

    private static final long serialVersionUID = 68729195L;

    public static final QQuestionBoard questionBoard = new QQuestionBoard("questionBoard");

    public final QBoard _super = new QBoard(this);

    //inherited
    public final StringPath content = _super.content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> prizePoint = createNumber("prizePoint", Integer.class);

    public final EnumPath<QuestionCategory> questionCategory = createEnum("questionCategory", QuestionCategory.class);

    public final EnumPath<BoardStatus> status = createEnum("status", BoardStatus.class);

    //inherited
    public final StringPath title = _super.title;

    //inherited
    public final NumberPath<Long> userId = _super.userId;

    public QQuestionBoard(String variable) {
        super(QuestionBoard.class, forVariable(variable));
    }

    public QQuestionBoard(Path<? extends QuestionBoard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestionBoard(PathMetadata metadata) {
        super(QuestionBoard.class, metadata);
    }

}

