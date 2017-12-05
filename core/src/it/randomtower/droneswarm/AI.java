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

	public AI(Player player, Vector2 starting) {
		this.player = player;
		this.starting = starting;
	}

	public void update(List<GameEntity> stations) {
		timer += Gdx.graphics.getDeltaTime() * 1000;
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
			TreeMap<Object, Object> result = enemyStations.stream()
					.collect(Collectors.toMap(s -> s.distance(starting), s -> s, (v1, v2) -> v1, TreeMap::new));
			Station s = (Station) result.get(result.firstKey());
			target = s.getVector2();
		}
	}

	public void process(List<GameEntity> drones) {
		for (GameEntity drone : drones) {
			Drone d = (Drone) drone;
			if (d.state == State.WAIT) {
				d.setTarget(target.x, target.y);
			}
		}
	}

}
