package org.module.command.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UnmuteCommand extends Command {
	public UnmuteCommand() {
		this.name = "unmute";
		this.description = "Unmute member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.USER, "user", "User to unmute", true
		));
	}

	@Override
	protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user");

		if (member == null) {
			ctx.replyHelp();
			return;
		}
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.replyError("I cannot mute member with role equal or higher than me.");
			return;
		}
		if (!member.isTimedOut()) {
			ctx.replyError("This member was not muted.");
			return;
		}

		member.removeTimeout().queue();
		ctx.replySuccess(MessageFormat.format("**%s** unmuted by moderator **%s**.",
			member.getUser().getAsTag(), ctx.getMember().getEffectiveName()));
	}
}
