package eu.u032.commands.settings;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Constants;
import eu.u032.GuildManager;
import eu.u032.utils.ArgsUtil;
import eu.u032.utils.GeneralUtil;
import eu.u032.utils.MsgUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;

public class ModroleCommand extends Command {
	final GuildManager manager;

	public ModroleCommand(GuildManager manager) {
		this.manager = manager;
		this.name = "modrole";
		this.help = "Set mod role";
		this.arguments = "<@Role | ID>";
		this.category = Constants.SETTINGS;
		this.userPermissions = new Permission[]{Permission.MANAGE_SERVER};
	}

	@Override
	protected void execute(final CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			MsgUtil.sendError(event, Constants.MISSING_ARGS);
			return;
		}

		final Role role = ArgsUtil.getRole(event, event.getArgs());
		final Role modRole = GeneralUtil.getModRole(event.getGuild());

		if (role == null) {
			MsgUtil.sendError(event, "Role not found.");
			return;
		}
		if (role.getIdLong() == (modRole == null ? 0 : modRole.getIdLong())) {
			MsgUtil.sendError(event, "This role already set.");
		}

		manager.setMod(event.getGuild(), role.getIdLong());

		MsgUtil.sendSuccess(event, "Moderator role changed to **" + role.getName() + "**");
	}
}
