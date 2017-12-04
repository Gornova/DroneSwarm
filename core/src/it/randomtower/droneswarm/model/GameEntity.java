package it.randomtower.droneswarm.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class GameEntity {

	public Sprite sprite;
	public int timer;
	public int hp;
	public int atk;
	public Player player;
	public GameEntityType type;

	public GameEntity(float i, float j, Sprite sprite, Player player, int hp, int atk, GameEntityType type) {
		this.sprite = sprite;
		this.sprite.setPosition(i, j);
		this.player = player;
		this.hp = hp;
		this.atk = atk;
		this.type = type;
	}

	public float distance(Vector2 other) {
		return other.dst(sprite.getX(), sprite.getY());
	}

	public Vector2 getVector2() {
		return new Vector2(sprite.getX(), sprite.getY());
	}

}
