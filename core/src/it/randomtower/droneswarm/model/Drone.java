package it.randomtower.droneswarm.model;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import it.marteEngine.tween.Ease;
import it.marteEngine.tween.LinearMotion;
import it.randomtower.droneswarm.G;

public class Drone extends GameEntity {

	public float tx;
	public float ty;
	public float moveTime;
	public LinearMotion motion;
	public int radius;

	private Random rnd = new Random();

	public State state = State.WAIT;
	public boolean selected;

	public Drone(float x, float y, Sprite img, Player player, int i, int j) {
		super(x, y, img, player, i, j, GameEntityType.DRONE);
		this.state = State.WAIT;
		this.radius = G.DRONE_RADIUS;
		this.moveTime = G.DRONE_MOVE_TIME;
	}

	public void setTarget(float f, float g) {
		this.tx = f + 5 + rnd.nextFloat() * 20;
		this.ty = g + 5 + rnd.nextFloat() * 20;
		this.state = State.MOVE;
		Vector2 v = new Vector2(sprite.getX(), sprite.getY());
		float distance = v.dst(tx, ty);
		float speed = 1;
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

	@Override
	public void update(World world) {
		GameEntity e = enemyInRadius(world, this);
		if (e != null) {
			this.state = State.ATTACK;
			combat(e, this);
			evaluateCombat(e, this.player);
			evaluateCombat(this, e.player);
		}
		if (this.state == State.ATTACK) {

		}
		if (this.state == State.MOVE) {
			this.motion.update((int) (Gdx.graphics.getDeltaTime() * 1000));
			this.sprite.setX(this.motion.getX());
			this.sprite.setY(this.motion.getY());
			if (this.motion.isFinished()) {
				this.state = State.WAIT;
			}
		}
		// if (this.state == State.WAIT && target != null && this.player.name ==
		// G.PLAYER_ONE) {
		// this.setTarget(target.x, target.y);
		// }
		if (this.state == State.WAIT && this.player.name == G.PLAYER_TWO) {
			// this.setTarget(ai.target.x, ai.target.y);
		}
	}

	@Override
	public void render(SpriteBatch batch, BitmapFont font) {
		if (selected) {
			sprite.setColor(Color.GREEN);
			sprite.draw(batch);
		} else {
			sprite.draw(batch);
		}
	}

	@Override
	public void renderEffect(ShapeRenderer shapeRenderer) {

	}

}
