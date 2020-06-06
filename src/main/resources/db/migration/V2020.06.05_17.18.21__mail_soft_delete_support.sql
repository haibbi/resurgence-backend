alter table mail
    add deleted bool default false not null;
alter table mail
    add deleted_by_sender bool default false not null;
