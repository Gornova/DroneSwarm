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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import it.randomtower.droneswarm.G;

public class StoryScreen implements Screen {
	private Game game;
	private BitmapFont font;
	private SpriteBatch batch;
	private BitmapFont fontBig;
	private Texture menuBackground;
	private Stage stage;
	private OrthographicCamera cam;
	private StretchViewport viewport;
	private Skin skin;

	public StoryScreen(final Game game) {
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
		skin = new Skin(Gdx.files.internal("defaultSkin/uiskin.json"));

		final TextButton lb = new TextButton("Start", skin, "default");
		lb.setWidth(150);
		lb.setHeight(30);
		lb.setPosition(250, 50);
		lb.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				// load level
				game.setScreen(new LevelSelectScreen(game));
			};
		});
		stage.addActor(lb);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		batch.begin();

		batch.setProjectionMatrix(cam.combined); // Important

		batch.draw(menuBackground, 0, 0);
		font.draw(batch, "After many wars, galaxy is without organic life", 50, 400);
		font.draw(batch, "Many weapons are still active", 50, 350);
		fontBig.draw(batch, "weapons like you", 150, 275);
		font.draw(batch, "drones factories are still active", 50, 200);
		font.draw(batch, "so take control of drone swarm and fight!", 50, 150);
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
