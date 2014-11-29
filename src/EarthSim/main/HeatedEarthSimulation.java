package EarthSim.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import javax.annotation.Generated;
import javax.persistence.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import EarthSim.persistance.DataManager;
import EarthSim.persistance.GridCellStorage;
import EarthSim.persistance.QueryParameters;
import EarthSim.persistance.SimulationStorage;

public class HeatedEarthSimulation implements Runnable {
	public HeatedEarthSimulation() {}

	protected static GridCell[][] gridcellsSurface1;
	protected static GridCell[][] gridcellsSurface2;

	private BlockingQueue<Message> queue;
	static int timeInterval = 0;
	static int timeOfDay = 720;
	private int elapsedMinutes = 0;
	private HeatedEarthPresentation presentation = null;
	private DataManager dataManager = new DataManager();
	
	protected static EarthRepresentation earthRepresentation;
	// GridCell gc;
	int gridSize;
	private boolean running; // copied this from TestSimulator
	private boolean paused;
	private int statsTimer = 0;
	ArrayList<Long> waitList = new ArrayList<Long>();
	ArrayList<Long> calcTimeList = new ArrayList<Long>();
	private final static Logger LOGGER = Logger
			.getLogger(HeatedEarthSimulation.class.getName());
	private String Name;
	Calendar calendar;
	private double orbit = 0;
	private int length = 0;
	private double tilt = 23;
	double eccentricity = 0.0167;
	SimulationStorage simulation;
    private int dataPrecision = HeatedEarthGUI.getinstance().getDataPrecision();
    private int geographicalPrecision = HeatedEarthGUI.getinstance().getGeographicalPrecision();
    
    
    
	public HeatedEarthSimulation (int gs, int interval, double orbit,
			double tilt,  BlockingQueue<Message> queue) {
		this.queue = queue;
		this.gridSize = gs;
		timeInterval = interval;
		this.orbit = orbit;
		this.tilt = tilt;
		this.eccentricity = orbit;
		calendar = Calendar.getInstance();
		calendar.set(2014, Calendar.JANUARY, 4);
		System.out.println("tilt " + tilt);

		earthRepresentation = new EarthRepresentation(gs, interval, orbit, tilt, eccentricity);
		gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation
				.getCols()];
		gridcellsSurface2 = new GridCell[earthRepresentation.getRows()][earthRepresentation
				.getCols()];

	}
	public int getGridSize() {
		return gridSize;
	}
	
	public void setGridSize(Integer size) {
		this.gridSize = size;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void reset() {

		earthRepresentation = new EarthRepresentation(gridSize, timeInterval,
				orbit, tilt, eccentricity);
		gridcellsSurface1 = new GridCell[earthRepresentation.getRows()][earthRepresentation
				.getCols()];
		gridcellsSurface2 = new GridCell[earthRepresentation.getRows()][earthRepresentation
				.getCols()];
		timeOfDay = 720;
		Initialize();
		running = true;
		paused = false;
	}

	// Initialize GridCells.
	public void Initialize() {

		for (int i = 0; i < earthRepresentation.getRows(); i++) {
			for (int j = 0; j < earthRepresentation.getCols(); j++) {
				setGridData(gridcellsSurface1, i, j);
				setGridData(gridcellsSurface2, i, j);
			}
		}

		for (int i = 0; i < earthRepresentation.getRows(); i++) {
			for (int j = 0; j < earthRepresentation.getCols(); j++) {
				setNeighborsData(gridcellsSurface1, i, j);
				setNeighborsData(gridcellsSurface2, i, j);
			}
		}

	}

	public void printRunningInfo() {
		Long totalTime = 0L;
		int count = 0;
		if (!waitList.isEmpty()) {
			for (Long waitTime : waitList) {
				count++;
				totalTime += waitTime;
			}
			Long averageWait = totalTime / count;
			System.out.println("Average simulation idle time: " + averageWait
					+ " ms");
		}
		totalTime = 0L;
		count = 0;
		if (!calcTimeList.isEmpty()) {
			for (Long waitTime : calcTimeList) {
				count++;
				totalTime += waitTime;
			}
			Long averageWait = totalTime / count;
			System.out.println("Average simulation Calculation time: "
					+ averageWait + " ms");
		}
	}

	private void setNeighborsData(GridCell[][] grid, int i, int j) {
		GridCell cell = grid[i][j];

		cell.setNeighbors(grid);
	}

	private void setGridData(GridCell[][] grid, int i, int j) {

		GridCell cell = new GridCell();

		cell.setLatitude(earthRepresentation.getOriginLatitude(i));
		cell.setCenterLatitude(earthRepresentation.getOriginLatitude(i)
				+ (gridSize / 2));
		cell.setLongtitude(earthRepresentation.getOriginLongtitude(j));
		cell.setLt(earthRepresentation.calcCTop(i));
		cell.setLb(earthRepresentation.calcCBase(i));
		cell.setLv(earthRepresentation.calcCVerticalSide());
		cell.setArea(earthRepresentation.calcCArea(i));
		cell.setPerimeter(earthRepresentation.calcCPerimeter(i));
		cell.setH(earthRepresentation.calcCHeight(i));
		cell.setProportion(earthRepresentation.calcCSurfaceArea(i));
		cell.setxCoordinate(i);
		cell.setyCoordinate(j);
		cell.setTemp(288);
		grid[i][j] = cell;

	}

	public void setPresentation(HeatedEarthPresentation p) {
		presentation = p;
	}

	public void update() {
		this.rotateEarth();
	}

	public void rotateEarth() {

		Long beforeCalc = (new Date()).getTime();
		long currentSunLocation = SunRepresentation.sunLocation;
		SunRepresentation.sunLatitude = earthRepresentation.getEarthsTilt();

		this.diffuse(gridcellsSurface1, gridcellsSurface2);
		calcTimeList.add((new Date()).getTime() - beforeCalc);

		try {
			Long before = (new Date()).getTime();
			queue.put(new Message(prepareOutput(gridcellsSurface2),
					currentSunLocation, earthRepresentation.getEarthsTilt()));
			waitList.add((new Date()).getTime() - before);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GridCell[][] temp = gridcellsSurface1;

		gridcellsSurface1 = gridcellsSurface2;
		gridcellsSurface2 = temp;
		temp = null;

		statsTimer++;
		if (statsTimer == 1400) {
			// LOGGER.log(Level.INFO, Analyzer.getMemoryStats());
			statsTimer = 0;
		}

	}

	@Override
	public void run() {

		running = true;
		paused = false;

		simulation = new SimulationStorage(this);		
		simulation.setCreateDate(calendar.getTime());
		simulation.setTime(timeInterval);
		simulation.setGeoPrecision(this.geographicalPrecision);
		simulation.setTemporalPrecision(HeatedEarthGUI.getinstance().getTemporalPrecision());
		
		dataManager.setSimulation(simulation);
		dataManager.storeSimulation();
		
		double percentageToStore = HeatedEarthGUI.getinstance().getTemporalPrecision()/100d;
		double numberToStore =  percentageToStore * 10;
		double numberNotToStore =  10 - numberToStore;
		
		double numberOfIterations =  (numberToStore / numberNotToStore);  // 80       8/2  - 4
		
		double numIterationRev =  (numberNotToStore/numberToStore);    // 20   8/2  - 4
		
		int count = 0;
		
		while (running) {
			while (!paused) {
				this.rotateEarth();			

				List<GridCellStorage> gridCells = createGridStorageCells(gridcellsSurface1, simulation,calendar.getTime());
				
				
				if (numberToStore >= 5)
				{
				
					if (count < numberOfIterations)
					{
						System.out.println(" count less  " );
						
						simulation.setGridCells(gridCells);
						dataManager.storeSimulationCells();
						count++;
					}
					else
					{
						
						System.out.println(" count more  " );
						count = 0;
					}
					
				}
				else
				{
					
					if (count < numIterationRev)
					{
						System.out.println(" count less  " );
						count++;
					}
					else
					{
						simulation.setGridCells(gridCells);
						dataManager.storeSimulationCells();
						
						System.out.println(" count more  " );
						count = 0;
					}
					
					
					
					
					
				}
				
				
				calendar.add(Calendar.HOUR, timeInterval);
				if (presentation != null) {
					System.out.println("Simulation update");
					presentation.update();
				}
			}
		}
	}

	private List<GridCellStorage> createGridStorageCells(GridCell[][] grid, SimulationStorage methodSimulation, Date currentTime) {
		//GridCellStorage[][] gridCellList = new GridCellStorage[earthRepresentation.getRows()][earthRepresentation.getCols()];
		List<GridCellStorage> gridCell = new ArrayList<GridCellStorage>();
		for (int i = 0; i < earthRepresentation.getRows(); i++) {
			for (int j = 0; j < earthRepresentation.getCols(); j++) {
				GridCellStorage gricCellStorage = new GridCellStorage(grid[i][j]);
				
				gricCellStorage.setTime(currentTime);
				gricCellStorage.setStorage(methodSimulation);
				gridCell.add(gricCellStorage);
			}
		}

		return gridCell;
	}
	
	private double[][] prepareOutput(GridCell[][] grid) {

		double[][] gridOutput = new double[earthRepresentation.getRows()][earthRepresentation
				.getCols()];

		for (int i = 0; i < earthRepresentation.getRows(); i++) {
			for (int j = 0; j < earthRepresentation.getCols(); j++) {

				gridOutput[i][j] = grid[i][j].getTemp();

			}
		}

		return gridOutput;

	}

	protected void diffuse(GridCell[][] grid1, GridCell[][] grid2) {

		earthRepresentation.calculateAverageTemperature(grid1);
		earthRepresentation.setCurrentDay(calendar);
		
		for (int i = 0; i < earthRepresentation.getRows(); i++) {
			for (int j = 0; j < earthRepresentation.getCols(); j++) {

				grid2[i][j].setTemp(earthRepresentation
						.calculateCellTemperature(grid1[i][j]));

			}
		}

		// advance sun according to interval
		timeOfDay = (timeOfDay + timeInterval) % 1440;
		SunRepresentation.sunLocation = 180 - (timeOfDay / 4);
		calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
	}

	// copied this from TestSimulator
	public void setRunning(boolean running) {
		this.running = running;
	}

	public void setTimeStep(Integer interval) {
		timeInterval = interval;

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

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
