alter table invitation
    drop constraint invitation_player_id_fk;

alter table invitation
    add constraint invitation_player_id_fk
        foreign key (player_id) references player
            on update cascade on delete cascade;

alter table invitation
    drop constraint invitation_family_id_fk;

alter table invitation
    add constraint invitation_family_id_fk
        foreign key (family_id) references family
            on update cascade on delete cascade;

