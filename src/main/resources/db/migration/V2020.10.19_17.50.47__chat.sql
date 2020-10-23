create table topic
(
    name varchar not null primary key
);

create table topic_subscriptions
(
    name       varchar not null,
    topic_name varchar not null references topic on update cascade on delete cascade,
    primary key (topic_name, name)
);

create table topic_messages
(
    sequence   bigint    not null,
    topic_name varchar   not null references topic on update cascade on delete cascade,
    content    varchar   not null,
    _from      varchar   not null,
    time       timestamp not null default now(),
    primary key (topic_name, sequence)
);
