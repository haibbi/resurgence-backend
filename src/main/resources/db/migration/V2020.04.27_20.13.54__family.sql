create table family
(
    id       bigserial                                   not null
        constraint family_pk primary key,
    bank     bigint                                      not null check (bank >= 0),
    building varchar                                     not null,
    don_id   bigint
        constraint family_player_id_fk references player not null,
    name     varchar unique                              not null
);
create table family_members
(
    family_id  bigint not null references family,
    members_id bigint not null unique references player,
    primary key (family_id, members_id)
);
create table family_bank_log
(
    id     bigserial not null
        constraint family_bank_log_pk primary key,
    member varchar   not null,
    amount bigint    not null,
    reason varchar   not null,
    date   timestamp not null
);
