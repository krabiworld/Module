package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class UserCommand extends Command {

    public UserCommand() {
        this.name = "user";
        this.help = "User information";
        this.arguments = "[@Member | ID]";
        this.category = new Category("Information");
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member = Utils.getMemberFromArgs(event) != null ? Utils.getMemberFromArgs(event) : event.getMember();
        String status;
        StringBuilder device = new StringBuilder();
        User.Profile profile = member.getUser().retrieveProfile().complete();

        status = switch (member.getOnlineStatus()) {
            case ONLINE -> "<:online:925113750598598736>Online";
            case IDLE -> "<:idle:925113750254682133>Idle";
            case DO_NOT_DISTURB -> "<:dnd:925113750896398406>Do Not Disturb";
            default -> "<:offline:925113750581817354>Offline";
        };

        for (ClientType type : member.getActiveClients()) {
            if (member.getUser().isBot()) {
                device.append(":cloud: Server");
                break;
            }
            switch (type) {
                case DESKTOP -> device.append(":desktop: Desktop");
                case MOBILE -> device.append(":mobile_phone: Mobile");
                case WEB -> device.append(":spider_web: Web");
                case UNKNOWN -> device.append("Unknown");
            }
            device.append(" ");
        }

        String description = String.format("**Username:** %s\n**Status:** %s\n**Device:** %s\n**Role color:** #%s\n**Joined at:** <t:%s>\n**Registered at:** <t:%s>",
                member.getUser().getAsTag(),
                status, device,
                Integer.toHexString(Objects.requireNonNull(member.getColor()).getRGB()).substring(2),
                member.getTimeJoined().toEpochSecond(),
                member.getTimeCreated().toEpochSecond()
        );

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Information about " + member.getUser().getName())
                .setColor(member.getColorRaw())
                .setDescription(description)
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setFooter("User ID: " + member.getId());

        if (profile.getBannerUrl() != null) embed.setImage(profile.getBannerUrl() + "?size=512");

        event.reply(embed.build());
    }

}
