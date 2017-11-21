package it.randomtower.droneswarm.model;

import com.badlogic.gdx.graphics.Texture;

public class Station extends GameEntity {

	public int radius;
	public int creationTime; // in ms

	public Station(float i, float j, Texture texture, int radius, Player player, int hp, int creationTime) {
		super(i, j, texture, player, hp, 1, GameEntityType.STATION);
		this.creationTime = creationTime;
		this.radius = radius;
	}

}
