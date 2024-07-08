package org.module.command.owner;

import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class EvalCommand extends Command {
	private final ApplicationContext appCtx;

	@Autowired
	public EvalCommand(ApplicationContext appCtx) {
		this.name = "eval";
		this.ownerCommand = true;
		this.hidden = true;
		this.options.add(
			new OptionData(OptionType.STRING, "code", "Code", true)
		);
		this.appCtx = appCtx;
	}

	@Override
	protected void execute(CommandContext ctx) {
		GroovyShell shell = new GroovyShell();
		shell.setProperty("ctx", ctx);
		shell.setProperty("appCtx", appCtx);

		ctx.getChannel().sendTyping().queue();
		try {
			Instant start = Instant.now();

			Object eval = shell.evaluate(ctx.getOptionAsString("code"));

			Instant finish = Instant.now();

			ctx.replySuccess(String.format(
				"Evaluated successfully! %s ms\n```\n%s\n```", Duration.between(start, finish).toMillis(), eval
			));
		} catch (Exception e) {
			ctx.reply(e.getMessage());
		}
	}
}
