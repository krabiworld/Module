package eu.u032.GeneralEvents;

import eu.u032.Config;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class InviteEvents extends ListenerAdapter {

    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent event) {
        Invite invite = event.getInvite();
        User user = event.getInvite().getInviter();

        if (user == null) return;

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setColor(Config.getColorCreate())
                .setDescription(String.format("%s created an [invite](%s)", user.getAsMention(), invite.getUrl()))
                .setFooter("ID: " + user.getId());
        Utils.sendLog(event.getGuild(), embed);
    }

    @Override
    public void onGuildInviteDelete(GuildInviteDeleteEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Invite deleted")
                .setColor(Config.getColorDelete())
                .setDescription(String.format("Invite `%s` deleted", event.getCode()));
        Utils.sendLog(event.getGuild(), embed);
    }

}
