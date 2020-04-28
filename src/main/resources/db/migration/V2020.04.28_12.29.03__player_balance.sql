alter table player
    alter column balance type bigint using balance::bigint;
alter table player_item
    alter column quantity type bigint using quantity::bigint;
