package eu.u032.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.model.OwnerModel;
import eu.u032.service.OwnerService;
import eu.u032.util.ArgsUtil;
import eu.u032.util.MessageUtil;
import net.dv8tion.jda.api.entities.User;

public class OwnerCommand extends Command {
	public OwnerCommand() {
		this.name = MessageUtil.getMessage("command.owner.name");
		this.hidden = true;
		this.ownerCommand = true;
	}

	@Override
	protected void execute(final CommandEvent event) {
		String[] args = ArgsUtil.split(event.getArgs());

		if (event.getArgs().isEmpty() || args.length <= 1) {
			MessageUtil.sendError(event, "error.missing.args");
			return;
		}

		User user = ArgsUtil.getUser(event, args[1]);

		if (user == null) {
			MessageUtil.sendError(event, "error.member.not.found");
			return;
		}

		OwnerService ownerService = new OwnerService();

		if (args[0].startsWith("add")) {
			OwnerModel ownerModel = new OwnerModel();
			ownerModel.setId(user.getIdLong());

			ownerService.save(ownerModel);

			MessageUtil.sendSuccessMessage(event,
				"Member **" + user.getAsTag() + "** added to owner list.");
		} else if (args[0].startsWith("remove")) {
			OwnerModel ownerModel = ownerService.findById(user.getIdLong());

			if (ownerModel == null) {
				MessageUtil.sendError(event, "command.owner.error.not.found");
				return;
			}

			ownerService.delete(ownerModel);

			MessageUtil.sendSuccessMessage(event,
				"Member **" + user.getAsTag() + "** removed from owner list.");
		}
	}
}
