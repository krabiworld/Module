package org.module.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.module.music.GuildMusicManager;
import org.module.structure.CommandContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MusicManager {
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;

	public MusicManager() {
		this.musicManagers = new HashMap<>();

		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = guild.getIdLong();
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}

	public void loadAndPlay(CommandContext ctx, VoiceChannel voiceChannel, String trackUrl) {
		GuildMusicManager musicManager = getGuildAudioPlayer(ctx.getGuild());

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				ctx.reply("Adding to queue " + track.getInfo().title);

				play(ctx.getGuild().getAudioManager(), voiceChannel, musicManager, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();

				if (firstTrack == null) {
					firstTrack = playlist.getTracks().getFirst();
				}

				ctx.reply("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

				play(ctx.getGuild().getAudioManager(), voiceChannel, musicManager, firstTrack);
			}

			@Override
			public void noMatches() {
				ctx.reply("Nothing found by " + trackUrl);
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				ctx.reply("Could not play: " + exception.getMessage());
			}
		});
	}

	public void skip(Guild guild) {
		GuildMusicManager musicManager = getGuildAudioPlayer(guild);
		musicManager.scheduler.nextTrack();
	}

	public void pause(Guild guild, boolean pause) {
		GuildMusicManager musicManager = getGuildAudioPlayer(guild);
		musicManager.player.setPaused(pause);
	}

	private void play(AudioManager manager, VoiceChannel voiceChannel, GuildMusicManager musicManager, AudioTrack track) {
		if (!manager.isConnected()) {
			manager.openAudioConnection(voiceChannel);
		}

		musicManager.scheduler.queue(track);
	}
}
