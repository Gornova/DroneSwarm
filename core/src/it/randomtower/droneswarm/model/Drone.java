package it.randomtower.droneswarm.model;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import it.marteEngine.tween.Ease;
import it.marteEngine.tween.LinearMotion;

public class Drone extends GameEntity {

	public float tx;
	public float ty;
	public float moveTime = 250;
	public LinearMotion motion;
	public int radius = 20;

	private Random rnd = new Random();

	public State state = State.WAIT;

	public Drone(float x, float y, Texture img, Player player, int i, int j) {
		super(x, y, img, player, i, j, GameEntityType.DRONE);
	}

	public void setTarget(float f, float g) {
		this.tx = f + 5 + rnd.nextFloat() * 20;
		this.ty = g + 5 + rnd.nextFloat() * 20;
		this.state = State.MOVE;
		Vector2 v = new Vector2(x, y);
		float distance = v.dst(tx, ty);
		float speed = 1;
		// v = s/t and t = s/v
		this.motion = new LinearMotion(x, y, tx, ty, distance / speed, Ease.LINEAR);
	}

}
