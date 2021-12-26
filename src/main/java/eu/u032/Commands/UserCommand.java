package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class UserCommand extends Command {

    public UserCommand() {
        this.name = "user";
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member = Utils.getMemberFromArgs(event) != null ? Utils.getMemberFromArgs(event) : event.getMember();
        String status = ":black_circle: Offline";
        String device = "Unknown";

        status = switch (member.getOnlineStatus()) {
            case ONLINE -> ":green_circle: Online";
            case IDLE -> ":yellow_circle: Idle";
            case DO_NOT_DISTURB -> ":red_circle: Do Not Disturb";
            default -> status;
        };

        for (ClientType type : member.getActiveClients()) {
            if (member.getUser().isBot()) {
                device = ":cloud: Server";
                break;
            }
            device = switch (type) {
                case DESKTOP -> ":desktop: Desktop";
                case MOBILE -> ":mobile_phone: Mobile";
                case WEB -> ":spider_web: Web";
                default -> device;
            };
        }

        String description = String.format("**Username:** %s\n**Status:** %s\n**Device:** %s\n**Color:** #%s\n**Joined at:** <t:%s>\n**Registered at:** <t:%s>",
                member.getUser().getAsTag(),
                status, device,
                Integer.toHexString(Objects.requireNonNull(member.getColor()).getRGB()).substring(2),
                member.getTimeJoined().toEpochSecond(),
                member.getTimeCreated().toEpochSecond()
        );

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Information about " + member.getUser().getName())
                .setColor(member.getColor())
                .setDescription(description)
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setFooter("User ID: " + member.getId());
        event.reply(embed.build());
    }

}
