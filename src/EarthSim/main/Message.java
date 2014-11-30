package EarthSim.main;

public class Message {
	
	private double[][] grid;
	private Long sunsLongitude;
	private double sunsLatitude;
	private double heatingRatio;
	
	public Message(double[][] grid, Long sunsLongitude, double sunsLatitude, double heatingRatio) 
	{
		this.grid=grid;
		this.sunsLongitude=sunsLongitude;
		this.sunsLatitude = sunsLatitude;
	}
	
	public double[][] getGrid() 
	{
		return grid;
	}
	
	public void setGrid(double[][] grid) 
	{
		this.grid = grid;
	}
	
	public Long getSunsLongitude() 
	{
		return sunsLongitude;
	}
	
	public void setSunsLongitude(Long sunsLongitude) 
	{
		this.sunsLongitude = sunsLongitude;
	}
	
	public double getSunsLatitude() 
	{
		return sunsLatitude;
	}
	
	public void setSunsLatitude(double sunsLatitude) 
	{
		this.sunsLatitude = sunsLatitude;
	}

	/**
	 * @return the heatingRatio
	 */
	public double getHeatingRatio() {
		return heatingRatio;
	}

	/**
	 * @param heatingRatio the heatingRatio to set
	 */
	public void setHeatingRatio(double heatingRatio) {
		this.heatingRatio = heatingRatio;
	}

}
