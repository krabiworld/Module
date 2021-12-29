package eu.u032.Commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalCommand extends Command {

    public EvalCommand() {
        this.name = "eval";
        this.ownerCommand = true;
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getChannel().sendTyping().queue();
        event.async(() -> {
            ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
            scriptEngine.put("event", event);
            String args = event.getArgs();
            try {
                event.replySuccess("Evaluated Successfully:\n```" + scriptEngine.eval(args) + "```");
            } catch (Exception e) {
                event.replyError("Error! " + e.getMessage());
            }
        });
    }

}
