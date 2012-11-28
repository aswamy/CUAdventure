package zuulproject.event;

import zuulproject.model.*;
import java.util.EventObject;

/**
 * An event created by the model to alert the views that the game has changed
 */

public class GameEvent extends EventObject {	
	private static final long serialVersionUID = 1L;

	private String gameStatus;
	
	public GameEvent(Object source) {
		super(source);
		gameStatus = ((GameSystem)source).getGameStatus();
	}
	
	// returns the status of the game
	public String getGameStatus() {
		return gameStatus;
	}
}
