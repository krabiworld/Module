package org.module.command.music;

import org.module.manager.MusicManager;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PauseCommand extends Command {
	private final MusicManager musicManager;

	@Autowired
	public PauseCommand(MusicManager musicManager) {
		this.name = "pause";
		this.description = "Pause the current song";
		this.category = Category.MUSIC;
		this.musicManager = musicManager;
	}

	@Override
	protected void execute(CommandContext ctx) {
		musicManager.pause(ctx.getGuild(), true);
		ctx.replySuccess("Song paused.");
	}
}
