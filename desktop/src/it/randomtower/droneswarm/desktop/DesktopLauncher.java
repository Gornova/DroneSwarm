package it.randomtower.droneswarm.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import it.randomtower.droneswarm.Main;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drone Swarm";
		config.width = 640;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new Main(), config);
	}
}
