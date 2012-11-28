package zuulproject.event;

/**
 * Listeners that only listen to the game when the status is changed
 * @author Alok
 *
 */
public interface GameChangeListener {
	public void gameCmdProcessed(GameChangeEvent e);
	public void gameEnded(GameEvent e);
}
