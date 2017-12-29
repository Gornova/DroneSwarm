package it.randomtower.droneswarm.level;

public class StationLevel {

	private int x;
	private int y;
	private int player;
	private boolean startingAi;

	public StationLevel() {
	}

	public StationLevel(int x, int y, int player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public boolean isStartingAi() {
		return startingAi;
	}

	public void setStartingAi(boolean startingAi) {
		this.startingAi = startingAi;
	}

}
