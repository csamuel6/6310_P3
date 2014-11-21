package EarthSim;

import java.util.Date;

public class QueryParameters {
	String name = "";
	private double orbit;
	private double tilt = 0;
	private int timeStep,length,gridSpacing = 0;
	private Date startDate, endDate; 
	private int lowerXCoordinate, upperXCoordinate, lowerYCoordinate, upperYCoordinate = 0;

	
	public int getLowerXCoordinate() {
		return lowerXCoordinate;
	}

	public void setLowerXCoordinate(int lowerXCoordinate) {
		this.lowerXCoordinate = lowerXCoordinate;
	}

	public int getUpperXCoordinate() {
		return upperXCoordinate;
	}

	public void setUpperXCoordinate(int upperXCoordinate) {
		this.upperXCoordinate = upperXCoordinate;
	}

	public int getLowerYCoordinate() {
		return lowerYCoordinate;
	}

	public void setLowerYCoordinate(int lowerYCoordinate) {
		this.lowerYCoordinate = lowerYCoordinate;
	}

	public int getUpperYCoordinate() {
		return upperYCoordinate;
	}

	public void setUpperYCoordinate(int upperYCoordinate) {
		this.upperYCoordinate = upperYCoordinate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getOrbit() {
		return orbit;
	}

	public void setOrbit(double orbit) {
		this.orbit = orbit;
	}

	public double getTilt() {
		return tilt;
	}

	public void setTilt(double tilt) {
		this.tilt = tilt;
	}

	public int getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(int timeStep) {
		this.timeStep = timeStep;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getGridSpacing() {
		return gridSpacing;
	}

	public void setGridSpacing(int gridSpacing) {
		this.gridSpacing = gridSpacing;
	}


}
