package eu.u032.logging;

import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InviteEvents extends ListenerAdapter {
    @Override
    public void onGuildInviteCreate(final GuildInviteCreateEvent event) {
        final Invite invite = event.getInvite();
        final User user = event.getInvite().getInviter();

        if (user == null) return;

        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
			.setColor(Utils.getColorGreen())
			.setDescription(String.format("%s created an [invite](%s)", user.getAsMention(), invite.getUrl()))
			.setFooter("ID: " + user.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildInviteDelete(final GuildInviteDeleteEvent event) {
        final EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Invite deleted")
			.setColor(Utils.getColorRed())
			.setDescription(String.format("Invite `%s` deleted", event.getCode()));
        Utils.sendLog(event.getGuild(), embed);
    }
}
