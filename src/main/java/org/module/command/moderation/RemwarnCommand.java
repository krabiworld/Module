package org.module.command.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.model.WarnModel;
import org.module.service.ModerationService;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class RemwarnCommand extends Command {
	private final ModerationService moderationService;

	@Autowired
	public RemwarnCommand(ModerationService moderationService) {
		this.name = "remwarn";
		this.description = "Remove warn from member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.INTEGER, "case", "Case to remove warn", true
		));
		this.moderationService = moderationService;
	}

	@Override
	protected void execute(CommandContext ctx) {
		WarnModel warnModel = moderationService.getWarn(ctx.getOptionAsInt("case"));

		if (warnModel == null || warnModel.getGuild() != ctx.getGuild().getIdLong()) {
			ctx.replyError("Warn not found.");
			return;
		}

		Member member = ctx.getGuild().getMemberById(warnModel.getUser());

		if (member == null) {
			ctx.replyHelp();
			return;
		}

		moderationService.removeWarn(warnModel);

		ctx.replySuccess(MessageFormat.format("Moderator **{0}** removed warning (ID: `{1}`) to **{2}**.",
			ctx.getMember().getEffectiveName(),
			warnModel.getId(),
			member.getUser().getAsTag()));
	}
}
