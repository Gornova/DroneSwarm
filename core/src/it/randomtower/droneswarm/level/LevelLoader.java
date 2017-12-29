package it.randomtower.droneswarm.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class LevelLoader {

	private static List<Level> levels = new ArrayList<>();

	public static final void loadLevels(String... files) throws IllegalArgumentException {
		if (files == null) {
			throw new IllegalArgumentException("provided argument should be not null or empty");
		}
		List<Level> result = new ArrayList<>();

		Json json = new Json();
		for (String file : files) {
			result.add(json.fromJson(Level.class, Gdx.files.internal(file)));
		}

		levels = result;
	}

	public static final Level get(int number) {
		return levels.stream().filter(l -> l.getNumber() == number).findFirst().get();
	}

}
