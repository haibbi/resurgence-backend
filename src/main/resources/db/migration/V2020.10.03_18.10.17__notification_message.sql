create table notification_message
(
    id      bigserial not null primary key,
    to_id   bigint    not null references player on update cascade on delete cascade,
    title   varchar   not null,
    content varchar   not null,
    time    timestamp not null default now(),
    deleted bool      not null default false
);
