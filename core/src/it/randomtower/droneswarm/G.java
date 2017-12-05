package it.randomtower.droneswarm;

import com.badlogic.gdx.graphics.Color;

public final class G {

	public static final int NEUTRAL = 0;
	public static final int PLAYER_ONE = 1;
	public static final int PLAYER_TWO = 2;

	public static final Color LIGHT_RED = new Color(0.25f, 0, 0, 0.1f);
	public static final Color LIGHT_BLUE = new Color(0, 0, 0.25f, 0.1f);
	public static final Color LIGHT_GRAY = new Color(150f, 150f, 150f, 0.1f);

	public static final int STATION_MAX_DRONES = 50;
	public static final int AI_RE_THINK_TIMER = 2000;

	public static final int DRONE_RADIUS = 20;
	public static final float DRONE_MOVE_TIME = 250;

}
