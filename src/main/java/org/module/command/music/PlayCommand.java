package org.module.command.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.module.manager.MusicManager;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayCommand extends Command {
	private final MusicManager musicManager;

	@Autowired
	public PlayCommand(MusicManager musicManager) {
		this.name = "play";
		this.ownerCommand = true;
		this.hidden = true;
		this.options.add(
			new OptionData(OptionType.STRING, "track", "URL to track", true)
		);
		this.musicManager = musicManager;
	}

	@Override
	protected void execute(CommandContext ctx) {
		String track = ctx.getOptionAsString("track");

		GuildVoiceState voiceState = ctx.getMember().getVoiceState();
		if (voiceState == null) {
			ctx.replyError("Please join to voice channel");
			return;
		}

		AudioChannelUnion union = voiceState.getChannel();
		if (union == null) {
			ctx.replyError("Please join to voice channel");
			return;
		}

		VoiceChannel voiceChannel = union.asVoiceChannel();

		musicManager.loadAndPlay(ctx, voiceChannel, track);
	}
}
