package it.randomtower.droneswarm.action;

import com.badlogic.gdx.math.Vector2;

public class MoveDrone implements GameAction {

	private Vector2 target;

	public MoveDrone(Vector2 target) {
		this.target = target;
	}

	@Override
	public boolean execute() {
		return false;
	}

}
