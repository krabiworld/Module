package org.module.command.moderation;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.Duration;

@Component
public class MuteCommand extends Command {
	public MuteCommand() {
		this.name = "mute";
		this.description = "Mute member";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.USER, "user", "User to mute", true
		));
		this.options.add(new OptionData(
			OptionType.NUMBER, "duration", "Duration", true
		));
		this.options.add(new OptionData(
			OptionType.STRING, "unit", "Duration unit s/m/h/d", true
		));
		this.options.add(new OptionData(
			OptionType.STRING, "reason", "Mute reason", false
		));
	}

	@Override
	protected void execute(CommandContext ctx) {
		Member member = ctx.getOptionAsMember("user");
		int durationInt = ctx.getOptionAsInt("duration");
		String unitOfTime = ctx.getOptionAsString("unit");

		if (member == null) {
			ctx.replyHelp();
			return;
		}
		if (!ctx.getSelfMember().canInteract(member)) {
			ctx.replyError("I cannot mute member with role equal or higher than me.");
			return;
		}
		if (member == ctx.getMember()) {
			ctx.replyError("You cannot mute yourself.");
			return;
		}
		if (member.isTimedOut()) {
			ctx.replyError("This user already muted.");
			return;
		}

		Duration duration;
		try {
			duration = switch (unitOfTime.toLowerCase()) {
				case "s" -> Duration.ofSeconds(durationInt);
				case "m" -> Duration.ofMinutes(durationInt);
				case "h" -> Duration.ofHours(durationInt);
				case "d" -> Duration.ofDays(durationInt);
				default -> throw new Exception(String.format("Unit of time %s not found.", unitOfTime));
			};
		} catch (Exception e) {
			ctx.replyHelp();
			return;
		}

		member.timeoutFor(duration).queue();
		ctx.replySuccess(MessageFormat.format("**{0}** muted by moderator **{1}** for {2}{3}.",
			member.getUser().getAsTag(),
			ctx.getMember().getEffectiveName(),
			durationInt, unitOfTime));
	}
}
