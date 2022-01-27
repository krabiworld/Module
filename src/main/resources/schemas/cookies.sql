create table cookies
(
	guild_id bigint not null primary key,
	user_id  bigint,
	count    bigint
);
