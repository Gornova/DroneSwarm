package it.randomtower.droneswarm.screen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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

import it.randomtower.droneswarm.AI;
import it.randomtower.droneswarm.G;
import it.randomtower.droneswarm.GameEntityFactory;
import it.randomtower.droneswarm.action.GameAction;
import it.randomtower.droneswarm.level.Level;
import it.randomtower.droneswarm.level.LevelLoader;
import it.randomtower.droneswarm.level.StationLevel;
import it.randomtower.droneswarm.model.Drone;
import it.randomtower.droneswarm.model.GameEntityType;
import it.randomtower.droneswarm.model.Player;
import it.randomtower.droneswarm.model.Station;
import it.randomtower.droneswarm.model.World;

public class GameScreen implements Screen, InputProcessor {

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera camera;
	private BitmapFont font;
	private boolean playerOneWin;
	private boolean playerTwoWin;
	private List<GameAction> actions = new ArrayList<>();
	private List<GameAction> actionToAdd = new ArrayList<>();
	private Player one;
	private Rectangle selection = new Rectangle(0, 0, 0, 0);
	private boolean startSelect = false;
	private List<AI> ais = new ArrayList<>();
	private World world;
	private Game game;
	private int level;
	private Texture background;
	private Player two;
	private Player neutral;
	private Player three;

	public GameScreen(Game game, int level) {
		this.game = game;
		this.level = level;
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
		two = new Player(G.LIGHT_BLUE, G.PLAYER_TWO);
		three = new Player(G.LIGHT_RED, G.PLAYER_THREE);
		neutral = new Player(G.LIGHT_GRAY, G.NEUTRAL);

		Level lev = LevelLoader.get(level);
		for (StationLevel s : lev.getStations()) {
			Station station = GameEntityFactory.buildStation(s.getX(), s.getY(), getSprite(s.getPlayer()), 50,
					getPlayer(s.getPlayer()), 100, 100);
			world.add(station);
			if (s.isStartingAi()) {
				ais.add(new AI(getPlayer(s.getPlayer()), station.getVector2(), 3000));
			}
		}
		//
		background = new Texture("background.png");
	}

	private Sprite getSprite(int player) {
		switch (player) {
		case 0:
			return new Sprite(new Texture("station-gray.png"));
		case 1:
			return new Sprite(new Texture("station.png"));
		case 2:
			return new Sprite(new Texture("station-blue.png"));
		case 3:
			return new Sprite(new Texture("station-red.png"));
		default:
			return null;
		}
	}

	private Player getPlayer(int player) {
		switch (player) {
		case 0:
			return neutral;
		case 1:
			return one;
		case 2:
			return two;
		case 3:
			return three;
		default:
			return null;
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined); // Important
		batch.begin();
		if (playerOneWin) {
			font.draw(batch, "Level completed!", 160, 240, 320, Align.center, true);
			font.draw(batch, "Click to continue", 160, 200, 320, Align.center, true);
			batch.end();
			return;
		}
		if (playerTwoWin) {
			font.draw(batch, "Game over!", 160, 240, 320, Align.center, true);
			font.draw(batch, "Press ESC to get back to level selection", 160, 200, 320, Align.center, true);
			batch.end();
			return;
		}
		// render actual world
		batch.draw(background, 0, 0);
		world.render(batch, font);
		// render effects
		world.renderEffect(shapeRenderer);
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
		for (AI ai : ais) {
			ai.update(world.stream().filter(e -> e.type == GameEntityType.STATION).collect(Collectors.toList()));
			ai.process(world.stream().filter(e -> e.type == GameEntityType.DRONE && e.player.name == ai.player.name)
					.collect(Collectors.toList()));
		}
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
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
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
		if (keycode == Keys.ESCAPE) {
			game.setScreen(new LevelSelectScreen(game));
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (playerOneWin && button == 0) {
			System.out.println("next level");
			G.unlockedLevel++;
			saveUnlockedLevelProgress();
			if (level + 1 <= G.TOTAL_LEVELS) {
				game.setScreen(new GameScreen(game, level + 1));
			} else {
				game.setScreen(new WinScreen(game));
			}
			return false;
		}
		if (button == 1) {
			// check if screenX and screenY are point for a station and set as
			// target center of that station
			Vector2 point = new Vector2(screenX, screenY);
			Optional<Station> station = world.stream().filter(e -> e.type == GameEntityType.STATION)
					.map(s -> (Station) s).filter(s -> s.inRadius(point)).findFirst();
			// for all selected drones, set target as point
			world.stream().filter(e -> e.type == GameEntityType.DRONE).map(d -> (Drone) d)
					.filter(d -> d.player.name == one.name).filter(d -> d.selected).forEach(d -> {
						if (station.isPresent()) {
							d.setTarget(station.get().getVector2().x, station.get().getVector2().y);
						} else {
							d.setTarget(screenX, screenY);
						}
					});
		}
		return false;
	}

	private void saveUnlockedLevelProgress() {
		try {
			if (G.HOME_OK) {
				Path path = Paths.get(G.GAME_HOME + File.separator + G.UNLOCKED_LEVELS_FILE);
				if (!Files.exists(path)) {
					Files.createFile(path);
				}
				File f = new File(G.GAME_HOME + File.separator + G.UNLOCKED_LEVELS_FILE);
				FileWriter fw = new FileWriter(f);
				fw.write(G.unlockedLevel);
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		System.out.println("stop drag");
		if (startSelect) {
			startSelect = false;
			// make selection
			List<Drone> selectedDrones = world.stream().filter(e -> e.type == GameEntityType.DRONE).map(d -> (Drone) d)
					.filter(d -> d.player.name == one.name).filter(d -> d.isInside(selection))
					.collect(Collectors.toList());
			if (!selectedDrones.isEmpty()) {
				for (Drone drone : selectedDrones) {
					drone.selected = true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (!startSelect) {
			// un-made selection
			world.stream().filter(e -> e.type == GameEntityType.DRONE).map(d -> (Drone) d)
					.filter(d -> d.player.name == one.name).forEach(d -> d.selected = false);
			selection.set(screenX, screenY, 0, 0);
			startSelect = true;
			selection.set(screenX, screenY, 0, 0);
		}
		selection.setSize(screenX - selection.x, screenY - selection.y);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
