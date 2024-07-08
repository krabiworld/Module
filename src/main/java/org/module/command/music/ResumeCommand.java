package org.module.command.music;

import org.module.manager.MusicManager;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResumeCommand extends Command {
	private final MusicManager musicManager;

	@Autowired
	public ResumeCommand(MusicManager musicManager) {
		this.name = "resume";
		this.description = "Resume the current song";
		this.category = Category.MUSIC;
		this.musicManager = musicManager;
	}

	@Override
	protected void execute(CommandContext ctx) {
		musicManager.pause(ctx.getGuild(), false);
		ctx.replySuccess("Song resumed.");
	}
}
