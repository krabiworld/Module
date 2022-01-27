create table warns
(
	id       bigint generated always as identity primary key,
	guild_id bigint not null,
	user_id  bigint not null,
	reason   text
);
