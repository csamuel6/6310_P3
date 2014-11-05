package EarthSim;

import java.util.concurrent.BlockingQueue;

public class StandAloneSimulation extends HeatedEarthSimulation {


	
	public StandAloneSimulation(int gs, int interval,
			BlockingQueue<Message> queue) {
		super(gs, interval, queue);

	}

	
	public static void main(String[] args) {
	
		StandAloneSimulation sim = new StandAloneSimulation(15, 15, null);

		
		GridCell [][] temp;
		
		for (int k=0; k < 4*24*365; k++)
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
	}

}
