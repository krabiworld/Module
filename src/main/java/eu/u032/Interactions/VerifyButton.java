package eu.u032.Interactions;

import eu.u032.Utils.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;

public class VerifyButton extends ListenerAdapter {
    public void onButtonClick(ButtonClickEvent event) {
        Role verifiedRole = event.getGuild().getRoleById(Config.getString("VERIFIED_ROLE"));
        Role awaitingRole = event.getGuild().getRoleById(Config.getString("AWAITING_ROLE"));
        Member member = event.getMember();

        if (event.getComponentId().equals("send_verify_button")) {

            if(!member.getRoles().isEmpty()) {
                return;
            }

            System.out.println(verifiedRole.getName());

            event.reply("Successfully!")
                .setEphemeral(true)
                .queue();

            event.getGuild().addRoleToMember(member, awaitingRole).queue();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(member.getEffectiveName(), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl())
                .setDescription("Verification request was sent");

        event.getJDA().getTextChannelById(Config.getString("ADMINS_CHANNEL"))
            .sendMessageEmbeds(embed.build())
            .setActionRow(
                    Button.success(String.format("accept_member_%s", member.getId()), "Accept"),
                    Button.danger(String.format("deny_member_%s", member.getId()), "Deny")
            )
        .queue();

        }

        if (event.getComponentId().startsWith("accept_member_")) {

            String awaitingMemberId = event.getComponentId().split("_")[2];

            event.getGuild().addRoleToMember(awaitingMemberId, verifiedRole).queue();
            event.getGuild().removeRoleFromMember(awaitingMemberId, awaitingRole).queue();

            event.editMessage("Accepted")
                .setActionRow(
                    Button.success(String.format("accept_member_%s", member.getId()), "Accept")
                            .withDisabled(true),
                    Button.danger(String.format("deny_member_%s", member.getId()), "Deny")
                            .withDisabled(true)
                ).queue();

            event.reply("Successfully!")
                    .setEphemeral(true)
                    .queue();
        } else if (event.getComponentId().startsWith("deny_member_")) {
            String awaitingMemberId = event.getComponentId().split("_")[2];

            event.editMessage("Denied")
                    .setActionRow(
                            Button.success(String.format("accept_member_%s", member.getId()), "Accept")
                                    .withDisabled(true),
                            Button.danger(String.format("deny_member_%s", member.getId()), "Deny")
                                    .withDisabled(true)
                    ).queue();
            event.getGuild().kick(awaitingMemberId).queue();
            event.reply("Successfully!")
                    .setEphemeral(true)
                    .queue();
        }

    }
}
