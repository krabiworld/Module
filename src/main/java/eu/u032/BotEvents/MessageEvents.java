package eu.u032.BotEvents;

import eu.u032.Config;
import eu.u032.MessageCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class MessageEvents extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
        MessageCache.addMessage(event.getMessage());
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        if (MessageCache.getMessage(event.getMessageIdLong()) == null) return;

        Message msg = MessageCache.getMessage(event.getMessageIdLong());
        User author = Objects.requireNonNull(msg).getAuthor();

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
                .setColor(Color.decode("#e94b3e"))
                .setDescription(String.format("Message from %s deleted in <#%s>", author.getAsMention(), msg.getChannel().getId()))
                .setFooter("User ID: " + author.getId())
                .setTimestamp(new Date().toInstant());

        if (!msg.getAttachments().isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (Message.Attachment attachment : msg.getAttachments()) {
                message.append(
                        String.format("File: [%s](%s)", attachment.getFileName(), attachment.getUrl())
                ).append("\n");
            }
            message.append(msg.getContentDisplay());
            embed.addField("Message content", message.toString(), false);
        } else {
            embed.addField("Message content", msg.getContentDisplay(), false);
        }

        Objects.requireNonNull(event.getJDA().getTextChannelById(Config.getString("LOGS_CHANNEL")))
                .sendMessageEmbeds(embed.build())
                .queue();
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        if (MessageCache.getMessage(event.getMessageIdLong()) == null) return;

        Message before = MessageCache.getMessage(event.getMessageIdLong());

        if (before == null) return;

        Message after = event.getMessage();
        User author = after.getAuthor();

        MessageCache.addMessage(after);

        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(author.getAsTag(), author.getEffectiveAvatarUrl(), author.getEffectiveAvatarUrl())
                .setColor(Color.decode("#f7d724"))
                .setDescription(String.format("Message from %s edited in <#%s>\n[Jump to Message](%s)", author.getAsMention(), after.getChannel().getId(), after.getJumpUrl()))
                .addField("Before", before.getContentDisplay(), false)
                .addField("After", after.getContentDisplay(), false)
                .setFooter("User ID: " + author.getId())
                .setTimestamp(new Date().toInstant());
        Objects.requireNonNull(event.getJDA().getTextChannelById(Config.getString("LOGS_CHANNEL")))
                .sendMessageEmbeds(embed.build())
                .queue();

    }

}
