drop table if exists users;

create table users
(
    id int not null,
    created_at datetime null,
    updated_at datetime null,
    email_address varchar(256) not null,
    password varchar(256) not null,
    name varchar(256) not null,
    constraint users_pk
        primary key (id)
);

alter table users modify id int auto_increment;

