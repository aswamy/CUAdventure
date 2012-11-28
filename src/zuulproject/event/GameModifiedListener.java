package zuulproject.event;

/**
 * Listeners that only listen to the game when the status is changed
 * @author Alok
 *
 */
public interface GameModifiedListener {
	public void commandProcessed(GameModifiedEvent e);
	public void gameEnded();
}
