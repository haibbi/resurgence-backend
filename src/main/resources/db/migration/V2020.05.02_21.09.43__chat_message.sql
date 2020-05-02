create table message_log
(
    id      uuid      not null primary key,
    content varchar,
    topic   varchar   not null,
    type    varchar   not null,
    player  varchar   not null,
    time    timestamp not null default now()
);
