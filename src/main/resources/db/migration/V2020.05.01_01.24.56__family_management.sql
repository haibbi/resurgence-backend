alter table family
    add consultant_id bigint references player;

create table chief
(
    chief_id  bigint not null primary key references player,
    family_id bigint not null references family
);

create table chief_members
(
    chief_chief_id bigint not null references chief,
    members_id     bigint not null unique references player,
    primary key (chief_chief_id, members_id)
);
