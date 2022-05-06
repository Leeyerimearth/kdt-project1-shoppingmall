create table products
(
    product_id   binary(16)                     not null
        primary key,
    product_type varchar(20)                    not null,
    product_name varchar(500)                   not null,
    description  varchar(500)                   null,
    price        bigint                         not null,
    brand_name   varchar(500)                   not null,
    size         varchar(20)                    not null,
    status       varchar(20) default 'IN_STOCK' not null
);