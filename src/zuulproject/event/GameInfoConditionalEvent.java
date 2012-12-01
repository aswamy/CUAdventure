package zuulproject.event;

import zuulproject.model.innercontroller.CommandTypes;

public class GameInfoConditionalEvent extends GameInfoEvent {

	private static final long serialVersionUID = -8196151262623445910L;
	private Object conditionObject;
	
	public GameInfoConditionalEvent(Object o, CommandTypes command, Object conditionalObj) {
		super(o, command);
		conditionObject = conditionalObj;
	}
	
	public Object getConditionObject() {
		return conditionObject;
	}
}
