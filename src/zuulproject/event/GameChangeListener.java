package zuulproject.event;

/**
 * Listeners that only listen to the game when the status is changed
 * @author Alok
 *
 */
public interface GameChangeListener {
	public void gameBegins(GameEvent e);
	public void gameCmdProcessed(GameChangeEvent e);
	public void gameBattleCmdProcessed(GameBattleChangeEvent e);
	public void gameEnded(GameOverEvent e);
	public void gameBattleEnded(GameEvent e);
}
