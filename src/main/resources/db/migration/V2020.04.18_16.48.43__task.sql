create table task_log
(
    id                  bigserial not null
        constraint task_log_pk primary key,
    created_date        timestamp,
    task                varchar,
    created_by_id       bigserial
        constraint task_log_created_player_id_fk references player (id)
);
