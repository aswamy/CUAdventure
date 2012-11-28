package zuulproject.event;

public interface GameListener {
	public void commandProcessed(GameEvent e);
	public void endGame();
}
