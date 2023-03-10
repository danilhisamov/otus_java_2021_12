create table client
(
    id   bigserial   not null primary key,
    name varchar(50) not null
);

create table address
(
    id        bigserial   not null primary key,
    street    varchar(50) not null,
    client_id bigint      not null references client (id)
);
create index idx_address_client_id on address (client_id);


create table phone
(
    id           bigserial   not null primary key,
    number       varchar(50) not null,
    order_column int         not null,
    client_id    bigint      not null references client (id)
);
create index idx_phone_client_id on phone (client_id);


insert into client (name)
values ('Джон Уик'),
       ('Брюс Уэйн');

insert into address (street, client_id)
values ('Отель Континенталь', 1),
       ('Бэт пещера', 2);

insert into phone (number, order_column, client_id)
VALUES ('+1 234 56 78', 0, 1),
       ('+9 876 54 32', 0, 2);
