create table lottery_ticket
(
    id            bigserial                                      not null
        constraint lottery_ticket_pk primary key,
    player_id     bigserial
        constraint lottery_ticket_player_id_fk references player not null,
    purchase_time timestamp                                      not null
);
