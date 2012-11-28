package zuulproject.event;

/**
 * An event created by the model to alert the views that the game has changed
 */

public class GameWelcomeEvent extends GameEvent {	
	private static final long serialVersionUID = -6366276021531910302L;

	public GameWelcomeEvent(Object source) {
		super(source);
	}
}
