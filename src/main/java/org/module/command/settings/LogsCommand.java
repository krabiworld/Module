package org.module.command.settings;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class LogsCommand extends Command {
	public LogsCommand() {
		this.name = "logs";
		this.description = "Server logging";
		this.category = Category.SETTINGS;
		this.children = new Command[]{new OffSubCommand(), new OnSubCommand()};
	}

	@Override
	protected void execute(CommandContext ctx) {}

	public static class OffSubCommand extends Command {
		public OffSubCommand() {
			this.name = "off";
		}

		@Override
		protected void execute(CommandContext ctx) {
			TextChannel currentChannel = ctx.getSettings().getLogsChannel();

			if (currentChannel == null) {
				ctx.replyError("Logs already disabled.");
				return;
			}

			ctx.getClient().getManager().setLogsChannel(ctx.getGuild(), null);
			ctx.replySuccess("Logs are disabled.");
		}
	}

	public static class OnSubCommand extends Command {
		public OnSubCommand() {
			this.name = "on";
			this.options.add(
				new OptionData(OptionType.CHANNEL, "channel", "Channel to reply logs", true)
			);
		}

		@Override
		protected void execute(CommandContext ctx) {
			TextChannel channel = ctx.getOptionAsTextChannel("channel");
			TextChannel currentChannel = ctx.getSettings().getLogsChannel();

			if (channel == currentChannel) {
				ctx.replyError("This channel already set.");
				return;
			}

			ctx.getClient().getManager().setLogsChannel(ctx.getGuild(), channel);

			ctx.replySuccess(MessageFormat.format("Channel changed to {0}", channel.getAsMention()));
		}
	}
}
