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

	public float distance(GameEntity other) {
		Vector2 o = new Vector2(other.sprite.getX(), other.sprite.getY());
		return o.dst(sprite.getX(), sprite.getY());
	}

}
