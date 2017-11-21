package it.randomtower.droneswarm.action;

import it.randomtower.droneswarm.model.Player;
import it.randomtower.droneswarm.model.Station;

/**
 * Select a station, if controlled by player
 */
public class SelectStation implements GameAction {

	private Station station;
	private Player player;

	public SelectStation(Station t, Player p) {
		this.station = t;
		this.player = p;
	}

	@Override
	public boolean execute() {
		if (station.player.name == player.name) {
			station.selected = station.selected ? false : true;
		}
		return false;
	}

}
