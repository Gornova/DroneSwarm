package it.randomtower.droneswarm.model;

import com.badlogic.gdx.graphics.Color;
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

	public Station(float i, float j, Sprite texture, int radius, Player player, int hp, int creationTime) {
		super(i, j, texture, player, hp, 1, GameEntityType.STATION);
		this.creationTime = creationTime;
		this.radius = radius;
		this.selected = false;
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
		if (selected) {
			font.setColor(Color.YELLOW);
			font.draw(batch, getPercent(hp), sprite.getX() - 5, sprite.getY() + 70);
			font.setColor(Color.WHITE);
		} else {
			font.draw(batch, getPercent(hp), sprite.getX() - 5, sprite.getY() + 70);
		}
	}

	private String getPercent(int hp) {
		int p = (int) ((hp / 100f) * 100);
		return "" + p + " %";
	}

	@Override
	public void renderEffect(ShapeRenderer shapeRenderer) {
		if (G.DEBUG) {
			shapeRenderer.setColor(player.color);
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.circle(sprite.getX() + 10, sprite.getY() + 10, radius);
			shapeRenderer.end();
		}
	}

	// return if a point is inside this entity radius
	public boolean inRadius(Vector2 point) {
		return distance(point) <= radius;
	}

}
