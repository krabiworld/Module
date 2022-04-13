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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.module.structure.AbstractCommand;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

import static org.module.Constants.*;

@Component
@Command(
	name = "command.user.name",
	args = "command.user.args",
	help = "command.user.help",
	category = "category.information"
)
public class UserCommand extends AbstractCommand {
    @Override
    protected void execute(CommandContext ctx) {
		Member member = ctx.getMember();

		if (!ctx.getArgs().isEmpty()) {
			member = ctx.findMember(ctx.getArgs());
		}
		if (member == null) {
			ctx.sendHelp();
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

        ctx.send(embed);
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
