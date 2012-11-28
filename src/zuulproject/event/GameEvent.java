package zuulproject.event;

import java.util.EventObject;

/**
 * Just an event Object that all game events extend from
 * @author Alok
 *
 */
public class GameEvent extends EventObject {

	private static final long serialVersionUID = 6134178878134047837L;

	public GameEvent(Object source) {
		super(source);
	}	
}
