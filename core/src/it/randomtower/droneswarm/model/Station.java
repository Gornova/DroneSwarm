package it.randomtower.droneswarm.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;

public class Station extends GameEntity {

	public int radius;
	public int creationTime; // in ms
	public boolean selected;
	public int created;

	public Station(float i, float j, Sprite texture, int radius, Player player, int hp, int creationTime) {
		super(i, j, texture, player, hp, 1, GameEntityType.STATION);
		this.creationTime = creationTime;
		this.radius = radius;
		this.selected = false;
	}

	public boolean inRange(int screenX, int screenY) {
		Circle c = new Circle(sprite.getX(), sprite.getY(), radius);
		return c.contains(screenX, screenY);
	}

}
