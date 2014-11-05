package EarthSim;

public class Message {
	
	private double[][] grid;
	private Long sunsLongitude;
	public Message(double[][] grid, Long sunsLongitude) {
		this.grid=grid;
		this.sunsLongitude=sunsLongitude;
	}
	public double[][] getGrid() {
		return grid;
	}
	public void setGrid(double[][] grid) {
		this.grid = grid;
	}
	public Long getSunsLongitude() {
		return sunsLongitude;
	}
	public void setSunsLongitude(Long sunsLongitude) {
		this.sunsLongitude = sunsLongitude;
	}

}
