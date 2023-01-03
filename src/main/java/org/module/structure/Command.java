/*
 * This file is part of Module.
 *
 * Module is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Module is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.structure;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class Command {
	protected String name = null;

	protected String description = "Without description";

	protected Category category = null;

	protected boolean ownerCommand = false;

	protected boolean moderationCommand = false;

	protected boolean hidden = false;

	protected Command[] children = new Command[0];

	protected List<OptionData> options = new ArrayList<>();

	protected SubcommandGroupData subcommandGroup = null;

	protected Permission[] botPermissions = {};

	protected Permission[] userPermissions = {};

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Category getCategory() {
		return category;
	}

	public Command[] getChildren() {
		return children;
	}

	public boolean isModerationCommand() {
		return moderationCommand;
	}

	public boolean isOwnerCommand() {
		return ownerCommand;
	}

	public boolean isHidden() {
		return hidden;
	}

	public List<OptionData> getOptions() {
		return options;
	}

	public SubcommandGroupData getSubcommandGroup() {
		return subcommandGroup;
	}

	public List<Permission> getUserPermissions() {
		return List.of(userPermissions);
	}

	protected abstract void execute(CommandContext ctx);

	public void run(CommandContext ctx) {
		if (ctx.getSubcommandName() != null) {
			for (Command command : getChildren()) {
				if (command.getName().equals(ctx.getSubcommandName())) {
					command.run(ctx);
					return;
				}
			}
		}

		if (isOwnerCommand() && !ctx.isOwner()) {
			ctx.replyError("You are not owner.");
			return;
		}

		if (isModerationCommand() && !ctx.isModerator()) {
			ctx.replyError("You are not a moderator or the role of a moderator is not set.");
			return;
		}

		Member bot = ctx.getSelfMember();

		if (!bot.hasPermission(Permission.MESSAGE_SEND)) {
			ctx.getUser()
				.openPrivateChannel()
				.flatMap(channel -> channel.sendMessage(
					botPerms(Permission.MESSAGE_SEND.getName())
				))
				.queue();
		}

		if (!bot.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
			ctx.reply(botPerms(Permission.MESSAGE_EMBED_LINKS.getName()));
			return;
		}

		StringBuilder botPermsMsg = new StringBuilder();

		for (Permission permission : botPermissions) {
			if (permission.isChannel()) {
				if (!bot.hasPermission(ctx.getGuildChannel(), permission)) {
					botPermsMsg.append("`").append(permission.getName()).append("` ");
				}
			} else {
				if (!bot.hasPermission(permission)) {
					botPermsMsg.append("`").append(permission.getName()).append("` ");
				}
			}
		}

		if (!botPermsMsg.isEmpty()) {
			ctx.replyError(botPerms(botPermsMsg.toString()));
			return;
		}

		StringBuilder userPermsMsg = new StringBuilder();

		for (Permission permission : userPermissions) {
			if (permission.isChannel()) {
				if (!ctx.getMember().hasPermission(ctx.getGuildChannel(), permission)) {
					userPermsMsg.append("`").append(permission.getName()).append("` ");
				}
			} else {
				if (!ctx.getMember().hasPermission(permission)) {
					userPermsMsg.append("`").append(permission.getName()).append("` ");
				}
			}
		}

		if (!userPermsMsg.isEmpty()) {
			ctx.replyError(MessageFormat.format("You don't have permission: {0}", userPermsMsg.toString()));
			return;
		}

		execute(ctx);
	}

	public CommandData buildCommandData() {
		SlashCommandData data = Commands.slash(name, description);

		if (!getUserPermissions().isEmpty()) {
			data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(getUserPermissions()));
		}

		if (!getOptions().isEmpty()) {
			data.addOptions(getOptions());
		}

		if (getChildren().length != 0) {
			List<SubcommandGroupData> groupData = new ArrayList<>();

			for (Command child : getChildren()) {
				SubcommandData subcommandData = new SubcommandData(child.getName(), child.getDescription());

				if (!child.getOptions().isEmpty()) {
					subcommandData.addOptions(child.getOptions());
				}

				if (child.getSubcommandGroup() != null) {
					groupData.add(child.getSubcommandGroup().addSubcommands(subcommandData));
				} else {
					data.addSubcommands(subcommandData);
				}
			}

			if (!groupData.isEmpty()) {
				data.addSubcommandGroups(groupData);
			}
		}

		return data;
	}

	private String botPerms(String perms) {
		return MessageFormat.format("I don't have permission: {0}", perms);
	}
}
