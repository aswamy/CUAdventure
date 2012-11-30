package zuulproject.event;

public class GameOverEvent extends GameEvent {

	private static final long serialVersionUID = -7611551191543855816L;

	public enum GameResult {
		QUIT, LOSE, WIN;
	}
	
	private GameResult result;
	
	public GameOverEvent(Object source) {
		super(source);
		result = (GameResult)source;
	}
	
	public GameResult getGameResult() {
		return result;
	}
}
