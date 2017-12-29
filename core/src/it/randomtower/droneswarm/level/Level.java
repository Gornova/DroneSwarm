package it.randomtower.droneswarm.level;

import java.util.ArrayList;
import java.util.List;

public class Level {

	private int number;
	private List<StationLevel> stations = new ArrayList<>();

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<StationLevel> getStations() {
		return stations;
	}

	public void setStations(List<StationLevel> stations) {
		this.stations = stations;
	}

}