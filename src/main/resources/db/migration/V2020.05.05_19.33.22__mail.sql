create table mail
(
    id      bigserial not null primary key,
    from_id bigint    not null references player on update cascade on delete cascade,
    to_id   bigint    not null references player on update cascade on delete cascade,
    content varchar   not null,
    time    timestamp not null,
    read    bool      not null
);
create table reported_mail
(
    mail_id     bigint    not null primary key references mail,
    from_id     bigint    not null references player,
    to_id       bigint    not null references player,
    content     varchar   not null,
    time        timestamp not null,
    report_time timestamp not null default now()
);
