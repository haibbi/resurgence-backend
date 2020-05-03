create table bank_account
(
    owner_id bigint not null primary key references player on update cascade on delete cascade,
    amount   bigint not null
);
