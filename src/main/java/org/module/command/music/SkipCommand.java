package org.module.command.music;

import org.module.manager.MusicManager;
import org.module.structure.Category;
import org.module.structure.Command;
import org.module.structure.CommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkipCommand extends Command {
	private final MusicManager musicManager;

	@Autowired
	public SkipCommand(MusicManager musicManager) {
		this.name = "skip";
		this.description = "Skip the current song";
		this.category = Category.MUSIC;
		this.musicManager = musicManager;
	}

	@Override
	protected void execute(CommandContext ctx) {
		musicManager.skip(ctx.getGuild());
		ctx.replySuccess("Song skipped.");
	}
}
