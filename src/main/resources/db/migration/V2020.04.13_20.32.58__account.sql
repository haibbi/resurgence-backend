create table account
(
    id       serial      not null
        constraint account_pk primary key,
    email    varchar     not null,
    password varchar     not null,
    status   varchar(20) not null
);

create unique index account_email_index on account (email);

