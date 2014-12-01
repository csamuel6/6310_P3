package EarthSim.persistance;

import java.util.Date;

public class QueryParameters {
	String name = "";
	private double orbit;
	private double tilt = 0;
	private Integer timeStep,length,gridSpacing = 0;
	private Date startDate, endDate; 
	private Integer lowerLatitude, upperLatitude, lowerLongitude, upperLongitude = 0;

	
	public int getLowerLatitude() {
		return lowerLatitude;
	}

	public void setLowerLatitude(int latitude) {
		this.lowerLatitude = latitude;
	}

	public int getUpperLatitude() {
		return upperLatitude;
	}

	public void setUpperLatitude(int latitude) {
		this.upperLatitude = latitude;
	}

	public int getLowerLongitude() {
		return lowerLongitude;
	}

	public void setLowerLongitude(int longitude) {
		this.lowerLongitude = longitude;
	}

	public int getUpperLongitude() {
		return upperLongitude;
	}

	public void setUpperLongitude(int longitude) {
		this.upperLongitude = longitude;
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
}
