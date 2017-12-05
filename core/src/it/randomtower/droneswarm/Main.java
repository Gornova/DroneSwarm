package it.randomtower.droneswarm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import it.randomtower.droneswarm.action.GameAction;
import it.randomtower.droneswarm.model.Drone;
import it.randomtower.droneswarm.model.GameEntityType;
import it.randomtower.droneswarm.model.Player;
import it.randomtower.droneswarm.model.Station;
import it.randomtower.droneswarm.model.World;

public class Main extends ApplicationAdapter implements InputProcessor {

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private Vector2 target;
	private OrthographicCamera camera;
	private BitmapFont font;
	private boolean playerOneWin;
	private boolean playerTwoWin;
	private List<GameAction> actions = new ArrayList<GameAction>();
	private List<GameAction> actionToAdd = new ArrayList<GameAction>();
	private Player one;
	private Rectangle selection = new Rectangle(0, 0, 0, 0);
	private boolean startSelect = false;
	private AI ai;
	private World world;

	@Override
	public void create() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);
		font = new BitmapFont(true);
		Gdx.input.setInputProcessor(this);

		//
		world = new World();
		one = new Player(Color.FOREST, G.PLAYER_ONE);
		Player two = new Player(G.LIGHT_BLUE, G.PLAYER_TWO);
		Player neutral = new Player(G.LIGHT_GRAY, G.NEUTRAL);

		Station ste = GameEntityFactory.buildStation(50, 50, new Sprite(new Texture("station.png")), 50, one, 100, 100);
		world.add(ste);

		Station ste2 = GameEntityFactory.buildStation(640 - 80, 480 - 80, new Sprite(new Texture("station-blue.png")),
				50, two, 100, 500);
		world.add(ste2);

		Station ste3 = GameEntityFactory.buildStation(640 - 80, 50, new Sprite(new Texture("station-gray.png")), 50,
				neutral, 100, 500);
		world.add(ste3);

		//
		ai = new AI(two, ste2.getVector2());
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined); // Important
		batch.begin();
		if (playerOneWin) {
			font.draw(batch, "Player One Wins!", 160, 240, 320, Align.center, true);
			batch.end();
			return;
		}
		if (playerTwoWin) {
			font.draw(batch, "Player Two Wins!", 160, 240, 320, Align.center, true);
			batch.end();
			return;
		}
		// render effects
		// world.renderEffect(shapeRenderer);
		// render actual world
		world.render(batch, font);
		// draw selection
		if (startSelect) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.rect(selection.x, selection.y, selection.width, selection.height);
			shapeRenderer.end();
		}
		batch.end();
		// add actions
		actions.addAll(actionToAdd);
		// execute actions
		for (GameAction action : actions) {
			action.execute();
			action.setToRemove();
		}
		// update
		ai.update(world.stream().filter(e -> e.type == GameEntityType.STATION).collect(Collectors.toList()));
		world.update();
		// check win condition
		long ts = world.stream().filter(e -> (e.type == GameEntityType.STATION)).count();
		long onets = countAllStations(G.PLAYER_ONE);
		long twots = countAllStations(G.PLAYER_TWO);
		playerOneWin = ts == onets ? true : false;
		playerTwoWin = ts == twots ? true : false;
		// remove entities
		world.removeAll(world.stream().filter(e -> e.toRemove).collect(Collectors.toList()));
		// clear actions
		actionToAdd.clear();
		actions.removeAll(actions.stream().filter(a -> a.isToRemove()).collect(Collectors.toList()));
	}

	private long countAllStations(int name) {
		return world.stream().filter(e -> (e.type == GameEntityType.STATION)).filter(e -> e.player.name == name)
				.count();
	}

	@Override
	public void dispose() {
		batch.dispose();
		world.clear();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// selection rectangle
		if (button == 0) {
			if (!startSelect) {
				startSelect = true;
				// un-made selection
				world.stream().filter(e -> e.type == GameEntityType.DRONE).map(d -> (Drone) d)
						.filter(d -> d.player.name == one.name).forEach(d -> d.selected = false);
				selection.set(screenX, screenY, 0, 0);
			} else {
				startSelect = false;
				// make selection
				List<Drone> selectedDrones = world.stream().filter(e -> e.type == GameEntityType.DRONE)
						.map(d -> (Drone) d).filter(d -> d.player.name == one.name).filter(d -> d.isInside(selection))
						.collect(Collectors.toList());
				if (!selectedDrones.isEmpty()) {
					for (Drone drone : selectedDrones) {
						drone.selected = true;
					}
				}
			}
		}
		if (button == 1) {
			// for all selected drones, set target as point
			world.stream().filter(e -> e.type == GameEntityType.DRONE).map(d -> (Drone) d)
					.filter(d -> d.player.name == one.name).filter(d -> d.selected)
					.forEach(d -> d.setTarget(screenX, screenY));
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (startSelect) {
			selection.setSize(screenX - selection.x, screenY - selection.y);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
