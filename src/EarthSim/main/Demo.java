package EarthSim.main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Demo
{

	private int gridSpacing; //1 to 180- input provided by the user
	private int timeInterval=1; //1 to 1440 - input provided by the user
	
	
	public static void main(String[] args) {
	
	//	 initializeLogging();
		 
		int blen = 0;
	    boolean  s, p, r, t, b, g; 
	    s = p = r = t = b = false; 
	    

//	    -p #: The precision of the data to be stored, in decimal digits after the decimal point. 
//	    The default is to use the number of digits storable in a normalized float variable. The maximum is 
//	    the number of digits storable in a normalized double variable. The minimum is zero.
//	    -g #: The geographic precision (sampling rate) of the temperature data to be stored, as an integer 
//	    percentage of the number of grid cells saved versus the number simulated. The default is 100%; that is, a value is stored for each grid cell.
//	    -t #: The temporal precision of the temperature data to be stored, as an integer percentage of the number 
//	    of time periods saved versus the number computed. The default is 100%; that is, all computed values should be stored.

	    
	    int dataPrecision = 2;
	    int geographicalPrecision = 100;
	    int temporalPrecision = 100;
	    
	   
		 for (int i = 0; i < args.length; i++) {
		        if (args[i].equalsIgnoreCase("-p")) 
		        {
		        	
		            try 
		            {
		            	dataPrecision = Integer.parseInt(args[i+1]);
		             
		            } 
		            catch (NumberFormatException e) 
		            {
		                System.out.println("Data Precision should be an integer between 0 and 20");
		                System.exit(1);
		            }
		            if (dataPrecision < 0 || dataPrecision > 20) 
		            {
		                System.out.println("Data Precision must be between 0 and 20");
		                System.exit(1);
		            }
		        	
		        } 
		        else if (args[i].equalsIgnoreCase("-t")) 
		        {
		      
		            temporalPrecision = Integer.parseInt(args[i+1]);
		            
		            try 
		            {
		            	temporalPrecision = Integer.parseInt(args[i+1]);
		             
		            } 
		            catch (NumberFormatException e) 
		            {
		                System.out.println("Temporal Precision should be an integer between 0 and 100");
		                System.exit(1);
		            }
		            if (temporalPrecision < 0 || temporalPrecision > 100) 
		            {
		                System.out.println("Temporal Precision must be between 0 and 100");
		                System.exit(1);
		            }
		            
		        } 
		        else if (args[i].equalsIgnoreCase("-g")) 
		        {
		            geographicalPrecision = Integer.parseInt(args[i+1]);
		            
		            
		            try 
		            {
		            	geographicalPrecision = Integer.parseInt(args[i+1]);
		          
		            } 
		            catch (NumberFormatException e) 
		            {
		                System.out.println("Geographical Precision should be an integer between 0 and 100");
		                System.exit(1);
		            }
		            if (geographicalPrecision < 0 || geographicalPrecision > 100) 
		            {
		                System.out.println("Geographical Precision should be an integer between 0 and 100");
		                System.exit(1);
		            }
		            
		            
		            
		        } 
		        else if (args[i].equalsIgnoreCase("-b")) 
		        {
		            try {
		                blen = Integer.parseInt(args[i+1]);
		                b = true;
		            } catch (NumberFormatException e) {
		                System.out.println("Buffer size should be an integer between 0 and 100");
		                System.exit(1);
		            }
		            if (blen < 0 || blen > 100) {
		                System.out.println("Buffer size must be between 0 and 100");
		                System.exit(1);
		            }
		            
		        }
		    }
    
     //if -b option is not specified treat it as -b 1
     if (b == false)
     {
    	 blen =1;
     }
     //add code to call simulation
     if(r && t){
    	 System.out.println("Must specify only one component to have initiative.");
         System.exit(1);
     }
     String initiative ="";
     if(r)
    	 initiative="P";
     else if(t)
    	 initiative="S";
     else
    	 initiative="G";
     
     
    	
     System.out.println("tmp " + temporalPrecision + "  geo" + geographicalPrecision + "  data " + dataPrecision);

     HeatedEarthGUI.createInstance(dataPrecision, geographicalPrecision, temporalPrecision, initiative, blen);
     HeatedEarthGUI gui = HeatedEarthGUI.getinstance();
     gui.displayGui();
	 }
     
     
 
	private static void initializeLogging()
	{
		Logger Presentation_LOGGER = Logger.getLogger(HeatedEarthPresentation.class.getName());
	    Logger Simulation_LOGGER = Logger.getLogger(HeatedEarthSimulation.class.getName()); 
	    FileHandler fh = null;
		try {
			fh = new FileHandler("HeatedEarthLogFile.log");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Presentation_LOGGER.addHandler(fh);
		Simulation_LOGGER.addHandler(fh);
	    SimpleFormatter formatter = new SimpleFormatter();  
	    fh.setFormatter(formatter);  
	}

}