package eu.u032.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.GuildManager;
import eu.u032.util.ArgsUtil;
import eu.u032.util.GeneralUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

public class ModroleCommand extends Command {
	final GuildManager manager;

	public ModroleCommand(GuildManager manager) {
		this.manager = manager;
		this.name = MessageUtil.getMessage("command.modrole.name");
		this.help = MessageUtil.getMessage("command.modrole.help");
		this.arguments = MessageUtil.getMessage("command.modrole.arguments");
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(final CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			MessageUtil.sendError(event, "error.missing.args");
			return;
		}

		final Role role = ArgsUtil.getRole(event, event.getArgs());
		final Role modRole = GeneralUtil.getModRole(event.getGuild());

		if (role == null) {
			MessageUtil.sendError(event, "error.role.not.found");
			return;
		}
		if (role.getIdLong() == (modRole == null ? 0 : modRole.getIdLong())) {
			MessageUtil.sendError(event, "error.role.already.set");
		}

		manager.setMod(event.getGuild(), role.getIdLong());

		MessageUtil.sendSuccessMessage(event, "Moderator role changed to **" + role.getName() + "**");
	}
}
