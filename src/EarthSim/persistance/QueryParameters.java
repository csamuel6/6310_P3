package EarthSim.persistance;

import java.util.Date;

public class QueryParameters {
	private String name;
	private Double orbit,tilt;
	private Date startDate, endDate; 
	private Integer lowerLatitude, upperLatitude, lowerLongitude, upperLongitude;

	
	public Integer getLowerLatitude() {
		return lowerLatitude;
	}

	public void setLowerLatitude(Integer latitude) {
		this.lowerLatitude = latitude;
	}

	public Integer getUpperLatitude() {
		return upperLatitude;
	}

	public void setUpperLatitude(Integer latitude) {
		this.upperLatitude = latitude;
	}

	public Integer getLowerLongitude() {
		return lowerLongitude;
	}

	public void setLowerLongitude(Integer longitude) {
		this.lowerLongitude = longitude;
	}

	public Integer getUpperLongitude() {
		return upperLongitude;
	}

	public void setUpperLongitude(Integer longitude) {
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

	public Double getOrbit() {
		return orbit;
	}

	public void setOrbit(Double orbit) {
		this.orbit = orbit;
	}

	public Double getTilt() {
		return tilt;
	}

	public void setTilt(Double tilt) {
		this.tilt = tilt;
	}
}
