package org.module.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "guilds")
@Getter
@Setter
public class GuildModel {
	@Id
	@Column(name = "guild_id")
	private long id;

	private long logs = 0;

	private long mod = 0;
}
