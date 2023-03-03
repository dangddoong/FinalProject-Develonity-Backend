package com.develonity.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGiftCard is a Querydsl query type for GiftCard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGiftCard extends EntityPathBase<GiftCard> {

    private static final long serialVersionUID = -1681703139L;

    public static final QGiftCard giftCard = new QGiftCard("giftCard");

    public final EnumPath<GiftCardCategory> category = createEnum("category", GiftCardCategory.class);

    public final StringPath details = createString("details");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final StringPath lastModifiedDate = createString("lastModifiedDate");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> stockQuantity = createNumber("stockQuantity", Integer.class);

    public QGiftCard(String variable) {
        super(GiftCard.class, forVariable(variable));
    }

    public QGiftCard(Path<? extends GiftCard> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGiftCard(PathMetadata metadata) {
        super(GiftCard.class, metadata);
    }

}

