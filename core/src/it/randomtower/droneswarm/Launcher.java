package it.randomtower.droneswarm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.ixeption.libgdx.transitions.FadingGame;
import com.ixeption.libgdx.transitions.impl.ColorFadeTransition;

import it.randomtower.droneswarm.screen.MenuScreen;

public class Launcher extends FadingGame {

	private ColorFadeTransition colorFadeInTransition;

	public Launcher() {
		Path gameHome = Paths.get(G.GAME_HOME);
		if (!Files.isDirectory(gameHome)) {
			try {
				Files.createDirectory(gameHome);
				G.HOME_OK = true;
			} catch (IOException e) {
				System.out.println("Failed game home directory creation " + gameHome.getFileName());
				G.HOME_OK = false;
			}
		}
		System.out.println("Home dir found : " + G.GAME_HOME);
		G.HOME_OK = true;
	}

	@Override
	public void create() {
		colorFadeInTransition = new ColorFadeTransition(Color.BLACK, Interpolation.exp10);
		setTransition(colorFadeInTransition, 1);
		batch = new SpriteBatch();

		Screen start = new ScreenAdapter();
		this.setScreen(start);
		this.setScreen(new MenuScreen(this));
	}

}
