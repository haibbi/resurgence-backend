create table account_push_notification_tokens
(
    account_id bigint not null references account,
    token      varchar
)
