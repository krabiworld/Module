package eu.u032.Commands.Information;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Config;
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
        int bots = guild.getMembers().stream().filter(m -> m.getUser().isBot()).toList().size();
        String verLevel = guild.getVerificationLevel().name();
        long createdAt = guild.getTimeCreated().toEpochSecond();

        for (Member member : guild.getMembers()) {
            switch (member.getOnlineStatus()) {
                case ONLINE -> online++;
                case IDLE -> idle++;
                case DO_NOT_DISTURB -> dnd++;
                case OFFLINE -> offline++;
            }
        }

        String members = String.format("""
                        <:members:925771713197768714> Members: **%s**
                        <:bots:926447286907719751> Bots: **%s**""",
                guild.getMemberCount() - bots, bots
        );
        String channels = String.format("""
                        <:text:925763237423763517> Text: **%s**
                        <:voice:925763238103228416> Voice: **%s**
                        <:stage:925763237201453078> Stage: **%s**
                        <:store:925763237843173396> Store: **%s**""",
                guild.getTextChannels().size(),
                guild.getVoiceChannels().size(),
                guild.getStageChannels().size(),
                guild.getStoreChannels().size()
        );
        String byStatus = String.format("""
                        <:online:925113750598598736>Online: **%s**
                        <:idle:925113750254682133>Idle: **%s**
                        <:dnd:925113750896398406>Do Not Disturb: **%s**
                        <:offline:925113750581817354>Offline: **%s**""",
                online, idle, dnd, offline
        );

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(guild.getName())
                .setColor(Config.getColor())
                .setThumbnail(guild.getIconUrl())
                .addField("Members (" + guild.getMemberCount() + ")", members, true)
                .addField("Channels (" + (guild.getChannels().size() - guild.getCategories().size()) + ")", channels, true)
                .addField("By Status", byStatus, true)
                .addField("Owner", Objects.requireNonNull(guild.getOwner()).getAsMention(), true)
                .addField("Verification Level", verLevel.charAt(0) + verLevel.substring(1).toLowerCase(), true)
                .addField("Created at", "<t:" + createdAt + ":D> (<t:" + createdAt + ":R>)", true)
                .setImage(guild.getBannerUrl())
                .setFooter("ID: " + guild.getId());
        event.reply(embed.build());
    }

}
