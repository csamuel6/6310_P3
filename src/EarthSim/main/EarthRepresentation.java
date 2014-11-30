package EarthSim.main;

import java.util.Calendar;

public class EarthRepresentation {
	
	
	private static int eradius = 6371000; // in meters
	private static double esurfaceArea = 5.10072E14;
	private static double esurfaceAreaSummed = 0; //because SUM of area of trapezoids != esurfaceArea  !!
	private static double ecircumference =40030140;
	private static double eAreaVisibleToSun = 2.55036E14;
	private int timeInterval;
	private int gs;
	private int p; //proportion of equator used by one unit of gs
	private double earthsTilt = 23.44;
	
	private double averageTemperature = 288;
	
	//Variables for Orbit Calculation
	//Set to defaults for Earth
	private double eccentricity = 0.0167; //Value is (0,1] 
	private double obliquity = 23.44; //Value (-180, 180)
	private double perihelion = 147094000;
	private double semiMajorAxis = 149600000;
	private double argumentOfPeriapsis = 114;
	private int rotationalPeriod = 1440; //minutes
	private int orbitalPeriod = 525600; //minutes
	private Calendar day;
	private double heatingRatio = 1.0;
	private double distanceFromCenter = 0.0;
	private double semiMinorAxis = 0.0;
	
	//add method to get grid proportion to the equator
	
	double sunLocation = 0;
	private double x;
	private double y;
	
	public EarthRepresentation (int gridSpacing, int interval, double orbit, double tilt, double eccentricity)
	{
		this.gs= gridSpacing;
		this.timeInterval = interval;
		p = 360/gs;
		this.earthsTilt = tilt;
		this.eccentricity = eccentricity;
	}
	public int getCols()
	{
		return 360/gs;
	}
	
	public int getRows()
	{
		return 180/gs;
	}
	
	public int getNumberOfCells()
	{
	   return getRows()*getCols();
	   
	}
	public int getGS()
	{
		return this.gs;
	}
	public int getTimeInterval()
	{
		return this.timeInterval;
	}
	
	

	//Origin of a cell is the lower left hand corner. 
	public double getOriginLatitude(int row)
	{

		double oLat = 0;

		oLat = (row-(getRows()/2))*gs;
				
		return oLat;
		
	}
	
	
	public double getOriginLongtitude(int col)
	{

		double initialSpace = gs/2;
		
		double oLong = 0;

		oLong = (initialSpace + (gs* col) ) - 180;

		return oLong;
		
	}
	
	//calculate cell's vertical side lv
	public double calcCVerticalSide()
	{
		return ecircumference/p;
	}
	
	//calculate cell's base lb
	public double calcCBase(int row)
	{
		return Math.cos(Math.toRadians(getOriginLatitude(row)))*calcCVerticalSide();
	}
	//calculate cell's top side
	public double calcCTop(int row)
	{
		return Math.cos(Math.toRadians(getOriginLatitude(row) +gs))*calcCVerticalSide();
	}
	
	//calculate altitude of the cell
	public double calcCHeight(int row)
	{
		double lv = calcCVerticalSide();
		double lb = calcCBase(row);
		double lt = calcCTop(row);
		return Math.sqrt(Math.pow(lv,2) - Math.pow((lb - lt),2)/4.0);
	}
	
	//perimeter of a cell
	public double calcCPerimeter(int row)
	{
		double lv = calcCVerticalSide();
		double lb = calcCBase(row);
		double lt = calcCTop(row);
		return lt+lb+(2*lv);
	}
	
	//area of a cell
	public double calcCArea(int row)
	{
		double lb = calcCBase(row);
		double lt = calcCTop(row);
		double h = calcCHeight(row);
		return ((lt+lb)/2)*h;
	}
	
	//proportion of earth's surface area taken by a cell
	public double calcCSurfaceArea(int row)
	{
		return calcCArea(row)/getSummedArea();
	}
	
	public double getArea()
	{
		//use summed value when we need it
		//return esurfaceArea;
		return getSummedArea();
		
	}
	
	public double getSummedArea()
	{
		//calculate and store this on first use
		if( esurfaceAreaSummed == 0)
		{
			double entireColumnArea = 0;
			for (int i = 0; i < getRows();i++)
			{
				entireColumnArea += calcCArea(i);
			}
			//now multiply by the number of columns
			esurfaceAreaSummed = entireColumnArea * getCols();
		}
		return esurfaceAreaSummed;
	}
	
	public int getXCoordinate(int col)
	{
		int cols = getCols();
		return (int) ((col+(cols/2))%cols-((cols/2)-1)*(ecircumference/cols));
	}
	
	public int getYCoordinate(int row)
	{
		int rows = getRows();
		return (int) ((row-(rows/2))*(ecircumference/rows));
	}
	
	
	
	
	//Methods return Latitude - Longtitude mapping to Grid indices
	public double getGridRowIndex()
	{
		return 0;
		
	}
	
	//Identify cell location based on rotation.
	
	
	
	//TODO : Add code for solar heating, cooling , attenuation.
	
	public void calculateAverageTemperature(GridCell [][] grid)
	{
		
		double totalCellTemp = 0; 
		
		for (int i =0; i<this.getRows(); i++)
		{
			
			for(int j=0; j<this.getCols(); j++)
			{		
				totalCellTemp = totalCellTemp + grid[i][j].getTemp();
			}
		}
		
		this.averageTemperature = totalCellTemp / this.getNumberOfCells();
		
	}
	
	public double getAverageTemperature()
	{
		return this.averageTemperature;
	}
	
	public double calculateCellTemperature(GridCell cell)
	{
		calculateDistance();
		double initialTemperature = cell.getTemp();
		double temperatureDueToSun = SunRepresentation.calculateTemperatureDueToSun(cell, this, heatingRatio);
		double temperatureDueToCooling = SunRepresentation.calculateTemperatureDueToCooling(cell, this, heatingRatio);
		
		//double temperateCooledPerHour = 23.16;//TODO - Kelly - where does this number come from?
		//double timePassed = 1;//TODO - Kelly - where does this number come from? Should it be timeInterval?
		
		//double percentageOfCooling = temperatureDueToCooling / initialTemperature;
		
		//double actualCooling = percentageOfCooling * temperateCooledPerHour * timePassed;
		
		double temperatureOfNeighbors = cell.getNeighborsAverageTemp();
		
		double cellTemperature = initialTemperature;
		
		if ( temperatureDueToSun != 0.0 )
		{
			cellTemperature = (cellTemperature + temperatureDueToSun); // / 2;//avg current temp with sun cause temp
		}
		
		if (Double.isNaN(cellTemperature) )
		{
			
			System.out.println("nan sun ");
			
		}
		
		
		cellTemperature = cellTemperature + temperatureDueToCooling;
			
		cellTemperature = (cellTemperature +  temperatureOfNeighbors) /2;

		if (Double.isNaN(cellTemperature) )
		{
			
			System.out.println("nan final ");
			
		}
		return cellTemperature;
	}
	
	public double getEarthsTilt() {
		return earthsTilt;
	}
	
	public void setEarthsTilt(double earthsTilt) {
		this.earthsTilt = earthsTilt;
	}

	/*
	 * Orbital Methods and Constants
	 * 
	 * Defaults:
	 * Eccentricity = 0.0167 (Earth eccentricity)
	 * 
	 * Solving for distance from the sun
	 * 
	 * Perihelion - when planet is at closest orbital position to the sun
	 * 
	 */
	
	/*
	 * Process to find orbital position:
	 * Find time since last perihelion (0,T] where T is the period of the planet
	 * Need to calculate mean anomaly, eccentric anomaly, and the true anomaly
	 */
	
	/**
	 * @return the eccentricity
	 */
	public double getEccentricity() {
		return eccentricity;
	}
	/**
	 * @param eccentricity the eccentricity to set
	 */
	public void setEccentricity(double eccentricity) {
		this.eccentricity = eccentricity;
	}
	
	public void setCurrentDay(Calendar day){
		this.day = day;
	}
	
	public void calculateDistanceFromCenter(){
		distanceFromCenter = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		heatingRatio = semiMajorAxis/distanceFromCenter;
	}
	
	private void calculateSemiMinorAxis(){
		this.semiMinorAxis = Math.sqrt((semiMajorAxis * semiMajorAxis) * (1 - Math.pow(eccentricity, 2))); 
	}
	
	public double meanAnomaly(double currentTime, double orbitalPeriod){
		return 2 * Math.PI * (currentTime/orbitalPeriod);
	}
	
	/*
	 * Calculate Eccentricy
	 * Takes in two doubles:
	 * Double a: The length of the semi-major axis
	 * Double b: The length of the semi-minor axis
	 */
	
	public double calculateEccentricity(double a, double b){
		return (Math.sqrt(Math.abs(Math.pow(a, 2) - Math.pow(b, 2))) / Math.max(a, b));
	}
	
	
	public double eccentricAnomaly(double meanAnomaly, double eccentricity, int accuracy){
		int maxIteration = 30, iteration = 0;
		double delta = Math.pow(10, -accuracy);
		
		double E, F;
		meanAnomaly = meanAnomaly/(2*Math.PI);
		meanAnomaly = 2.0 * Math.PI * (meanAnomaly-Math.floor(meanAnomaly));
				
		if(eccentricity < 0.8)
			E = meanAnomaly;
		else
			E = Math.PI;
		
		F = E - eccentricity * Math.sin(meanAnomaly) - meanAnomaly;
		
		while((Math.abs(F) > delta) && (iteration < maxIteration)){
			E = E - F / (1.0 - eccentricity * Math.cos(E));
			F = E - eccentricity * Math.sin(E) - meanAnomaly;
			iteration++;
		}
		
		return Math.round(E * Math.pow(10, accuracy))/Math.pow(10, accuracy);
	}
	
	public double trueAnomaly(double eccentricity, double eAnomaly, int accuracy){
		double S = Math.sin(eAnomaly);
		double C = Math.cos(eAnomaly);
		
		double fak = Math.sqrt(1.0-eccentricity*eccentricity);
		double phi = Math.atan2(fak * S, C - eccentricity);
		return Math.round(phi * Math.pow(10, accuracy)) / Math.pow(10, accuracy);
	}
	
	/*
	 * calculatePosition
	 * Finds x and y position
	 */
	public void calculatePosition(double semiMajorAxis, double eccentricity, double eccAnomaly){
		double C = Math.cos(eccAnomaly);
		double S = Math.sin(eccAnomaly);
		
		this.x = semiMajorAxis * (C - eccentricity);
		this.y = semiMajorAxis * Math.sqrt(1.0 - eccentricity * eccentricity) * S;
	}
	

	public double calculateRadius(double semiMajorAxis, double eccentricity, double trueAnomaly){
		return semiMajorAxis * ((1 - Math.pow(eccentricity,2)) / (1 + eccentricity * Math.cos(trueAnomaly)));
	}

	public void calculateDistance(){
		//Julian Day
		double A = Math.floor(day.YEAR/100);
		A = 2- A + Math.floor(A/4);
		
		double julianDay = Math.floor(365.25 * (day.YEAR + 4716)) + Math.floor(30.6001 * (day.MONTH + 1)) + day.DAY_OF_WEEK_IN_MONTH + A - 1524.5;
		julianDay = Math.round(julianDay*1000000)/1000000;
		
		//Angles
		double dr = Math.PI/180;
		double T = (julianDay - 2451545.0)/36525.0;
		double M = 357.52910 + 35999.05030 * T - 0.0001559*T*T-0.00000048*T*T*T;
		double M_rad = M * dr;
		double e = eccentricity-0.000042037*T-0.0000001236*T*T;
		double C = (1.914600-0.004817*T-0.000014*T*T)*Math.sin(M_rad)
			    +(0.019993-0.000101*T)*Math.sin(2.*M_rad)+0.000290*Math.sin(3.*M_rad);
		double f = M_rad + C*dr;
		
		//Distance (AUs)
		double distanceAU = 1.000001018*(1-e*e)/(1+e*Math.cos(f));
		heatingRatio = distanceAU;
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
