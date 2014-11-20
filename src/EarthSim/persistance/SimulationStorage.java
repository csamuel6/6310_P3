package EarthSim;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SimulationInfo")
public class SimulationStorage {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String name = "";

	private int gridSpacing;
	private double axialTilt;
	private double eccentricity;
	private int timeStep;
	private Date creationDate; 
	
	public SimulationStorage() {}
	
	public Date getCreateDate() {
		return creationDate;
	}
	public void setCreateDate(Date createDate) {
		this.creationDate = createDate;
	}
	@Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getGridSpacing() {
		return gridSpacing;
	}
	public void setGridSpacing(int gridSize) {
		this.gridSpacing = gridSize;
	}

//	public static List<GridCellStorage> getGrid() {
//		return grid;
//	}
//	
//	public static void setGrid(List<GridCellStorage> grid) {
//		SimulationStorage.grid = grid;
//	}
	
	public double getAxialTilt() {
		return axialTilt;
	}
	public void setAxialTilt(double axialTilt) {
		this.axialTilt = axialTilt;
	}
	public double getEccentricity() {
		return eccentricity;
	}
	public void setEccentricity(double eccentricity) {
		this.eccentricity = eccentricity;
	}
	public int getTimeStep() {
		return timeStep;
	}
	public void setTime(int date) {
		this.timeStep = date;
	}

}
