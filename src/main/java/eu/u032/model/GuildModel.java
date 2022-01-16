package eu.u032.model;

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

	private String prefix = "!";

	private long mute = 0;

	private long logs = 0;

	private long mod = 0;
}
