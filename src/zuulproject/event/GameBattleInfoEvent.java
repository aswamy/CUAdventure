package zuulproject.event;

import zuulproject.model.innercontroller.battle.innerbattlecontroller.*;

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
