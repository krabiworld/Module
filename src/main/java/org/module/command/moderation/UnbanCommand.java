package org.module.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UnbanCommand extends Command {
	public UnbanCommand() {
		this.name = "unban";
		this.description = "Unban member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.STRING, "user", "User to ban", true
		));
		this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
		this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
	}

	@Override
	protected void execute(CommandContext ctx) {
		String member = ctx.getOptionAsString("user");
		if (!ctx.isBanned(member)) {
			ctx.replyError("Member not found in banned list.");
			return;
		}

		ctx.getGuild().unban(UserSnowflake.fromId(member)).queue();
		ctx.replySuccess(MessageFormat.format("Member **{0}** unbanned by moderator **{1}**.",
			member, ctx.getMember().getEffectiveName()));
	}
}
