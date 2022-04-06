/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.command.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.*;
import org.module.Constants;
import org.module.Locale;
import org.module.service.MessageService;
import org.module.util.ArgsUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

import static org.module.Constants.*;

@Component
public class UserCommand extends Command {
	private final MessageService messageService;

	@Autowired
	public UserCommand(MessageService messageService) {
		this.messageService = messageService;
        this.name = "user";
        this.category = Constants.INFORMATION;
    }

    @Override
    protected void execute(CommandEvent event) {
		Locale locale = messageService.getLocale(event.getGuild());
		Member member = event.getMember();

		if (!event.getArgs().isEmpty()) {
			member = ArgsUtil.getMember(event, event.getArgs());
		}
		if (member == null) {
			messageService.sendHelp(event, this, locale);
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
            case ONLINE -> ONLINE + "Online";
            case IDLE -> IDLE + "Idle";
            case DO_NOT_DISTURB -> DND + "Do Not Disturb";
            default -> OFFLINE + "Offline";
         };
        return "**Status:**" + status + "\n";
    }

    private String getActivities(List<Activity> activityList) {
        StringBuilder activities = new StringBuilder();
        for (Activity activity : activityList) {
            if (activity.getType() == Activity.ActivityType.CUSTOM_STATUS)
                activities.append("**Custom Status:** ")
					.append(activity.getEmoji() == null ? "" : activity.getEmoji().getAsMention() + " ");
            if (activity.getType() == Activity.ActivityType.PLAYING)
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
