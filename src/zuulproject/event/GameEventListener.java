package zuulproject.event;

/**
 * Listeners that listen to the game any time the player interacts with it (not only when the game changes)
 * 
 * @author Alok
 */
public interface GameEventListener extends GameChangeListener {
	public void gameActionFailed(GameActionFailEvent e);
	public void gameBattleBegins(GameEvent e);
	public void gameInfoRequested(GameInfoEvent e);
	public void gameBattleInfoRequested(GameBattleInfoEvent e);
}
