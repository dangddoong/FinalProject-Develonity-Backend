package com.develonity.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGiftCardImage is a Querydsl query type for GiftCardImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGiftCardImage extends EntityPathBase<GiftCardImage> {

    private static final long serialVersionUID = -48208834L;

    public static final QGiftCardImage giftCardImage = new QGiftCardImage("giftCardImage");

    public final com.develonity.user.entity.QTimeStamp _super = new com.develonity.user.entity.QTimeStamp(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> giftCardId = createNumber("giftCardId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public QGiftCardImage(String variable) {
        super(GiftCardImage.class, forVariable(variable));
    }

    public QGiftCardImage(Path<? extends GiftCardImage> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGiftCardImage(PathMetadata metadata) {
        super(GiftCardImage.class, metadata);
    }

}

