alter table announcement
    drop column if exists player_id cascade;
alter table announcement
    rename column general to secret;
alter table family
    add column image varchar
