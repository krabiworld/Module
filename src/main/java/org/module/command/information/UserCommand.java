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
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.List;

import static org.module.Constants.*;

@Component
public class UserCommand extends Command {
	public UserCommand() {
		this.name = "user";
		this.description = "User information";
		this.category = Category.INFORMATION;
		this.options.add(
			new OptionData(OptionType.USER, "user", "User to show information", false)
		);
	}

    @Override
    protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user", ctx.getMember());

		User.Profile profile = member.getUser().retrieveProfile().complete();

		String description = MessageFormat.format("**Username:** {0}\n{1}{2}{3}{4}",
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

		if (!member.getRoles().isEmpty()) embed.addField(getRolesField(member));
		if (profile.getBannerUrl() != null) embed.setImage(profile.getBannerUrl() + "?size=512");

        ctx.reply(embed.build());
    }

	private String getStatus(OnlineStatus onlineStatus) {
		StringBuilder status = new StringBuilder("**Status**:");
         switch (onlineStatus) {
            case ONLINE -> status.append(ONLINE).append("Online");
            case IDLE -> status.append(IDLE).append("Idle");
            case DO_NOT_DISTURB -> status.append(DND).append("Do Not Disturb");
            default -> status.append(OFFLINE).append("Offline");
         }
        return status.append("\n").toString();
    }

    private String getActivities(List<Activity> activityList) {
        StringBuilder activities = new StringBuilder();
        for (Activity activity : activityList) {
			switch (activity.getType()) {
				case CUSTOM_STATUS -> activities.append("**Custom Status:** ")
					.append(activity.getEmoji() == null ? "" : activity.getEmoji().getAsMention() + " ");
				case PLAYING -> activities.append("**Playing:** ");
				case COMPETING -> activities.append("**Competing in:** ");
				case LISTENING -> activities.append("**Listening to:** ");
				case STREAMING -> activities.append("**Streaming:** ");
				case WATCHING -> activities.append("**Watching:** ");
			}
            activities.append(activity.getName()).append("\n");
        }
        return activities.toString();
    }

    private String getJoinedAt(OffsetDateTime time) {
        return MessageFormat.format("**Joined at:** <t:{0,number,#}:D> (<t:{0,number,#}:R>)\n",
			time.toEpochSecond());
    }

    private String getRegisteredAt(OffsetDateTime time) {
        return MessageFormat.format("**Registered at:** <t:{0,number,#}:D> (<t:{0,number,#}:R>)",
			time.toEpochSecond());
    }

	private MessageEmbed.Field getRolesField(Member member) {
		StringBuilder roles = new StringBuilder();

		member.getRoles().forEach(role -> roles.append(role.getAsMention()).append(" "));

		return new MessageEmbed.Field("Roles", roles.toString(), false);
	}
}
