create table bank_transfer_log
(
    id          bigserial not null primary key,
    to_id       bigint    not null references player,
    from_id     bigint    not null references player,
    amount      bigint    not null,
    description varchar,
    time        timestamp not null
);
