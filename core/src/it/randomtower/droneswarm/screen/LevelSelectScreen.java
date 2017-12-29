package it.randomtower.droneswarm.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

public class LevelSelectScreen implements Screen {

	private Game game;
	private BitmapFont font;
	private SpriteBatch batch;
	private Texture menuBackground;
	private Skin skin;
	private Stage stage;
	private OrthographicCamera cam;
	private StretchViewport viewport;

	public LevelSelectScreen(final Game game) {
		this.game = game;

		cam = new OrthographicCamera(640, 480);
		viewport = new StretchViewport(640, 480, cam);

		font = new BitmapFont(Gdx.files.internal("orbitron.fnt"), Gdx.files.internal("orbitron.png"), false);
		batch = new SpriteBatch();
		menuBackground = new Texture(Gdx.files.internal("menu.png"));

		// ui
		stage = new Stage(viewport) {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Keys.BACK) {
					Gdx.gl.glClearColor(133 / 255f, 133 / 255f, 133 / 255f, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					game.setScreen(new MenuScreen(game));
					return true;
				}
				return super.keyDown(keyCode);
			}
		};
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		skin = new Skin(Gdx.files.internal("defaultSkin/uiskin.json"));

		int dx = 0;
		int dy = 0;
		int i = 0;
		int j = 0;
		while (i < G.TOTAL_LEVELS) {
			i++;
			if (i > G.unlockedLevel) {
				break;
			}
			dy = 350 - j * 60;
			addButton(i, 30 + dx, dy);
			if (i % 5 == 0) {
				dx = 0;
				j++;
				dx = 0;
			} else {
				dx += 130;
			}
		}
	}

	private void addButton(final int level, int dx, int dy) {
		final TextButton lb = new TextButton("" + level, skin, "default");
		lb.setWidth(50);
		lb.setHeight(30);
		lb.setPosition(dx, dy);
		lb.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				// load level
				game.setScreen(new GameScreen(game, level));
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
		font.draw(batch, "Select level  ", 30, 450);
		// + G.unlockedLevel + " / " + G.TOTAL_LEVELS
		font.draw(batch, "Random tower of games - 2017", 150, 50);
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
