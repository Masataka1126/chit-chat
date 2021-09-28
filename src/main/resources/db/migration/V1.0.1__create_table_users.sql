drop table if exists users;

create table users
(
    id int unique,
    created_at datetime null,
    updated_at datetime null,
    email_address varchar(256) not null,
    password varchar(50) not null,
    name varchar(50) not null,
    constraint users_pk
        primary key (email_address)
);

alter table users modify id int auto_increment;

