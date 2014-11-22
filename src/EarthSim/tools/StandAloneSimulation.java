package EarthSim.tools;

import java.util.concurrent.BlockingQueue;

import EarthSim.main.GridCell;
import EarthSim.main.HeatedEarthSimulation;
import EarthSim.main.Message;

public class StandAloneSimulation extends HeatedEarthSimulation {


	
	public StandAloneSimulation(int gs, int interval,
			BlockingQueue<Message> queue) {
		super(gs, interval, 0, 0 , queue);

	}

	
	public static void main(String[] args) {
	
		StandAloneSimulation sim = new StandAloneSimulation(15, 15, null);

		sim.Initialize();
		
		GridCell [][] temp;
		
//		for (int k=0; k < 4*24*365; k++)
		{
		//perform diffusion
			sim.diffuse(gridcellsSurface1, gridcellsSurface2);
			
		//swap out grids
		temp = gridcellsSurface1;
		gridcellsSurface1 = gridcellsSurface2;
		gridcellsSurface2 = temp;
		temp = null;
		}
		

		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			System.out.println(" ");
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				System.out.printf("%.12f ",gridcellsSurface1[i][j].getTemp() );
			}
		}
		
		System.out.println(" ");
		System.out.println(" ");
		
		//print grid coords
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			System.out.println(" ");
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				System.out.printf("% 6.1f, % 7.1f; ",gridcellsSurface1[i][j].getCenterLatitude() , gridcellsSurface1[i][j].getLongtitude() );
			}
		}
		
		System.out.println(" ");
		//print earths average temperature
		earthRepresentation.calculateAverageTemperature(gridcellsSurface1);
		System.out.println("Avg Temp" +earthRepresentation.getAverageTemperature());
		
		System.out.println(" ");
		System.out.println(" ");
		
		for (int i =0; i<earthRepresentation.getRows(); i++)
		{
			System.out.println(" ");
			for(int j=0; j<earthRepresentation.getCols(); j++)
			{		
				System.out.printf( "(" + gridcellsSurface1[i][j].getSouth().getxCoordinate() + ", " + gridcellsSurface1[i][j].getSouth().getyCoordinate() + ") " );
			}
		}
	}

}
