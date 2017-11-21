package it.randomtower.droneswarm;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import it.randomtower.droneswarm.model.Drone;
import it.randomtower.droneswarm.model.GameEntity;
import it.randomtower.droneswarm.model.Player;
import it.randomtower.droneswarm.model.Station;

public final class GameEntityFactory {

	public static Station buildStation(int x, int y, Texture texture, int k, Player one, int l, int m) {
		return new Station(x, y, texture, k, one, l, m);
	}

	public static GameEntity createDrone(float x, float y, int radius, Player player, Texture img, Vector2 target) {
		Random rnd = new Random();
		int dx = rnd.nextInt(radius / 2);
		int dy = rnd.nextInt(radius / 2);
		int mx = rnd.nextBoolean() ? 1 : -1;
		int my = rnd.nextBoolean() ? 1 : -1;
		Drone de = new Drone(x + 10, y + 10, img, player, 10, 2);
		if (target == null) {
			de.setTarget(x + mx * dx, y + my * dy);
		}
		return de;
	}

}
