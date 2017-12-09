package bgu.spl.a2;

import java.util.List;
import java.util.LinkedList;

/**
 * an abstract class that represents private states of an actor
 * it holds actions that the actor has executed so far
 * IMPORTANT: You can not add any field to this class.
 */
public abstract class PrivateState {

	// holds the actions' name what were executed
	private List<String> history;

	public List<String> getLogger(){
		return history;
	}

	/**
	 * add an action to the records
	 *
	 * @param actionName
	 */
	public void addRecord(String actionName){//pretty sure only one thread has access to privatestate at a time
		if(history==null){history = new LinkedList<String>();}
		history.add(actionName);
	}


}
