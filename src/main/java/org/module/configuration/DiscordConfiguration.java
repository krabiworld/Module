package org.module.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("discord")
@Getter
@Setter
public class DiscordConfiguration {
	private String token;

	private String ownerId;

	private String guildId;
}
