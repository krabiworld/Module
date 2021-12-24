package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils.Args;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public class UserCommand extends Command {

    public UserCommand() {
        this.name = "user";
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            Member member = Args.getMemberFromArgs(event) != null ? Args.getMemberFromArgs(event) : event.getMember();
            String color = "#" + Integer.toHexString(member.getColor().getRGB()).substring(2);
            String device = "Unknown";

            for (ClientType type : member.getActiveClients()) {
                if (member.getUser().isBot()) {
                    device = ":cloud: Server";
                    break;
                }
                switch (type) {
                    case DESKTOP -> device = ":desktop: Desktop";
                    case WEB -> device = ":spider_web: Web";
                    case MOBILE -> device = ":mobile_phone: Mobile";
                }
            }

            String description = String.format("**Username:** %s\n**Device:** %s\n**Color:** %s\n**Joined at:** <t:%s>\n**Registered at:** <t:%s>",
                    member.getUser().getAsTag(),
                    device, color,
                    member.getTimeJoined().toEpochSecond(),
                    member.getTimeCreated().toEpochSecond()
            );

            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Information about " + member.getUser().getName())
                    .setColor(Color.decode(color))
                    .setDescription(description)
                    .setThumbnail(member.getEffectiveAvatarUrl())
                    .setFooter("ID: " + member.getId());
            event.reply(embed.build());
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
