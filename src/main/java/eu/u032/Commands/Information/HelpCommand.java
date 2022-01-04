package eu.u032.Commands.Information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static eu.u032.Utils.getColor;
import static eu.u032.Utils.getCopyright;

public class HelpCommand extends Command {
    public HelpCommand() {
        this.name = "help";
        this.help = "This message";
        this.arguments = "[command/category]";
        this.category = new Category("Information");
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();
        String prefix = event.getClient().getPrefix();
        EmbedBuilder embed = new EmbedBuilder().setColor(getColor()).setFooter(getCopyright());

        List<String> categoriesList = new LinkedList<>();

        for (Command command : event.getClient().getCommands()) {
            if (command.getCategory() == null) continue;
            categoriesList.add(command.getCategory().getName());
        }

        Set<String> categories = new LinkedHashSet<>(categoriesList);

        if (args.isEmpty()) {
            StringBuilder commands = new StringBuilder();
            embed.setTitle("Available commands:");

            for (String category : categories) {
                for (Command command : event.getClient().getCommands()) {
                    if (command.isHidden()) continue;
                    if (command.getCategory().getName().equals(category)) {
                        commands.append("`")
                                .append(prefix)
                                .append(command.getName())
                                .append("` ");
                    }
                }
                embed.addField(category + " (" + prefix + "help " + category + ")", commands.toString(), false);
                commands = new StringBuilder();
            }

            event.reply(embed.build());
            return;
        } else {
            for (String category : categories) {
                if (category.toLowerCase().startsWith(args.toLowerCase())) {
                    for (Command cmd : event.getClient().getCommands()) {
                        if (cmd.isHidden()) continue;
                        if (cmd.getCategory().getName().equals(category))
                            embed.addField(prefix + cmd.getName(), cmd.getHelp(), false);
                    }
                    embed.setTitle("Commands of category " + category);
                    event.reply(embed.build());
                    return;
                }
            }
            for (Command cmd : event.getClient().getCommands()) {
                if (cmd.getName().toLowerCase().startsWith(args.toLowerCase()) && !cmd.isHidden()) {
                    embed.setTitle("Information of command " + cmd.getName());
                    embed.setDescription(
                            "`" + prefix + cmd.getName() + (cmd.getArguments() == null ? "" : " " + cmd.getArguments()) + "`\n" + cmd.getHelp()
                    );
                    event.reply(embed.build());
                    return;
                }
            }
        }

        event.replyError("Command or category **" + args + "** not found.");
    }
}
