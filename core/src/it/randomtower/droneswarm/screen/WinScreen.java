package it.randomtower.droneswarm.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import it.randomtower.droneswarm.G;

public class WinScreen implements Screen {
	private Game game;
	private BitmapFont font;
	private SpriteBatch batch;
	private BitmapFont fontBig;
	private Texture menuBackground;
	private Stage stage;
	private OrthographicCamera cam;
	private StretchViewport viewport;

	public WinScreen(final Game game) {
		cam = new OrthographicCamera(G.WIDTH, G.HEIGHT);
		viewport = new StretchViewport(G.WIDTH, G.HEIGHT, cam);

		this.game = game;
		font = new BitmapFont(Gdx.files.internal("orbitron.fnt"), Gdx.files.internal("orbitron.png"), false);
		fontBig = new BitmapFont(Gdx.files.internal("orbitron_big_yellow.fnt"),
				Gdx.files.internal("orbitron_big_yellow.png"), false);
		batch = new SpriteBatch();
		menuBackground = new Texture(Gdx.files.internal("menu.png"));

		// ui
		stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		batch.begin();

		batch.setProjectionMatrix(cam.combined); // Important

		batch.draw(menuBackground, 0, 0);
		fontBig.draw(batch, "Drone Swarm", 190, 420);
		font.draw(batch, "You Win!", 270, 260);
		font.draw(batch, "Press ESC to continue", 200, 120);
		font.draw(batch, "Random tower of games - 2018", 150, 50);
		batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.setScreen(new MenuScreen(game));
		}
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
	}
}
