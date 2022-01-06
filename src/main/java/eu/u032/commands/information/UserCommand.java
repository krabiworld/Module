package eu.u032.commands.information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;
import java.util.List;

public class UserCommand extends Command {
    public UserCommand() {
        this.name = "user";
        this.help = "User information";
        this.arguments = "[@Member | ID]";
        this.category = new Category("Information");
    }

    @Override
    protected void execute(CommandEvent event) {
        String memberId = Utils.getId(event.getArgs(), Utils.MEMBER);
        Member member = memberId.isEmpty() ? null : event.getGuild().getMemberById(memberId);

        if (event.getArgs().isEmpty()) {
            member = event.getMember();
        }
        if (member == null) {
            event.replyError("Member not found.");
            return;
        }

        User.Profile profile = member.getUser().retrieveProfile().complete();

        String description = String.format("**Username:** %s\n%s%s%s%s",
                member.getUser().getAsTag(),
                getStatus(member.getOnlineStatus()),
                getActivities(member.getActivities()),
                getJoinedAt(member.getTimeJoined()),
                getRegisteredAt(member.getTimeCreated()));

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Information about " + member.getUser().getName(),
                        null, member.getEffectiveAvatarUrl())
                .setColor(member.getColor())
                .setDescription(description)
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setFooter("ID: " + member.getId());

        if (profile.getBannerUrl() != null) embed.setImage(profile.getBannerUrl() + "?size=512");

        event.reply(embed.build());
    }

    private String getStatus(OnlineStatus onlineStatus) {
         String status = switch (onlineStatus) {
            case ONLINE -> "<:online:925113750598598736>Online";
            case IDLE -> "<:idle:925113750254682133>Idle";
            case DO_NOT_DISTURB -> "<:dnd:925113750896398406>Do Not Disturb";
            default -> "<:offline:925113750581817354>Offline";
         };
        return "**Status:**" + status + "\n";
    }

    private String getActivities(List<Activity> activityList) {
        StringBuilder activities = new StringBuilder();
        for (Activity activity : activityList) {
            if (activity.getType() == Activity.ActivityType.CUSTOM_STATUS)
                activities.append("**Custom Status:** ")
                        .append(activity.getEmoji() == null ? "" : activity.getEmoji().getAsMention() + " ");
            if (activity.getType() == Activity.ActivityType.DEFAULT)
                activities.append("**Playing:** ");
            if (activity.getType() == Activity.ActivityType.COMPETING)
                activities.append("**Competing in:** ");
            if (activity.getType() == Activity.ActivityType.LISTENING)
                activities.append("**Listening to:** ");
            if (activity.getType() == Activity.ActivityType.STREAMING)
                activities.append("**Streaming:** ");
            if (activity.getType() == Activity.ActivityType.WATCHING)
                activities.append("**Watching:** ");
            activities.append(activity.getName()).append("\n");
        }
        return activities.toString();
    }

    private String getJoinedAt(OffsetDateTime time) {
        return String.format("**Joined at:** <t:%s:D> (<t:%s:R>)\n",
                time.toEpochSecond(), time.toEpochSecond());
    }

    private String getRegisteredAt(OffsetDateTime time) {
        return String.format("**Registered at:** <t:%s:D> (<t:%s:R>)",
                time.toEpochSecond(), time.toEpochSecond());
    }
}
