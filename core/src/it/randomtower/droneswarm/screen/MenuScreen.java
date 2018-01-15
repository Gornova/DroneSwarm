package it.randomtower.droneswarm.screen;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
import it.randomtower.droneswarm.level.LevelLoader;

public class MenuScreen implements Screen {

	private Game game;
	private BitmapFont font;
	private SpriteBatch batch;
	private BitmapFont fontBig;
	private Texture menuBackground;
	private Stage stage;
	private Skin skin;
	private OrthographicCamera cam;
	private StretchViewport viewport;

	public MenuScreen(final Game game) {

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
		lb.setPosition(250, 200);
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
		List<String> levels = new ArrayList<>();
		for (int i = 1; i <= G.TOTAL_LEVELS; i++) {
			levels.add("levels/" + i + ".json");
		}
		LevelLoader.loadLevels(levels);
	}

	@Override
	public void render(float delta) {
		batch.begin();

		batch.setProjectionMatrix(cam.combined); // Important

		batch.draw(menuBackground, 0, 0);
		fontBig.draw(batch, "Drone Swarm", 190, 420);
		font.draw(batch, "Control your drone swarm and conquest galaxy", 60, 370);
		font.draw(batch, "Drag with left mouse button to select", 130, 160);
		font.draw(batch, "and use right mouse button to attack", 130, 130);
		font.draw(batch, "Random tower of games - 2017", 150, 50);
		batch.end();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

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
