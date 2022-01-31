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

package org.module.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.Constants;
import org.module.service.OwnerService;
import org.module.service.impl.OwnerServiceImpl;
import org.module.util.ArgsUtil;
import org.module.util.MessageUtil;
import org.module.util.PropertyUtil;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;
import java.util.TreeSet;

import static org.reflections.scanners.Scanners.SubTypes;

public class EventCommand extends Command {
	private final OwnerService ownerService = new OwnerServiceImpl();

	public EventCommand() {
		this.name = PropertyUtil.getProperty("command.event.name");
		this.help = PropertyUtil.getProperty("command.event.help");
		this.arguments = PropertyUtil.getProperty("command.event.arguments");
		this.category = Constants.OWNER;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (ownerService.isNotOwner(event.getMember())) return;

		if (event.getArgs().isEmpty()) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());
		if (args.length < 1) {
			MessageUtil.sendHelp(event, this);
			return;
		}

		if (args[0].startsWith("add")) {
			if (args.length < 2) {
				MessageUtil.sendHelp(event, this);
				return;
			}
			try {
				event.getJDA().addEventListener(
					Class.forName("org.module.events." + args[1]).getDeclaredConstructor().newInstance());
				event.reactSuccess();
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.sendError(event, "command.event.error.class.not.found");
			}
		} else if (args[0].startsWith("remove")) {
			if (args.length < 2) {
				MessageUtil.sendHelp(event, this);
				return;
			}
			try {
				event.getJDA().removeEventListener(
					Class.forName("org.module.events." + args[1]).getDeclaredConstructor().newInstance());
				event.reactSuccess();
			} catch (Exception e) {
				e.printStackTrace();
				MessageUtil.sendError(event, "command.event.error.class.not.found");
			}
		} else if (args[0].startsWith("list")) {
			Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.module.events"));
			Set<Class<?>> subTypes = reflections.get(SubTypes.of(ListenerAdapter.class).asClass());

			TreeSet<String> treeSet = new TreeSet<>();
			for (Class<?> clazz : subTypes) {
				if (!clazz.getName().startsWith("org.module.events.")) continue;
				treeSet.add(clazz.getName().replace("org.module.events.", ""));
			}

			StringBuilder list = new StringBuilder();
			for (String clazz : treeSet) {
				list.append(clazz).append("\n");
			}

			EmbedBuilder embed = new EmbedBuilder()
				.setAuthor("List of events")
				.setColor(Constants.DEFAULT)
				.setDescription("```" + list + "```");
			event.reply(embed.build());
		} else {
			MessageUtil.sendHelp(event, this);
		}
	}
}
