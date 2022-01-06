package eu.u032.commands.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.LinkedList;
import java.util.List;

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
        List<Command> commands = event.getClient().getCommands();
        EmbedBuilder embed = new EmbedBuilder().setColor(getColor()).setFooter(getCopyright());
        List<String> categories = new LinkedList<>();

        categoriesLoop:
        for (Command cmd : commands) {
            if (cmd.getCategory() == null) continue;
            for (String category : categories) {
                // if command already exists in "categories" - continue
                if (category.equals(cmd.getCategory().getName())) continue categoriesLoop;
            }
            categories.add(cmd.getCategory().getName());
        }

        // if "args" is empty - get all commands
        if (args.isEmpty()) {
            StringBuilder commandsBuilder = new StringBuilder();
            embed.setTitle("Available commands:");

            for (String category : categories) {
                for (Command cmd : commands) {
                    if (cmd.isHidden()) continue;
                    if (cmd.getCategory().getName().equals(category)) {
                        commandsBuilder.append("`")
                                .append(prefix)
                                .append(cmd.getName())
                                .append("` ");
                    }
                }
                embed.addField(category + " (" + prefix + "help " + category + ")",
                        commandsBuilder.toString(),
                        false);
                commandsBuilder = new StringBuilder();
            }

            event.reply(embed.build());
        } else {
            for (String category : categories) {
                // if match found with name of category
                if (category.toLowerCase().startsWith(args.toLowerCase())) {
                    for (Command cmd : commands) {
                        if (cmd.isHidden()) continue;
                        if (cmd.getCategory().getName().equals(category))
                            embed.addField(prefix + cmd.getName(), cmd.getHelp(), false);
                    }
                    embed.setTitle("Commands of category " + category);
                    event.reply(embed.build());
                    return;
                }
            }
            for (Command cmd : commands) {
                // if match found with name of command
                if (cmd.getName().toLowerCase().startsWith(args.toLowerCase()) && !cmd.isHidden()) {
                    embed.setTitle("Information of command " + cmd.getName());
                    embed.setDescription("`" + prefix + cmd.getName() +
                            (cmd.getArguments() == null ? "" : " " + cmd.getArguments()) + "`\n" +
                            cmd.getHelp());
                    event.reply(embed.build());
                    return;
                }
            }

            event.replyError("Command or category **" + args + "** not found.");
        }
    }
}
