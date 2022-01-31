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

package org.module.commands.information;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

public class UserCommand extends SlashCommand {
	public UserCommand() {
        this.name = PropertyUtil.getProperty("command.user.name");
        this.help = PropertyUtil.getProperty("command.user.help");
        this.arguments = PropertyUtil.getProperty("command.user.arguments");
		this.options = Collections.singletonList(new OptionData(
			OptionType.USER, "user", "User to get information."
		));
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

        event.reply(command(member));
    }

	@Override
	protected void execute(SlashCommandEvent event) {
		OptionMapping option = event.getOption("user");

		if (option == null) {
			event.replyEmbeds(command(event.getMember())).queue();
			return;
		}

		event.replyEmbeds(command(option.getAsMember())).queue();
	}

	private MessageEmbed command(Member member) {
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
		return embed.build();
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
