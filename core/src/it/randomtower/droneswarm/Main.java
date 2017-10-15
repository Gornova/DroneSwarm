package it.randomtower.droneswarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import it.randomtower.droneswarm.model.Drone;
import it.randomtower.droneswarm.model.GameEntity;
import it.randomtower.droneswarm.model.State;
import it.randomtower.droneswarm.model.Station;

public class Main extends ApplicationAdapter implements InputProcessor {

	private static final int PLAYER_ONE = 1;
	private static final int PLAYER_TWO = 2;
	private SpriteBatch batch;
	private List<GameEntity> entities = new ArrayList<GameEntity>();
	private List<GameEntity> toAdd = new ArrayList<GameEntity>();
	private List<GameEntity> toRemove = new ArrayList<GameEntity>();
	private ShapeRenderer shapeRenderer;
	private final Color LIGHT_RED = new Color(0.25f, 0, 0, 0.1f);
	private final Color LIGHT_BLUE = new Color(0, 0, 0.25f, 0.1f);
	private Vector2 target;
	private OrthographicCamera camera;
	private BitmapFont font;
	private boolean playerOneWin;
	private boolean playerTwoWin;

	@Override
	public void create() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont(true); // or use alex answer to use custom font

		Gdx.input.setInputProcessor(this);

		Station ste = new Station(50, 50, new Texture("station.png"), 50, LIGHT_RED, PLAYER_ONE, 100, 100);
		entities.add(ste);

		Station ste2 = new Station(640 - 80, 480 - 80, new Texture("station-blue.png"), 50, LIGHT_BLUE, PLAYER_TWO, 100,
				500);
		entities.add(ste2);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (GameEntity ge : entities) {
			// render efffects
			renderRadius(shapeRenderer, ge);
		}
		batch.setProjectionMatrix(camera.combined); // Important
		batch.begin();
		if (playerOneWin) {
			font.draw(batch, "Player One Wins!", 320, 240);
			batch.end();
			return;
		}
		if (playerTwoWin) {
			font.draw(batch, "Player Two Wins!", 320, 240);
			batch.end();
			return;
		}
		for (GameEntity ge : entities) {
			// render entities
			batch.draw(ge.img, ge.x, ge.y);
			if (ge instanceof Station) {
				font.draw(batch, getPercent(ge.hp), ge.x - 5, ge.y + 30);
			}
		}
		batch.end();
		// update
		for (GameEntity ge : entities) {
			update(ge);
		}
		// add entities
		entities.addAll(toAdd);
		toAdd.clear();
		// remove entities
		for (GameEntity ge : toRemove) {
			if (ge instanceof Station) {
				playerOneWin = ge.player == PLAYER_TWO ? true : false;
				playerTwoWin = ge.player == PLAYER_TWO ? true : false;
			}
		}
		entities.removeAll(toRemove);
		toRemove.clear();
	}

	private String getPercent(int hp) {
		int p = (int) ((hp / 100f) * 100);
		return "" + p + " %";
	}

	private void update(GameEntity ge) {
		ge.timer += Gdx.graphics.getDeltaTime() * 1000;
		if (ge instanceof Station) {
			Station s = (Station) ge;
			if (s.timer >= s.creationTime) {
				s.timer = 0;
				createDrone(s.x, s.y, s.radius, s.color, s.player);
			}
		}
		if (ge instanceof Drone) {
			Drone d = (Drone) ge;
			GameEntity e = enemyInRadius(d);
			if (e != null) {
				d.state = State.ATTACK;
				combat(e, d);
				if (e.hp < 0) {
					toRemove.add(e);
				}
				if (d.hp < 0) {
					toRemove.add(d);
				}
			}
			if (d.state == State.ATTACK) {

			}
			if (d.state == State.MOVE) {
				d.motion.update((int) (Gdx.graphics.getDeltaTime() * 1000));
				d.x = d.motion.getX();
				d.y = d.motion.getY();
				if (d.motion.isFinished()) {
					d.state = State.WAIT;
				}
			}
			if (d.state == State.WAIT && target != null && d.player == PLAYER_ONE) {
				d.setTarget(target.x, target.y);
			}
		}
	}

	private void combat(GameEntity e, Drone d) {
		e.hp -= d.atk;
		d.hp -= e.atk;
	}

	private GameEntity enemyInRadius(Drone d) {
		// TODO: al posto di instanceof mettere un tipo alle entità!!
		Optional<GameEntity> o = entities.stream().filter(e -> e.player != d.player)
				.filter(e -> e.distance(d) < d.radius).findFirst();
		return o.isPresent() ? o.get() : null;
	}

	private void createDrone(float x, float y, int radius, Color color, int player) {
		Random rnd = new Random();
		int dx = rnd.nextInt(radius / 2);
		int dy = rnd.nextInt(radius / 2);
		int mx = rnd.nextBoolean() ? 1 : -1;
		int my = rnd.nextBoolean() ? 1 : -1;
		Texture img = null;
		if (player == PLAYER_ONE) {
			img = new Texture("drone.png");
		} else {
			img = new Texture("drone-blue.png");
		}
		Drone de = new Drone(x + 10, y + 10, img, color, player, 10, 2);
		if (target == null) {
			de.setTarget(x + mx * dx, y + my * dy);
		}
		// de.setTarget(100, 100);
		toAdd.add(de);
	}

	private void renderRadius(ShapeRenderer shapeRenderer, GameEntity ge) {
		if (ge instanceof Station) {
			Station s = (Station) ge;
			shapeRenderer.setColor(s.color);
			shapeRenderer.setProjectionMatrix(camera.combined); // Important
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.circle(s.x + 10, s.y + 10, s.radius);
			shapeRenderer.end();
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		for (GameEntity ge : entities) {
			ge.img.dispose();
		}
		entities.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("Click " + screenX + "," + screenY);
		target = new Vector2(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
