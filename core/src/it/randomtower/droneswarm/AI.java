package it.randomtower.droneswarm;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import it.randomtower.droneswarm.model.Drone;
import it.randomtower.droneswarm.model.GameEntity;
import it.randomtower.droneswarm.model.Player;
import it.randomtower.droneswarm.model.State;
import it.randomtower.droneswarm.model.Station;

// simplest AI: try to conquer nearest enemy station from starting station
public class AI {

	public Vector2 target;
	public Player player;
	private Vector2 starting;
	private int timer;
	private int startingTimer;
	private int startingSleepTime;

	public AI(Player player, Vector2 starting, int startingSleepTime) {
		this.player = player;
		this.starting = starting;
		this.startingSleepTime = startingSleepTime;
	}

	public void update(List<GameEntity> stations) {
		startingTimer += Gdx.graphics.getDeltaTime() * 1000;
		if (startingTimer < startingSleepTime) {
			return;
		}
		if (timer > G.AI_RE_THINK_TIMER) {
			timer = 0;
			target = null;
		}
		List<GameEntity> enemyStations = stations.stream().filter(s -> s.player.name != player.name)
				.collect(Collectors.toList());
		if (enemyStations.isEmpty()) {
			System.out.println("No more enemy stations!");
			return;
		}
		if (target == null) {
			// with no target, select nearest
			TreeMap<Object, Object> result = enemyStations.stream()
					.collect(Collectors.toMap(s -> s.distance(starting), s -> s, (v1, v2) -> v1, TreeMap::new));
			Station s = (Station) result.get(result.firstKey());
			target = s.getVector2();
			System.out.println("Player " + player.name + " decided to attack station of " + s.player);
		} else {
			// is target conquered ?
			List<GameEntity> test = enemyStations.stream()
					.filter(e -> e.getVector2().x == target.x && e.getVector2().y == target.y)
					.collect(Collectors.toList());
			if (test.isEmpty()) {
				target = null;
				// warmup time after a conquest!
				startingTimer = 0;
			}
		}
	}

	public void process(List<GameEntity> drones) {
		for (GameEntity drone : drones) {
			Drone d = (Drone) drone;
			if (d.state == State.WAIT && target != null) {
				d.setTarget(target.x, target.y);
			}
		}
	}

}
