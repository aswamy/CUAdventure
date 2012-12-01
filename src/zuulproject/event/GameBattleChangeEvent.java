package zuulproject.event;

import zuulproject.model.innercontroller.battle.innerbattlecontroller.BattleCommandTypes;

public class GameBattleChangeEvent extends GameEvent {

	private static final long serialVersionUID = 8762222103990180081L;
	protected BattleCommandTypes command;
	protected boolean commandSuccess;
	
	public GameBattleChangeEvent(Object o, BattleCommandTypes command, boolean success) {
		super(o);
		this.commandSuccess = success;
		this.command = command;
	}

	public BattleCommandTypes getCommand() {
		return command;
	}

	public boolean getSuccess() {
		return commandSuccess;
	}
}
