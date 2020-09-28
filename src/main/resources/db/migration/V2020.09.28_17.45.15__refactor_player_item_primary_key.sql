alter table player_item drop column id;
alter table player_item add primary key(player_id, item);
