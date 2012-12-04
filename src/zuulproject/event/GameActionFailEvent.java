package zuulproject.event;

public class GameActionFailEvent extends GameEvent {
	
	private static final long serialVersionUID = -3684920525087313723L;

	public enum FailedAction {
		SAVEFILE, SAVECOMBAT, OPEN, PARSE 
	}
	
	private FailedAction action;
	
	public GameActionFailEvent(Object o, FailedAction a) {
		super(o);
		action = a;
	}

	public FailedAction getFailedAction() {
		return action;
	}
}
