package EarthSim.main;



import java.util.List;
import java.util.concurrent.BlockingQueue;

import EarthSim.persistance.*;
import EarthSim.tools.StandAloneSimulation;

public class QueryInterfaceTest extends HeatedEarthSimulation {
	
	
	public QueryInterfaceTest (int gs, int interval,
			BlockingQueue<Message> queue) {
		super(gs, interval, 0, 0 , queue);

	}

	
	
	

	public static void main(String[] args) {
		
		
		
		
		
		DataManager dataManager = new DataManager();
		

		QueryParameters queryParameters = new QueryParameters();
		
		queryParameters.setName("50Geo");   //
		
		
		List results = dataManager.getSimulationByName(queryParameters);
		
		SimulationStorage simStored = null;
		
		if (results != null && results.size() > 0)
		{
			System.out.println("got results");
			
			simStored = (SimulationStorage) results.get(0);
		}
		
		
		QueryInterfaceTest simulation = new QueryInterfaceTest(simStored.getGridSpacing(), simStored.getTimeStep() , null);
		
		simStored.getGridCells();
		
		
		simulation.Initialize();
		
		int col = 360/simStored.getGridSpacing();
		int row = 180/simStored.getGridSpacing();
		
		for (GridCellStorage g : simStored.getGridCells())
		{
			
			for(int i = 0; i < gridcellsSurface1)
		}

		
		
		
		System.exit(0);
	}

}
