alter table family
    add consultant_id bigint references player;

create table chief
(
    chief_id  bigint not null primary key references player,
    family_id bigint references family
);
create table chief_members
(
    chief_chief_id bigint        not null references chief,
    members_id     bigint unique not null references player,
    primary key (chief_chief_id, members_id)
);
create table family_chiefs
(
    family_id       bigint        not null references family,
    chiefs_chief_id bigint unique not null references chief,
    primary key (family_id, chiefs_chief_id)
);
