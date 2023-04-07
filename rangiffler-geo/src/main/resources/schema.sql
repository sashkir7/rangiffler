create extension if not exists "uuid-ossp";

create table if not exists country
(
    id                      UUID unique             not null default uuid_generate_v1(),
    code                    varchar(10)     unique  not null,
    name                    varchar(255)    unique  not null,
    primary key (id)
);

alter table country
    owner to postgres;
