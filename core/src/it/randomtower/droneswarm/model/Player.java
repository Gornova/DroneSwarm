package it.randomtower.droneswarm.model;

import com.badlogic.gdx.graphics.Color;

public class Player {

	public Color color;
	public Color colorStation;
	public int name;

	public Player(Color color, int name) {
		this.color = color;
		this.name = name;
		this.colorStation = new Color(color);
		this.colorStation.a = 0.5f;
	}

}
