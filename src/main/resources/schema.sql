DROP TABLE IF EXISTS PRODUCT_BOARD;

-- auto-generated definition
create table PRODUCT_BOARD
(
    ID                BIGINT auto_increment primary key not null ,
    SELLER_ID         VARCHAR(255) not null ,
    NICKNAME          VARCHAR(255) not null ,
    TITLE             VARCHAR(255) not null ,
    CONTENT           VARCHAR(255) not null ,
    UNIT_PRICE        INTEGER not null ,
    CATEGORY_NAME     VARCHAR(255) not null ,
    MAIN_IMAGE_URL    VARCHAR(255) not null ,
    MAX_RENTAL_PERIOD VARCHAR(255) not null ,
    STATUS            VARCHAR(255) not null ,
    WISH_REGION       VARCHAR(255) not null ,
    CREATED_AT        TIMESTAMP not null ,
    MODIFIED_AT       TIMESTAMP not null
);
