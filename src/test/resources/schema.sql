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


create table orders
(
    order_id     binary(16)   not null primary key,
    email        varchar(50)  not null,
    address      varchar(200) not null,
    postcode     varchar(200) not null,
    order_status varchar(50)  not null,
    created_at   datetime(6)  not null,
    updated_at   datetime(6)  null
);


create table order_items
(
    seq        bigint auto_increment primary key,
    order_id   binary(16)  not null,
    product_id binary(16)  not null,
    product_type varchar(20)                    not null,
    price      bigint      not null,
    quantity   int         not null,
    created_at datetime(6) not null,
    updated_at datetime(6) null,
    constraint fk_order_items_to_order
        foreign key (order_id) references orders (order_id)
            on delete cascade,
    constraint fk_order_items_to_product
        foreign key (product_id) references products (product_id)
);

create index order_id
    on order_items (order_id);
