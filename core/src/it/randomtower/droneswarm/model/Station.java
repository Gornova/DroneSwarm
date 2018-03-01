package it.randomtower.droneswarm.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import it.randomtower.droneswarm.G;
import it.randomtower.droneswarm.GameEntityFactory;

public class Station extends GameEntity {

	public int radius;
	public int creationTime; // in ms
	public boolean selected;
	public int created;
	private Sprite percentage;

	public Station(float i, float j, Sprite texture, int radius, Player player, int hp, int creationTime) {
		super(i, j, texture, player, hp, 1, GameEntityType.STATION);
		this.creationTime = creationTime;
		this.radius = radius;
		this.selected = false;
		this.percentage = new Sprite(new Texture("percentage.png"));
	}

	public void update(World world) {
		if (this.player.name != G.NEUTRAL && this.timer >= this.creationTime) {
			this.created++;
			this.timer = 0;
			Sprite img = null;
			if (this.player.name == G.PLAYER_ONE) {
				img = new Sprite(new Texture("drone.png"));
			} else {
				img = new Sprite(new Texture("drone-blue.png"));
			}
			world.add(GameEntityFactory.createDrone(this.sprite.getX(), this.sprite.getY(), this.radius, this.player,
					img, null));
		}
	}

	@Override
	public void render(SpriteBatch batch, BitmapFont font) {
		if (getPercent(hp) > 70) {
			batch.setColor(G.GREEN);
		} else if (getPercent(hp) > 50 && getPercent(hp) <= 70) {
			batch.setColor(G.ORANGE);
		} else if (getPercent(hp) <= 50) {
			batch.setColor(G.RED);
		}
		batch.draw(percentage, sprite.getX() - 40, sprite.getY() - 40);
		batch.setColor(Color.WHITE);
	}

	private int getPercent(int hp) {
		return (int) ((hp / 100f) * 100);
	}

	@Override
	public void renderEffect(ShapeRenderer shapeRenderer) {
		if (G.DEBUG) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.setColor(player.colorStation);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.circle(sprite.getX() + 10, sprite.getY() + 10, radius);
			shapeRenderer.end();
		}
	}

	// return if a point is inside this entity radius
	public boolean inRadius(Vector2 point) {
		return distance(point) <= radius;
	}

}
