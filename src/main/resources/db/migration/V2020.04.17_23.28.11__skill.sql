create table player_skill
(
    id        bigserial        not null
        constraint player_skill_pk primary key,
    expertise double precision not null,
    skill     varchar          not null,
    player_id bigserial
        constraint player_skill_player_id_fk
            references player  not null
);


