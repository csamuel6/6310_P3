package EarthSim.main;

public class GridCell
{
	private GridCell west;
    private GridCell east;
    private GridCell north;
    private GridCell south;
    private double temp = 0.00;
    private double prevTemp = 0.00;
    private  double latitude, longtitude, ctrLatitude;
    private double lv,lb, lt, h, perimeter, area;
    private double proportion;
    private int xCoordinate, yCoordinate;
    
    
    /** Add constructor getters and setters **/
    
    public GridCell ()
    {
    	
    }


	public GridCell getWest() {
		return west;
	}


	public void setWest(GridCell west) {
		this.west = west;
	}


	public GridCell getEast() {
		return east;
	}


	public void setEast(GridCell east) {
		this.east = east;
	}


	public GridCell getNorth() {
		return north;
	}


	public void setNorth(GridCell north) {
		this.north = north;
	}


	public GridCell getSouth() {
		return south;
	}


	public void setSouth(GridCell south) {
		this.south = south;
	}


	public double getTemp() {
		return temp;
	}


	public void setTemp(double temp) {
		this.temp = temp;
	}


	public double getPrevTemp() {
		return prevTemp;
	}


	public void setPrevTemp(double prevTemp) {
		this.prevTemp = prevTemp;
	}


	public double getLatitude() {
		return latitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getCenterLatitude() {
		return ctrLatitude;
	}


	public void setCenterLatitude(double latitude) {
		this.ctrLatitude = latitude;
	}


	public double getLongtitude() {
		return longtitude;
	}


	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}


	public double getLv() {
		return lv;
	}


	public void setLv(double lv) {
		this.lv = lv;
	}


	public double getLb() {
		return lb;
	}


	public void setLb(double lb) {
		this.lb = lb;
	}


	public double getLt() {
		return lt;
	}


	public void setLt(double lt) {
		this.lt = lt;
	}


	public double getH() {
		return h;
	}


	public void setH(double h) {
		this.h = h;
	}


	public double getPerimeter() {
		return perimeter;
	}


	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}


	public double getArea() {
		return area;
	}


	public void setArea(double area) {
		this.area = area;
	}


	public double getProportion() {
		return proportion;
	}


	public void setProportion(double proportion) {
		this.proportion = proportion;
	}


	public int getxCoordinate() {
		return xCoordinate;
	}


	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}


	public int getyCoordinate() {
		return yCoordinate;
	}


	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}


	public void setNeighbors(GridCell[][] gridcellsSurface) {
		
		int rows = gridcellsSurface.length;
		int cols = gridcellsSurface[0].length;
		
		
		setNorthCell(gridcellsSurface, rows, cols);
		setSouthCell(gridcellsSurface, rows, cols);
		setEastCell(gridcellsSurface, rows, cols);
		setWestCell(gridcellsSurface, rows, cols);
		
		
	}
	
	private void setWestCell(GridCell[][] gridcellsSurface, int rows, int cols) 
	{
	
		int cellLocation = this.getyCoordinate() + 1;
		
		if ( cellLocation > cols - 1 )
		{
			cellLocation = 0;
		}
		
		this.setWest(gridcellsSurface[this.getxCoordinate()][cellLocation]);
	}
	
	
	private void setEastCell(GridCell[][] gridcellsSurface, int rows, int cols) 
	{
	
		int cellLocation = this.getyCoordinate() - 1;
		
		if ( cellLocation < 0 )
		{
			cellLocation = cols - 1;
		}
		
		this.setEast(gridcellsSurface[this.getxCoordinate()][cellLocation]);
	}
	
	private void setSouthCell(GridCell[][] gridcellsSurface, int rows, int cols) 
	{
	
		int southCellLocation = this.getxCoordinate() + 1;
		
		if ( southCellLocation > rows - 1 )
		{
//			southCellLocation = 0;
			
			southCellLocation = cols - 1 - this.getyCoordinate();
			this.setSouth(gridcellsSurface[this.getxCoordinate()][southCellLocation]);
			
		}
		else 
		{
		
		this.setSouth(gridcellsSurface[southCellLocation][this.getyCoordinate()]);
		}
		
		
	}
    


	private void setNorthCell(GridCell[][] gridcellsSurface, int rows, int cols) 
	{
	
		int cellLocation = this.getxCoordinate() - 1;
		
		if ( cellLocation < 0 )
		{
		//	cellLocation = rows -1;
			cellLocation = cols - 1 - this.getyCoordinate();
			this.setNorth(gridcellsSurface[this.getxCoordinate()][cellLocation]);
		}
		else
		{
//		System.out.println(cellLocation + " nc  " + this.getyCoordinate());
		
		this.setNorth(gridcellsSurface[cellLocation][this.getyCoordinate()]);
		}
		
		
	}
	
	public double getNeighborsAverageTemp()
	{
		
		return (this.getNorth().getTemp() + this.getSouth().getTemp() + this.getEast().getTemp() + this.getWest().getTemp())/4.0 ;
		
	}
    
    
    
    
}
