create table bank_account_log
(
    id        bigserial not null primary key,
    owner_id  bigint    not null references player on update cascade on delete cascade,
    time      timestamp not null,
    change    bigint    not null,
    increased bool      not null
);
