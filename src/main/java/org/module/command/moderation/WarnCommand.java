package org.module.command.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.service.ModerationService;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class WarnCommand extends Command {
	private final ModerationService moderationService;

	@Autowired
	public WarnCommand(ModerationService moderationService) {
		this.name = "warn";
		this.description = "Warn member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.USER, "user", "User to warn", true
		));
		this.options.add(new OptionData(
			OptionType.STRING, "reason", "Warn reason", false
		));
		this.moderationService = moderationService;
	}

	@Override
	protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user");
		String reason = ctx.getOptionAsString("reason");

		if (member == null || member.getUser().isBot()) {
			ctx.replyHelp();
			return;
		}
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.replyError("I cannot warnModel member with role equal or higher than me.");
			return;
		}
		if (member == ctx.getMember()) {
			ctx.replyError("You cannot warnModel yourself.");
			return;
		}

		long warnId = moderationService.warn(member, reason);

		ctx.replySuccess(MessageFormat.format("**{0}** warned (ID: `#{1}`) by moderator **{2}**{3}",
			member.getUser().getAsTag(), warnId,
			ctx.getMember().getEffectiveName(),
			reason.isEmpty() ? "." : " with reason: " + reason));
	}
}
