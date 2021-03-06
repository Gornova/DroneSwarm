package it.randomtower.droneswarm;

import java.io.File;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;

public final class G {

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;

	public static final int NEUTRAL = 0;
	public static final int PLAYER_ONE = 1;
	public static final int PLAYER_TWO = 2;
	public static final int PLAYER_THREE = 3;

	public static final Color LIGHT_RED = new Color(0.25f, 0, 0, 0.1f);
	public static final Color LIGHT_BLUE = new Color(0, 0, 0.25f, 0.1f);
	public static final Color LIGHT_GRAY = new Color(150f, 150f, 150f, 0.1f);
	public static final Color GREEN = Color.valueOf("#48a70b");
	public static final Color ORANGE = Color.valueOf("#ffdb00");
	public static final Color RED = Color.valueOf("#ea1b1b");

	public static final int STATION_MAX_DRONES = 50;
	public static final int AI_RE_THINK_TIMER = 2000;

	public static final int DRONE_RADIUS = 10;
	public static final float DRONE_MOVE_TIME = 250;

	public static final Random rnd = new Random();
	public static final int TOTAL_LEVELS = 10;
	public static final boolean DEBUG_LEVELS = false;
	public static final boolean DEBUG = true;
	public static int unlockedLevel = 1;

	private static final String USER_HOME = System.getProperty("user.home");

	public static final String GAME_HOME = G.USER_HOME + File.separator + "DroneSwarm";
	public static boolean HOME_OK = false;
	public static final String UNLOCKED_LEVELS_FILE = "unlocked.levels";

}
