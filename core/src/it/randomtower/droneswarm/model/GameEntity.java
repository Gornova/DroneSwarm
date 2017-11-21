package it.randomtower.droneswarm.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class GameEntity {

	public float x;
	public float y;
	public Texture img;
	public int timer;
	public int hp;
	public int atk;
	public Player player;
	public GameEntityType type;

	public GameEntity(float i, float j, Texture texture, Player player, int hp, int atk, GameEntityType type) {
		this.x = i;
		this.y = j;
		this.img = texture;
		this.player = player;
		this.hp = hp;
		this.atk = atk;
		this.type = type;
	}

	public float distance(GameEntity other) {
		Vector2 o = new Vector2(other.x, other.y);
		return o.dst(x, y);
	}

}
