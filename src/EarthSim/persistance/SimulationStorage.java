package EarthSim.persistance;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	private int simulationLength;
	private Date creationDate; 
	@Embedded
	@ElementCollection
	private List<GridCellStorage> gridCells = new ArrayList<GridCellStorage>();
	
	public SimulationStorage() {}
	
	public SimulationStorage(EarthSim.main.HeatedEarthSimulation heatedEarthSimulation) {
		name = heatedEarthSimulation.getName();
		axialTilt = heatedEarthSimulation.getTilt();
		eccentricity = heatedEarthSimulation.getOrbit();
		gridSpacing = heatedEarthSimulation.getGridSize();
		simulationLength = heatedEarthSimulation.getLength();
	}
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

	public int getSimulationLength() {
		return simulationLength;
	}

	public void setSimulationLength(int simulationLength) {
		this.simulationLength = simulationLength;
	}

	public List<GridCellStorage> getGridCells() {
		return gridCells;
	}

	public void setGridCells(List<GridCellStorage> gridCells) {
		this.gridCells = gridCells;
	}

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}

}
