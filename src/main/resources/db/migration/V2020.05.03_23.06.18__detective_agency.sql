create table research_result
(
    id          bigserial not null primary key,
    seeker_id   bigint    not null references player on update cascade on delete cascade,
    wanted_id   bigint    not null references player on update cascade on delete cascade,
    found       bool      not null,
    expire_time timestamp not null
);
