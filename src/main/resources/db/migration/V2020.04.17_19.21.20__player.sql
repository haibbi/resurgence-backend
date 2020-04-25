create table player
(
    id         bigserial       not null
        constraint player_pk primary key,
    balance    integer         not null,
    health     integer         not null,
    honor      integer         not null,
    experience integer         not null,
    image      varchar,
    name       varchar unique  not null,
    account_id bigserial
        constraint player_account_id_fk
            references account not null
);

