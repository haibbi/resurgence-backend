alter table family_bank_log
    drop column if exists family;
alter table family_bank_log
    add column family_id bigint references family on update cascade on delete cascade;

create index on task_log (task, created_by_id, created_date desc);
