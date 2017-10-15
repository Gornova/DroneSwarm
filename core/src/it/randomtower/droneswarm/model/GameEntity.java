package it.randomtower.droneswarm.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class GameEntity {

	public float x;
	public float y;
	public Texture img;
	public int timer;
	public Color color;
	public int player;
	public int hp;
	public int atk;

	public GameEntity(float i, float j, Texture texture, Color color, int player, int hp, int atk) {
		this.x = i;
		this.y = j;
		this.img = texture;
		this.color = color;
		this.player = player;
		this.hp = hp;
		this.atk = atk;
	}

	public float distance(GameEntity other) {
		Vector2 o = new Vector2(other.x, other.y);
		return o.dst(x, y);
	}

}
