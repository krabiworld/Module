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

public abstract class AbstractCommand {
	private Command annotation;

	public Command getAnnotation() {
		if (annotation == null) {
			annotation = getClass().getDeclaredAnnotation(Command.class);
		}
		return annotation;
	}

	protected AbstractCommand[] children = new AbstractCommand[0];

	protected abstract void execute(CommandContext ctx);

	public void run(CommandContext ctx) {
		Command cmd = getAnnotation();

		String args = ctx.getArgs();

		if (!args.isEmpty()) {
			for (AbstractCommand command : children) {
				String cmdName = ctx.get(command.getAnnotation().name());
				if (args.startsWith(cmdName)) {
					ctx.setArgs(args.substring(cmdName.length()).trim());
					command.run(ctx);
					return;
				}
			}
		}

		if (cmd.moderator() && !ctx.isModerator()) {
			ctx.sendError("error.not.mod");
			return;
		}

		Member selfMember = ctx.getSelfMember();

		if (!selfMember.hasPermission(Permission.MESSAGE_SEND)) {
			ctx.getUser()
				.openPrivateChannel()
				.flatMap(channel -> channel.sendMessage(
					ctx.get("error.permission.bot", Permission.MESSAGE_SEND.getName())
				))
				.queue();
		}

		if (!selfMember.hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
			ctx.send(ctx.get("error.permission.bot", Permission.MESSAGE_EMBED_LINKS.getName()));
			return;
		}

		StringBuilder botPermissions = new StringBuilder();

		for (Permission permission : cmd.botPermissions()) {
			if (permission.isChannel()) {
				if (!selfMember.hasPermission(ctx.getGuildChannel(), permission)) {
					botPermissions.append("`").append(permission.getName()).append("` ");
				}
			} else {
				if (!selfMember.hasPermission(permission)) {
					botPermissions.append("`").append(permission.getName()).append("` ");
				}
			}
		}

		if (!botPermissions.isEmpty()) {
			ctx.sendError("error.permission.bot", botPermissions.toString());
			return;
		}

		StringBuilder userPermissions = new StringBuilder();

		for (Permission permission : cmd.userPermissions()) {
			if (permission.isChannel()) {
				if (!ctx.getMember().hasPermission(ctx.getGuildChannel(), permission)) {
					userPermissions.append("`").append(permission.getName()).append("` ");
				}
			} else {
				if (!ctx.getMember().hasPermission(permission)) {
					userPermissions.append("`").append(permission.getName()).append("` ");
				}
			}
		}

		if (!userPermissions.isEmpty()) {
			ctx.sendError("error.permission.user", userPermissions.toString());
			return;
		}

		execute(ctx);
	}
}
