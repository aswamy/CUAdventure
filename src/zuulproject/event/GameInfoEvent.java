package zuulproject.event;

import zuulproject.model.innercontroller.CommandTypes;

/**
 * These events are only used announce what kind of information needs to be displayed
 * They will not cause the 3D + 2D views to refresh
 * 
 * @author Alok
 */
public class GameInfoEvent extends GameEvent {

	private static final long serialVersionUID = -5748226848111096094L;
	protected CommandTypes command;

	public GameInfoEvent(Object o, CommandTypes command) {
		super(o);
		this.command = command;
	}

	public CommandTypes getCommand() {
		return command;
	}
}
