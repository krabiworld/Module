package eu.u032.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.service.OwnerService;
import eu.u032.util.MessageUtil;
import groovy.lang.GroovyShell;

public class EvalCommand extends Command {
	public EvalCommand() {
		this.name = MessageUtil.getMessage("command.eval.name");
		this.hidden = true;
	}

	@Override
	protected void execute(final CommandEvent event) {
		if (!event.isOwner() || new OwnerService().findById(event.getMember().getIdLong()) == null) {
			return;
		}

		String args = event.getArgs();

		if (args.isEmpty()) {
			MessageUtil.sendError(event, "error.missing.args");
			return;
		}

		final GroovyShell shell = new GroovyShell();

		shell.setProperty("event", event);
		shell.setProperty("jda", event.getJDA());
		shell.setProperty("guild", event.getGuild());
		shell.setProperty("channel", event.getChannel());
		shell.setProperty("member", event.getMember());
		shell.setProperty("client", event.getClient());

		event.getChannel().sendTyping().queue();
		event.async(() -> {
			try {
				event.replySuccess("Evaluated Successfully:\n" + shell.evaluate(args));
			} catch (Exception e) {
				MessageUtil.sendErrorMessage(event, e.getMessage());
			}
		});
	}
}
