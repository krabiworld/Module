package eu.u032.commands.utilities;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import eu.u032.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class AvatarCommand extends Command {
    public AvatarCommand() {
        this.name = "avatar";
        this.help = "Show avatar of member";
        this.arguments = "[@Member | ID]";
        this.category = new Category("Utilities");
    }

    @Override
    protected void execute(final CommandEvent event) {
        final String memberId = Utils.getId(event.getArgs(), Utils.MEMBER);
        Member member = memberId.isEmpty() ? null : event.getGuild().getMemberById(memberId);

        if (event.getArgs().isEmpty()) {
            member = event.getMember();
        }
        if (member == null) {
            event.replyError("Member not found.");
            return;
        }

        final EmbedBuilder embed = new EmbedBuilder()
                .setAuthor("Avatar of " + member.getUser().getName())
                .setColor(member.getColor())
                .setImage(member.getEffectiveAvatarUrl() + "?size=512");
        event.reply(embed.build());
    }
}
