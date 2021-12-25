package eu.u032.BotEvents;

import eu.u032.Utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;

public class InviteEvents extends ListenerAdapter {

    // Invite created
    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent event) {
        Invite invite = event.getInvite();
        User inviter = event.getInvite().getInviter();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(inviter.getAsTag(), inviter.getEffectiveAvatarUrl(), inviter.getEffectiveAvatarUrl())
                .setColor(Color.decode("#89d561"))
                .setDescription(String.format("%s created an [invite](https://discord.gg/%s)", inviter.getAsMention(), invite.getCode()))
                .setFooter("User ID: " + inviter.getId())
                .setTimestamp(new Date().toInstant());
        event.getJDA()
                .getTextChannelById(Config.getString("LOGS_CHANNEL"))
                .sendMessageEmbeds(embed.build())
                .queue();

    }

}
