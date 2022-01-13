package eu.u032.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "guilds")
@Getter
@Setter
public class GuildModel {
	@Id
	@Column(name = "guild_id")
	private long id;

	@Column(name = "prefix")
	private String prefix;

	@Column(name = "mute")
	private long mute;

	@Column(name = "logs")
	private long logs;

	@Column(name = "mod")
	private long mod;
}
