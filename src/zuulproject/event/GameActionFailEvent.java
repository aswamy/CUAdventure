package zuulproject.event;

/**
 * An event sent when opening/saving file fails
 * @author Alok
 *
 */
public class GameActionFailEvent extends GameEvent {
	
	private static final long serialVersionUID = -3684920525087313723L;

	public enum FailedAction {
		SAVEFILE, OPENFILE, PARSEFILE
	}
	
	private FailedAction action;
	private boolean success;
	
	public GameActionFailEvent(Object o, FailedAction a, boolean s) {
		super(o);
		action = a;
		success = s;
	}

	public FailedAction getFailedAction() {
		return action;
	}
	
	public boolean getActionSuccess() {
		return success;
	}
}
