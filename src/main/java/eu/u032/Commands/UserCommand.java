package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.Date;

public class UserCommand extends Command {

    public UserCommand() {
        this.name = "user";
        this.help = "Information about user";
        this.category = new Category("Information");
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            Member member =
                    event.getArgs().isEmpty() ? event.getMember() : event.getGuild().retrieveMemberById(event.getArgs()).complete();

            String value = String.format("**Username:** %s\n**Color:** #%06x\n**Joined at:** <t:%s>\n**Registered at:** <t:%s>",
                    member.getUser().getAsTag(),
                    member.getColor().getRGB() & 0xFFFFFF,
                    member.getTimeJoined().toEpochSecond(),
                    member.getTimeCreated().toEpochSecond()
            );

            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Information about " + member.getUser().getName(), member.getEffectiveAvatarUrl())
                    .setColor(Color.decode("#6196d5"))
                    .setDescription(value)
                    .setThumbnail(member.getEffectiveAvatarUrl())
                    .setFooter("ID: " + member.getId())
                    .setTimestamp(new Date().toInstant());
            event.reply(embed.build());
        } catch (Exception e) {
            event.replyError(e.getMessage());
        }
    }
}
