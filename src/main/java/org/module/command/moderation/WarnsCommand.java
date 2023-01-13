package org.module.command.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.Constants;
import org.module.model.WarnModel;
import org.module.service.ModerationService;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarnsCommand extends Command {
	private final ModerationService moderationService;

	@Autowired
	public WarnsCommand(ModerationService moderationService) {
		this.name = "warns";
		this.description = "Warns member";
		this.category = Category.MODERATION;
		this.options.add(new OptionData(
			OptionType.USER, "user", "User to show warns", false
		));
		this.moderationService = moderationService;
	}

	@Override
	protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user", ctx.getMember());

		if (member == null || member.getUser().isBot()) {
			ctx.replyHelp();
			return;
		}

		List<WarnModel> warnModels = moderationService.getWarns(member);

		StringBuilder warnsMessage = new StringBuilder("Warns count: " + warnModels.size() + "\n");
		for (WarnModel warnModel : warnModels) {
			warnsMessage.append("ID: `").append(warnModel.getId()).append("`").append(" ")
				.append(member.getAsMention())
				.append(warnModel.getReason().isEmpty() ? "" : ": ")
				.append(warnModel.getReason()).append("\n");
		}

		EmbedBuilder embed = new EmbedBuilder()
			.setAuthor(member.getUser().getAsTag(), null, member.getEffectiveAvatarUrl())
			.setColor(Constants.DEFAULT)
			.setDescription(warnsMessage.isEmpty() ? "" : warnsMessage)
			.setFooter("ID: " + member.getId());
		ctx.reply(embed.build());
	}
}
