create extension if not exists "uuid-ossp";

create table if not exists users
(
    id                      UUID unique         not null default uuid_generate_v1(),
    username                varchar(50) unique  not null,
    firstname               varchar(255)        not null,
    lastName                varchar(255)        not null,
    avatar                  bytea,
    primary key (id)
);

alter table users
    owner to postgres;

create table if not exists users_relationship
(
    user_id                 UUID not null,
    partner_id              UUID not null,
    status                  varchar(20) not null,
    primary key (user_id, partner_id),
    constraint fk_user_id foreign key (user_id) references users (id),
    constraint fk_friend_id foreign key (partner_id) references users (id)
);

alter table users_relationship
    owner to postgres;
