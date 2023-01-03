package org.module.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class KickCommand extends Command {
	public KickCommand() {
		this.name = "kick";
		this.description = "Kick member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.USER, "user", "User to kick", true
		));
		this.options.add(new OptionData(
			OptionType.STRING, "reason", "Kick reason", false
		));
		this.botPermissions = new Permission[]{Permission.KICK_MEMBERS};
		this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
	}

	@Override
	protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user");
		String reason = ctx.getOptionAsString("reason");

		if (member == null) {
			ctx.replyHelp();
			return;
		}
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.replyError("I cannot kick member with role equal or higher than me.");
			return;
		}
		if (member == ctx.getMember()) {
			ctx.replyError("You cannot kick yourself.");
			return;
		}

		ctx.getGuild().kick(member).reason(reason).queue();
		ctx.replySuccess(MessageFormat.format("**{0}** kicked by moderator **{1}**{2}",
			member.getUser().getAsTag(),
			ctx.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason));
	}
}
