create table account
(
    id       bigserial      not null
        constraint account_pk primary key,
    email    varchar unique not null,
    password varchar        not null,
    status   varchar(20)    not null
);

-- Password is 123456789
insert into account (email, password, status)
values ('admin@localhost', '$2y$10$uzsDS5ckMoS7myXndpU34OVZng.8bpGfHvIpcHo15tjPi4mL0BsYO', 'VERIFIED');
