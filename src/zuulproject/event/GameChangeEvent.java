package zuulproject.event;

import zuulproject.model.innercontroller.*;

/**
 * These events are caused my commands that change the status of the game.
 * In other words, these events will cause the 2D + 3D view to refresh
 * 
 * @author Alok
 */

public class GameChangeEvent extends GameEvent {

	private static final long serialVersionUID = 2679963384241180371L;
	
	private CommandTypes command;
	private boolean success;
	
	public GameChangeEvent(Object source, CommandTypes command, boolean success) {
		super(source);
		this.command = command;
		this.success = success;
	}
	
	public CommandTypes getCommand() {
		return command;
	}
	
	public boolean getSuccess() {
		return success;
	}
}