package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils.Args;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public class AvatarCommand extends Command {

    public AvatarCommand() {
        this.name = "avatar";
    }

    @Override
    protected void execute(CommandEvent event) {
        Member member = Args.getMemberFromArgs(event) != null ? Args.getMemberFromArgs(event) : event.getMember();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Avatar of " + member.getUser().getName())
                .setColor(Color.decode("#6196d5"))
                .setImage(member.getEffectiveAvatarUrl() + "?size=512");
        event.reply(embed.build());
    }

}
