create extension if not exists "uuid-ossp";

create table if not exists photo
(
    id                      UUID unique         not null default uuid_generate_v1(),
    username                varchar(255)        not null,
    description             varchar(255),
    photo                   bytea               not null,
    country_code            varchar(10)         not null,
    primary key (id)
);

alter table photo
    owner to postgres;
