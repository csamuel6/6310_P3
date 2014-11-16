package EarthSim;

public class Message {
	
	private double[][] grid;
	private Long sunsLongitude;
	private double sunsLatitude;
	
	public Message(double[][] grid, Long sunsLongitude, double sunsLatitude) 
	{
		this.grid=grid;
		this.sunsLongitude=sunsLongitude;
		this.sunsLatitude = sunsLatitude * -1;
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

}
