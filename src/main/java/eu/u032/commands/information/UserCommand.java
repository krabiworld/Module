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

package eu.u032.commands.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.util.ArgsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.List;

public class UserCommand extends Command {
    public UserCommand() {
        this.name = MessageUtil.getMessage("command.user.name");
        this.help = MessageUtil.getMessage("command.user.help");
        this.arguments = MessageUtil.getMessage("command.user.arguments");
        this.category = Constants.INFORMATION;
    }

    @Override
    protected void execute(CommandEvent event) {
		Member member = event.getMember();

        if (!event.getArgs().isEmpty()) {
            member = ArgsUtil.getMember(event, event.getArgs());
        }
        if (member == null) {
			MessageUtil.sendHelp(event, this);
            return;
        }

        User.Profile profile = member.getUser().retrieveProfile().complete();

        String description = String.format("**Username:** %s\n%s%s%s%s",
			member.getUser().getAsTag(),
			getStatus(member.getOnlineStatus()),
			getActivities(member.getActivities()),
			getJoinedAt(member.getTimeJoined()),
			getRegisteredAt(member.getTimeCreated()));

        EmbedBuilder embed = new EmbedBuilder()
			.setAuthor("Information about " + member.getUser().getName(),
				null, member.getEffectiveAvatarUrl())
			.setColor(member.getColor())
			.setDescription(description)
			.setThumbnail(member.getEffectiveAvatarUrl())
			.setFooter("ID: " + member.getId());

        if (profile.getBannerUrl() != null) embed.setImage(profile.getBannerUrl() + "?size=512");

        event.reply(embed.build());
    }

    private String getStatus(OnlineStatus onlineStatus) {
         String status = switch (onlineStatus) {
            case ONLINE -> "<:online:925113750598598736>Online";
            case IDLE -> "<:idle:925113750254682133>Idle";
            case DO_NOT_DISTURB -> "<:dnd:925113750896398406>Do Not Disturb";
            default -> "<:offline:925113750581817354>Offline";
         };
        return "**Status:**" + status + "\n";
    }

    private String getActivities(List<Activity> activityList) {
        StringBuilder activities = new StringBuilder();
        for (Activity activity : activityList) {
            if (activity.getType() == Activity.ActivityType.CUSTOM_STATUS)
                activities.append("**Custom Status:** ")
					.append(activity.getEmoji() == null ? "" : activity.getEmoji().getAsMention() + " ");
            if (activity.getType() == Activity.ActivityType.DEFAULT)
                activities.append("**Playing:** ");
            if (activity.getType() == Activity.ActivityType.COMPETING)
                activities.append("**Competing in:** ");
            if (activity.getType() == Activity.ActivityType.LISTENING)
                activities.append("**Listening to:** ");
            if (activity.getType() == Activity.ActivityType.STREAMING)
                activities.append("**Streaming:** ");
            if (activity.getType() == Activity.ActivityType.WATCHING)
                activities.append("**Watching:** ");
            activities.append(activity.getName()).append("\n");
        }
        return activities.toString();
    }

    private String getJoinedAt(OffsetDateTime time) {
        return String.format("**Joined at:** <t:%s:D> (<t:%s:R>)\n",
			time.toEpochSecond(), time.toEpochSecond());
    }

    private String getRegisteredAt(OffsetDateTime time) {
        return String.format("**Registered at:** <t:%s:D> (<t:%s:R>)",
			time.toEpochSecond(), time.toEpochSecond());
    }
}
