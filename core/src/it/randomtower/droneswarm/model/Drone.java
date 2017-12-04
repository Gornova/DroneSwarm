package it.randomtower.droneswarm.model;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
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
	public boolean selected;

	public Drone(float x, float y, Sprite img, Player player, int i, int j) {
		super(x, y, img, player, i, j, GameEntityType.DRONE);
		this.state = State.WAIT;
	}

	public void setTarget(float f, float g) {
		this.tx = f + 5 + rnd.nextFloat() * 20;
		this.ty = g + 5 + rnd.nextFloat() * 20;
		this.state = State.MOVE;
		Vector2 v = new Vector2(sprite.getX(), sprite.getY());
		float distance = v.dst(tx, ty);
		float speed = 1;
		// v = s/t and t = s/v
		this.motion = new LinearMotion(sprite.getX(), sprite.getY(), tx, ty, distance / speed, Ease.LINEAR);
	}

	public boolean isInside(Rectangle selection) {
		if (selection.width > 0 && selection.height > 0) {
			return selection.contains(sprite.getX(), sprite.getY());
		}
		Rectangle r = new Rectangle();
		if (selection.width <= 0) {
			r.setX(selection.x + selection.width);
			r.setWidth(Math.abs(selection.width));
		}
		if (selection.height <= 0) {
			r.setY(selection.y + selection.height);
			r.setHeight(Math.abs(selection.height));
		}
		return r.contains(sprite.getX(), sprite.getY());
	}

}
