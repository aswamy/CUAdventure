package zuulproject.event;

import zuulproject.model.innercontroller.CommandTypes;


/**
 * These events are only used announce what kind of information needs to be displayed
 * but unlike GameInfoEvent, these events are conditional - the info depends on the status of the game
 * These events wont refresh 2D + 3D view
 * 
 * @author Alok
 */
public class GameConditionalInfoEvent extends GameInfoEvent {

	private static final long serialVersionUID = 5117068892513693845L;

	protected boolean success;
	
	public GameConditionalInfoEvent(Object o, CommandTypes command, boolean success) {
		super(o, command);
		this.success = success;
	}

	public boolean getSuccess() {
		return success;
	}
}
