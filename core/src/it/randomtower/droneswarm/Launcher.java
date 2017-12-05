package it.randomtower.droneswarm;

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
