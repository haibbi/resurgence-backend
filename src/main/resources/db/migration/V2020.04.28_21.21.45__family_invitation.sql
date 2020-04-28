create table invitation
(
    id        bigserial                                      not null
        constraint invitation_pk primary key,
    player_id bigint
        constraint invitation_player_id_fk references player not null,
    family_id bigint
        constraint invitation_family_id_fk references family not null,
    time      timestamp                                      not null,
    direction varchar                                        not null,
    unique (player_id, family_id)
);
