create table task_log
(
    id            bigserial not null
        constraint task_log_pk primary key,
    created_date  timestamp not null,
    task          varchar   not null,
    succeed       bool      not null,
    created_by_id bigserial
        constraint task_log_created_player_id_fk references player (id)
);
