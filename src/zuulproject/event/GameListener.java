package zuulproject.event;

public interface GameListener {
	public void commandProcessed(GameModifiedEvent e);
	public void endGame();
}
