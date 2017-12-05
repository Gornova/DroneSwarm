package it.randomtower.droneswarm.model;

import java.util.Optional;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameEntity {

	public Sprite sprite;
	public int timer;
	public int hp;
	public int atk;
	public Player player;
	public GameEntityType type;
	public boolean toRemove;

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

	public abstract void update(World world);

	public abstract void render(SpriteBatch batch, BitmapFont font);

	public abstract void renderEffect(ShapeRenderer shapeRenderer);

	public void combat(GameEntity e, Drone d) {
		e.hp -= d.atk;
		d.hp -= e.atk;
	}

	public GameEntity enemyInRadius(World world, Drone d) {
		Optional<GameEntity> o = world.stream().filter(e -> e.player.name != d.player.name)
				.filter(e -> e.distance(d.getVector2()) < d.radius).findFirst();
		return o.isPresent() ? o.get() : null;
	}

	public void evaluateCombat(GameEntity e, Player player) {
		if (isDead(e)) {
			if (e.type == GameEntityType.STATION) {
				e.hp = 100;
				changePlayer(e, player);
			} else {
				e.toRemove = true;
			}
		}
	}

	public void changePlayer(GameEntity e, Player player) {
		e.player = player;
	}

	public boolean isDead(GameEntity e) {
		return e.hp < 0;
	}

}
