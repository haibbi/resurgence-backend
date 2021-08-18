create table quests
(
    id           bigserial
        constraint quests_pk primary key,
    player_id    bigserial
        references player
        constraint player_quests_id_fk not null,
    quest        text not null,
    status       text,
    created_time timestamp default now()
);

create index player_quests_idx on quests (player_id);
create index player_unique_quest_idx on quests (player_id, quest);
