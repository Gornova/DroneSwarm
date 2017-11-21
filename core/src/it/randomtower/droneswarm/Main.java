package it.randomtower.droneswarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import it.randomtower.droneswarm.action.GameAction;
import it.randomtower.droneswarm.model.Drone;
import it.randomtower.droneswarm.model.GameEntity;
import it.randomtower.droneswarm.model.GameEntityType;
import it.randomtower.droneswarm.model.Player;
import it.randomtower.droneswarm.model.State;
import it.randomtower.droneswarm.model.Station;

public class Main extends ApplicationAdapter implements InputProcessor {

	private SpriteBatch batch;
	private List<GameEntity> entities = new ArrayList<GameEntity>();
	private List<GameEntity> toAdd = new ArrayList<GameEntity>();
	private List<GameEntity> toRemove = new ArrayList<GameEntity>();
	private ShapeRenderer shapeRenderer;
	private Vector2 target;
	private OrthographicCamera camera;
	private BitmapFont font;
	private boolean playerOneWin;
	private boolean playerTwoWin;
	private List<GameAction> actions = new ArrayList<GameAction>();
	private List<GameAction> actionToAdd = new ArrayList<GameAction>();
	private List<GameAction> actionToRemove = new ArrayList<GameAction>();
	private Player one;
	private Rectangle selection = new Rectangle(0, 0, 0, 0);
	private boolean startSelect = false;

	@Override
	public void create() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont(true); // or use alex answer to use custom font
		Gdx.input.setInputProcessor(this);

		//
		one = new Player(G.LIGHT_RED, G.PLAYER_ONE);
		Player two = new Player(G.LIGHT_BLUE, G.PLAYER_TWO);
		Player neutral = new Player(G.LIGHT_GRAY, G.NEUTRAL);

		Station ste = GameEntityFactory.buildStation(50, 50, new Texture("station.png"), 50, one, 100, 100);
		entities.add(ste);

		Station ste2 = GameEntityFactory.buildStation(640 - 80, 480 - 80, new Texture("station-blue.png"), 50, two, 100,
				500);
		entities.add(ste2);

		Station ste3 = GameEntityFactory.buildStation(640 - 80, 50, new Texture("station-gray.png"), 50, neutral, 100,
				500);
		entities.add(ste3);
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
			if (ge.type == GameEntityType.STATION) {
				Station s = (Station) ge;
				if (s.selected) {
					font.setColor(Color.YELLOW);
					font.draw(batch, getPercent(s.hp), s.x - 5, s.y + 30);
					font.setColor(Color.WHITE);
				} else {
					font.draw(batch, getPercent(s.hp), s.x - 5, s.y + 30);
				}
			}
		}
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
			actionToRemove.add(action);
		}
		// update
		for (GameEntity ge : entities) {
			update(ge);
		}
		// add entities
		entities.addAll(toAdd);
		toAdd.clear();
		// check win condition
		long ts = entities.stream().filter(e -> (e.type == GameEntityType.STATION)).count();
		long onets = countAllStations(G.PLAYER_ONE);
		long twots = countAllStations(G.PLAYER_TWO);
		playerOneWin = ts == onets ? true : false;
		playerTwoWin = ts == twots ? true : false;
		// remove entities
		entities.removeAll(toRemove);
		toRemove.clear();
		// clear actions
		actionToAdd.clear();
		actions.removeAll(actionToRemove);
		actionToRemove.clear();
	}

	private long countAllStations(int name) {
		return entities.stream().filter(e -> (e.type == GameEntityType.STATION)).filter(e -> e.player.name == name)
				.count();
	}

	private String getPercent(int hp) {
		int p = (int) ((hp / 100f) * 100);
		return "" + p + " %";
	}

	private void update(GameEntity ge) {
		ge.timer += Gdx.graphics.getDeltaTime() * 1000;
		if (ge.type == GameEntityType.STATION) {
			Station s = (Station) ge;
			if (s.player.name != G.NEUTRAL && s.timer >= s.creationTime) {
				s.timer = 0;
				Texture img = null;
				if (s.player.name == G.PLAYER_ONE) {
					img = new Texture("drone.png");
				} else {
					img = new Texture("drone-blue.png");
				}
				toAdd.add(GameEntityFactory.createDrone(s.x, s.y, s.radius, s.player, img, target));
			}
		}
		if (ge.type == GameEntityType.DRONE) {
			Drone d = (Drone) ge;
			GameEntity e = enemyInRadius(d);
			if (e != null) {
				d.state = State.ATTACK;
				combat(e, d);
				evaluateCombat(e, d.player);
				evaluateCombat(d, e.player);
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
			if (d.state == State.WAIT && target != null && d.player.name == G.PLAYER_ONE) {
				d.setTarget(target.x, target.y);
			}
		}
	}

	private void evaluateCombat(GameEntity e, Player player) {
		if (isDead(e)) {
			if (e.type == GameEntityType.STATION) {
				e.hp = 100;
				changePlayer(e, player);
			} else {
				toRemove.add(e);
			}
		}
	}

	private void changePlayer(GameEntity e, Player player) {
		e.player = player;
	}

	private boolean isDead(GameEntity e) {
		return e.hp < 0;
	}

	private void combat(GameEntity e, Drone d) {
		e.hp -= d.atk;
		d.hp -= e.atk;
	}

	private GameEntity enemyInRadius(Drone d) {
		Optional<GameEntity> o = entities.stream().filter(e -> e.player.name != d.player.name)
				.filter(e -> e.distance(d) < d.radius).findFirst();
		return o.isPresent() ? o.get() : null;
	}

	private void renderRadius(ShapeRenderer shapeRenderer, GameEntity ge) {
		if (ge.type == GameEntityType.STATION) {
			Station s = (Station) ge;
			shapeRenderer.setColor(s.player.color);
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
		// TODO
		// System.out.println("Click " + screenX + "," + screenY);
		// target = new Vector2(screenX, screenY);
		// selection rectangle
		if (button == 0) {
			if (!startSelect) {
				startSelect = true;
				// un-made selection
				entities.stream().filter(e -> e.type == GameEntityType.DRONE).map(d -> (Drone) d)
						.filter(d -> d.player.name == one.name).forEach(d -> d.selected = false);
				selection.set(screenX, screenY, 0, 0);
			} else {
				startSelect = false;
				// make selection
				List<Drone> selectedDrones = entities.stream().filter(e -> e.type == GameEntityType.DRONE)
						.map(d -> (Drone) d).filter(d -> d.player.name == one.name).filter(d -> d.isInside(selection))
						.collect(Collectors.toList());
				if (!selectedDrones.isEmpty()) {
					System.out.println();
					for (Drone drone : selectedDrones) {
						drone.selected = true;
					}
				}
			}
		}
		if (button == 1) {
			// for all selected drones, set target as point
			entities.stream().filter(e -> e.type == GameEntityType.DRONE).map(d -> (Drone) d)
					.filter(d -> d.player.name == one.name).filter(d -> d.selected)
					.forEach(d -> d.setTarget(screenX, screenY));
			;
		}
		// Optional<Station> o = entities.stream().filter(e -> e.type ==
		// GameEntityType.STATION).map(s -> (Station) s)
		// .filter(s -> s.inRange(screenX, screenY)).findFirst();
		// if (o.isPresent()) {
		// if (button == 0) {
		// // left click on station => select a station if from that player
		// Station s = o.get();
		// actionToAdd.add(new SelectStation(s, one));
		// }
		// return false;
		// }

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
