create table online_state
(
    name   varchar   not null,
    online bool      not null,
    time   timestamp not null default now(),
    primary key (name, online, time)
);
create index online_state_name_time_idx on online_state (name, time desc);
