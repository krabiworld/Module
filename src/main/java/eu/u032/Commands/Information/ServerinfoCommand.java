package eu.u032.Commands.Information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class ServerinfoCommand extends Command {
    public ServerinfoCommand() {
        this.name = "serverinfo";
        this.help = "Server information";
        this.category = new Category("Information");
    }

    @Override
    protected void execute(CommandEvent event) {
        Guild guild = event.getGuild();
        int online = 0, dnd = 0, idle = 0, offline = 0;
        String vName = guild.getVerificationLevel().name();
        String verificationLevel = vName.charAt(0) + vName.substring(1).toLowerCase().replace("_", " ");
        long createdAt = guild.getTimeCreated().toEpochSecond();

        int botCount = guild.getMembers().stream().filter(m -> m.getUser().isBot()).toList().size();
        int memberCount = guild.getMemberCount();
        int channelCount = guild.getChannels().size() - guild.getCategories().size();
        int voiceCount = guild.getVoiceChannels().size();
        int stageCount = guild.getStageChannels().size();
        int storeCount = guild.getStoreChannels().size();

        for (Member member : guild.getMembers()) {
            switch (member.getOnlineStatus()) {
                case ONLINE -> online++;
                case IDLE -> idle++;
                case DO_NOT_DISTURB -> dnd++;
                case OFFLINE -> offline++;
            }
        }

        String members = "<:members:926844061707546654> Members: **" + (memberCount - botCount) + "**\n" +
                "<:bots:926844061703364648> Bots: **" + botCount + "**";
        String channels = "<:text:926844062198276136> Text: **" + guild.getTextChannels().size() + "**\n" +
                (voiceCount == 0 ? "" : "<:voice:926844062504464444> Voice: **" + voiceCount + "**\n") +
                (stageCount == 0 ? "" : "<:stage:926844062252818522> Stage: **" + stageCount + "**\n") +
                (storeCount == 0 ? "" : "<:store:926844062160519178> Store: **" + stageCount + "**");
        String byStatus = online == 0 ? "" : "<:online:925113750598598736>Online: **" + online + "**\n" +
                (idle == 0 ? "" : "<:idle:925113750254682133>Idle: **" + idle + "**\n") +
                (dnd == 0 ? "" : "<:dnd:925113750896398406>Do Not Disturb: **" + dnd + "**\n") +
                (offline == 0 ? "" : "<:offline:925113750581817354>Offline: **" + offline + "**");

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Information about " + guild.getName())
                .setColor(Utils.getColor())
                .setThumbnail(guild.getIconUrl())
                .addField("Members (" + memberCount + ")", members, true)
                .addField("Channels (" + channelCount + ")", channels, true)
                .addField("By Status", byStatus, true)
                .addField("Owner", Objects.requireNonNull(guild.getOwner()).getAsMention(), true)
                .addField("Verification Level", verificationLevel, true)
                .addField("Created at", "<t:" + createdAt + ":D> (<t:" + createdAt + ":R>)", true)
                .setImage(guild.getBannerUrl())
                .setFooter("ID: " + guild.getId());
        event.reply(embed.build());
    }
}
