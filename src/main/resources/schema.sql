create table guilds
(
	guild_id bigint,
	logs     bigint,
	mod      bigint
);
create table stats
(
	id                bigint not null primary key,
	executed_commands bigint
);
create table warns
(
	id       bigint generated always as identity primary key,
	guild_id bigint not null,
	user_id  bigint not null,
	reason   text
);
