package EarthSim.tools;



import java.util.List;
import java.util.concurrent.BlockingQueue;

import EarthSim.main.GridCell;
import EarthSim.main.HeatedEarthSimulation;
import EarthSim.main.Message;
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
		
		
		List<SimulationStorage> results = dataManager.getSimulationByName(queryParameters);
		
		SimulationStorage simStored = null;
		
		if (results != null && results.size() > 0)
		{
			System.out.println("got results");
			
			simStored = (SimulationStorage) results.get(0);
		}
		
		
		QueryInterfaceTest simulation = new QueryInterfaceTest(simStored.getGridSpacing(), simStored.getTimeStep() , null);
		
		
		simulation.defaultTemp = 0;
		
		simulation.Initialize();
		
		int col = 360/simStored.getGridSpacing();
		int row = 180/simStored.getGridSpacing();
		
		for (GridCellStorage g : simStored.getGridCells())
		{
			
			for(int i = 0; i < row; i++)
			{
				for(int j = 0; j < col; j++)
				{
					if (g.getxCoordinate() == gridcellsSurface1[i][j].getxCoordinate() && g.getyCoordinate() == gridcellsSurface1[i][j].getyCoordinate() )
					{
						gridcellsSurface1[i][j].setTemp(Double.valueOf(g.getTemperature()));
						break;
					}
				}
				
			}
		}

		
		
		printGridCells(row, col);
		
		simulation.diffuse(gridcellsSurface1, gridcellsSurface2);
		

		GridCell[][] temp = gridcellsSurface1;
		
		gridcellsSurface1 = gridcellsSurface2;
		gridcellsSurface2 = temp;
		temp = null;
		
		
		printGridCells(row, col);

		
		
		
		System.exit(0);
	}
	
	public static void printGridCells(int row, int col)
	{
		
		System.out.println("**************************************************************************************");
		
		for (int i =0; i< row; i++)
		{
			System.out.println(" ");
			for(int j=0; j<col; j++)
			{		
				System.out.printf("%.12f ",gridcellsSurface1[i][j].getTemp() );
			}
		}
		
		
		
		for (int i =0; i<row; i++)
		{
			System.out.println(" ");
			for(int j=0; j<col; j++)
			{		
				System.out.printf("% 6.1f, % 7.1f; ",gridcellsSurface1[i][j].getCenterLatitude() , gridcellsSurface1[i][j].getLongtitude() );
			}
		}
	}

}
