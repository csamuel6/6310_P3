package EarthSim.persistance;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import EarthSim.main.GridCell;

@Entity
@IdClass(GridCellStoragePK.class)
@Embeddable
@Table(name = "CellData")
public class GridCellStorage {
	
	
	private double temperature = 0.00;
    private double latitude, longitude;
    private Date Time;
    private SimulationStorage storage;
    private int xCoordinate;
    private int yCoordinate;
    
	public GridCellStorage() {
    	
    }
	
    public GridCellStorage(GridCell gridCell) {
    	this.temperature = gridCell.getTemp();
    	this.latitude = gridCell.getLatitude();
    	this.longitude = gridCell.getLongtitude();
    	this.xCoordinate = gridCell.getxCoordinate();
    	this.yCoordinate = gridCell.getyCoordinate();
    }
    
    
    public GridCellStorage(double temp, double latitude, double longitude, Date date, SimulationStorage simulation) {
    	this.temperature = temp;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.Time = date;
    	this.storage = simulation;
    }
	
    @Id
    public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temp) {
		this.temperature = temp;
	}
	@Id
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	@Id
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Id
	public Date getTime() {
		return Time;
	}

	public void setTime(Date time) {
		this.Time = time;
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
	
	@Id
	@ManyToOne
	@JoinTable(name = "CellData")
	@JoinColumn(name="SimulationInfo_id")
	public SimulationStorage getStorage() {
		return storage;
	}

	public void setStorage(SimulationStorage storage) {
		this.storage = storage;
	}
}
