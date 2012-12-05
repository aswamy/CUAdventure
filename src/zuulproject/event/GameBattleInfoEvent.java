package zuulproject.event;

import zuulproject.model.innercontroller.battle.innerbattlecontroller.*;

/*
 * An event where information from the battle is requested (like who are you fighting, what is their status)
 * But this event also says that the status of the game hasnt changed (no one is killed, no one attacked yet)
 */
public class GameBattleInfoEvent extends GameEvent {

	private static final long serialVersionUID = 1104988322526206728L;

	protected BattleCommandTypes command;

	public GameBattleInfoEvent(Object o, BattleCommandTypes command) {
		super(o);
		this.command = command;
	}

	public BattleCommandTypes getCommand() {
		return command;
	}
}
