package org.module.command.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class SlowmodeCommand extends Command {
	public SlowmodeCommand() {
		this.name = "slowmode";
		this.description = "Set slowmode in current channel";
		this.category = Category.MODERATION;
		this.moderationCommand = true;
		this.options.add(new OptionData(
			OptionType.INTEGER, "duration", "Duration", true
		));
		this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
		this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
	}

    @Override
    protected void execute(CommandContext ctx) {
		int interval = ctx.getOptionAsInt("duration");

        if (interval < 0 || interval > 21600) {
			ctx.replyHelp();
            return;
        }
        if (ctx.getTextChannel().getSlowmode() == interval) {
			ctx.replyError("This value already set.");
            return;
        }

        ctx.getTextChannel().getManager().setSlowmode(interval).queue();
		ctx.replySuccess(MessageFormat.format("Slowmode for channel {0} changed to **{1}**.",
			ctx.getTextChannel().getAsMention(), interval));
    }
}
