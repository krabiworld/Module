package eu.u032.events;

import eu.u032.models.GuildModel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.hibernate.Session;

import static eu.u032.utils.SessionFactoryUtil.getSessionFactory;

public class GuildEvents extends ListenerAdapter {
	@Override
	public void onGuildJoin(final GuildJoinEvent event) {
		final Session session = getSessionFactory().openSession();

		final GuildModel guildModel = new GuildModel();
		guildModel.setId(event.getGuild().getIdLong());
		guildModel.setPrefix("!");
		guildModel.setLogs(0);
		guildModel.setMute(0);
		guildModel.setMod(0);

		session.save(guildModel);
		session.flush();
		session.close();
	}

	@Override
	public void onGuildLeave(final GuildLeaveEvent event) {
		final Session session = getSessionFactory().openSession();

		final GuildModel guildModel = session.get(GuildModel.class, event.getGuild().getIdLong());

		session.delete(guildModel);
		session.flush();
		session.close();
	}
}
