create table announcement
(
    id        bigserial not null primary key,
    player_id bigint    not null references player on update cascade on delete cascade,
    family_id bigint    not null references family on update cascade on delete cascade,
    title     varchar   not null,
    content   varchar   not null,
    general   bool      not null,
    time      timestamp not null
);
