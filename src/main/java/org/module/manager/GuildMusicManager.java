package org.module.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.module.music.AudioPlayerSendHandler;
import org.module.music.TrackScheduler;

public class GuildMusicManager {
	public final AudioPlayer player;

	public final TrackScheduler scheduler;

	public GuildMusicManager(AudioPlayerManager manager) {
		player = manager.createPlayer();
		scheduler = new TrackScheduler(player);
		player.addListener(scheduler);
	}

	public AudioPlayerSendHandler getSendHandler() {
		return new AudioPlayerSendHandler(player);
	}
}
