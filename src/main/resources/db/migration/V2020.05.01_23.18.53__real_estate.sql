create table deed
(
    building      varchar not null primary key,
    purchase_time timestamp,
    player_id     bigint  not null references player on update cascade on delete cascade
);
