package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

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
        User.Profile profile = member.getUser().retrieveProfile().complete();
        StringBuilder activities = new StringBuilder();

        for (Activity activity : member.getActivities()) {
            if (activity.getType() == Activity.ActivityType.CUSTOM_STATUS) {
                activities.append("**Custom Status:** ")
                        .append(activity.getEmoji() == null ? "" : activity.getEmoji().getAsMention())
                        .append(" ").append(activity.getName());
            }
            if (activity.getType() == Activity.ActivityType.DEFAULT)
                activities.append("**Playing:** ").append(activity.getName());
            if (activity.getType() == Activity.ActivityType.COMPETING)
                activities.append("**Competing:** ").append(activity.getName());
            if (activity.getType() == Activity.ActivityType.LISTENING)
                activities.append("**Listening:** ").append(activity.getName());
            if (activity.getType() == Activity.ActivityType.STREAMING)
                activities.append("**Streaming:** ").append(activity.getName());
            if (activity.getType() == Activity.ActivityType.WATCHING)
                activities.append("**Watching:** ").append(activity.getName());
            activities.append("\n");
        }

        status = switch (member.getOnlineStatus()) {
            case ONLINE -> "<:online:925113750598598736>Online";
            case IDLE -> "<:idle:925113750254682133>Idle";
            case DO_NOT_DISTURB -> "<:dnd:925113750896398406>Do Not Disturb";
            default -> "<:offline:925113750581817354>Offline";
        };

        String description = String.format("**Username:** %s\n**Status:** %s\n%s**Joined at:** <t:%s>\n**Registered at:** <t:%s>",
                member.getUser().getAsTag(),
                status, activities,
                member.getTimeJoined().toEpochSecond(),
                member.getTimeCreated().toEpochSecond()
        );

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Information about " + member.getUser().getName())
                .setColor(member.getColorRaw())
                .setDescription(description)
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setFooter("ID: " + member.getId());

        if (profile.getBannerUrl() != null) embed.setImage(profile.getBannerUrl() + "?size=512");

        event.reply(embed.build());
    }

}
