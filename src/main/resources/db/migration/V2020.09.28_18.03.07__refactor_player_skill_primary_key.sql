alter table player_skill drop column id;
alter table player_skill add primary key(player_id, skill);
