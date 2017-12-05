package it.randomtower.droneswarm.action;

public interface GameAction {

	public boolean execute();

	public boolean isToRemove();

	public void setToRemove();

}
