package it.randomtower.droneswarm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class World {

	private List<GameEntity> entities = new ArrayList<GameEntity>();
	private List<GameEntity> toAdd = new ArrayList<GameEntity>();

	public void add(GameEntity ge) {
		toAdd.add(ge);
	}

	public void clear() {
		entities.clear();
		toAdd.clear();
	}

	public void render(SpriteBatch batch, BitmapFont font) {
		entities.stream().forEach(e -> e.render(batch, font));
	}

	public void update() {
		for (GameEntity ge : entities) {
			ge.timer += Gdx.graphics.getDeltaTime() * 1000;
			ge.update(this);
		}
		// add entities
		entities.addAll(toAdd);
		toAdd.clear();
	}

	public Stream<GameEntity> stream() {
		return entities.stream();
	}

	public void removeAll(List<GameEntity> collect) {
		entities.removeAll(collect);
	}

	public void renderEffect(ShapeRenderer shapeRenderer) {
		entities.stream().forEach(e -> e.renderEffect(shapeRenderer));

	}

}
