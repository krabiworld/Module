/*
 * This file is part of Module.

 *  Module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with Module. If not, see <https://www.gnu.org/licenses/>.
 */

package org.module.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.module.constants.Constants;
import org.module.service.MessageService;
import org.module.service.OwnerService;
import org.module.util.ArgsUtil;
import org.module.util.PropertyUtil;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;

import static org.reflections.scanners.Scanners.SubTypes;

@Component
public class EventCommand extends Command {
	private final MessageService messageService;
	private final OwnerService ownerService;
	private final ApplicationContext ctx;

	@Autowired
	public EventCommand(MessageService messageService, OwnerService ownerService, ApplicationContext ctx) {
		this.messageService = messageService;
		this.ownerService = ownerService;
		this.ctx = ctx;
		this.name = PropertyUtil.getProperty("command.event.name");
		this.help = PropertyUtil.getProperty("command.event.help");
		this.arguments = PropertyUtil.getProperty("command.event.arguments");
		this.category = Constants.OWNER;
		this.hidden = true;
	}

	@Override
	protected void execute(CommandEvent event) {
		if (!ownerService.isOwner(event.getMember())) return;

		if (event.getArgs().isEmpty()) {
			messageService.sendHelp(event, this);
			return;
		}

		String[] args = ArgsUtil.split(event.getArgs());
		if (args.length < 1) {
			messageService.sendHelp(event, this);
			return;
		}

		if (args[0].startsWith("add")) {
			if (args.length < 2) {
				messageService.sendHelp(event, this);
				return;
			}
			try {
				event.getJDA().addEventListener(ctx.getBean(Class.forName("org.module.events." + args[1])));
				event.reactSuccess();
			} catch (Exception e) {
				e.printStackTrace();
				messageService.sendError(event, "command.event.error.class.not.found");
			}
		} else if (args[0].startsWith("remove")) {
			if (args.length < 2) {
				messageService.sendHelp(event, this);
				return;
			}
			try {
				event.getJDA().removeEventListener(ctx.getBean(Class.forName("org.module.events." + args[1])));
				event.reactSuccess();
			} catch (Exception e) {
				e.printStackTrace();
				messageService.sendError(event, "command.event.error.class.not.found");
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
				.setColor(Constants.COLOR)
				.setDescription("```" + list + "```");
			event.reply(embed.build());
		} else {
			messageService.sendHelp(event, this);
		}
	}
}
