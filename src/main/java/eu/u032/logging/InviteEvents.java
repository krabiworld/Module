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
