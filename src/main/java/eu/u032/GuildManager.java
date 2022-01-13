/*
 * UASM Discord Bot.
 * Copyright (C) 2022 untled032, Headcrab

 * UASM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * UASM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with UASM. If not, see https://www.gnu.org/licenses/.
 */

package eu.u032;

import com.jagrosh.jdautilities.command.GuildSettingsManager;
import com.jagrosh.jdautilities.command.GuildSettingsProvider;
import eu.u032.models.GuildModel;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Collections;

import static eu.u032.utils.SessionFactoryUtil.getSessionFactory;

public class GuildManager implements GuildSettingsManager {

	@Override
	public GuildSettings getSettings(Guild guild) {
		Session session = getSessionFactory().openSession();
		GuildModel guildModel = session.get(GuildModel.class, guild.getIdLong());
		session.close();
		return new GuildSettings(guildModel);
	}

	public void setPrefix(Guild guild, String prefix) {
		Session session = getSessionFactory().openSession();

		GuildModel guildModel = session.get(GuildModel.class, guild.getIdLong());
		guildModel.setPrefix(prefix);

		session.update(guildModel);
		session.flush();
		session.close();
	}

	public void setLogs(Guild guild, long logsId) {
		Session session = getSessionFactory().openSession();

		GuildModel guildModel = session.get(GuildModel.class, guild.getIdLong());
		guildModel.setLogs(logsId);

		session.update(guildModel);
		session.flush();
		session.close();
	}

	public void setMute(Guild guild, long muteId) {
		Session session = getSessionFactory().openSession();

		GuildModel guildModel = session.get(GuildModel.class, guild.getIdLong());
		guildModel.setMute(muteId);

		session.update(guildModel);
		session.flush();
		session.close();
	}

	public void setMod(Guild guild, long modId) {
		Session session = getSessionFactory().openSession();

		GuildModel guildModel = session.get(GuildModel.class, guild.getIdLong());
		guildModel.setMod(modId);

		session.update(guildModel);
		session.flush();
		session.close();
	}

	@Getter
	public static class GuildSettings implements GuildSettingsProvider {
		private final long mute, logs, mod;
		private final String prefix;
		
		private GuildSettings(GuildModel guildModel) {
			this.mute = guildModel.getMute();
			this.logs = guildModel.getLogs();
			this.mod = guildModel.getMod();
			this.prefix = guildModel.getPrefix();
		}

		@Override
		public Collection<String> getPrefixes() {
			return Collections.singleton(prefix);
		}
	}
}
