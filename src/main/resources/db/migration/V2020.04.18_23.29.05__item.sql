create table player_item
(
    id        bigserial       not null
        constraint player_item_pk primary key,
    quantity  int             not null,
    item      varchar         not null,
    player_id bigserial
        constraint player_item_player_id_fk
            references player not null
);
