package EarthSim;



	import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

	public class Analyzer {
			
	    public static void main(String[] args) {
	    	
	        ArrayList<String> sizes = new ArrayList<String>();
	       
	        ArrayList<String[]> options = new ArrayList<String[]>();
	        //options.add( new String[]{"-s", "", "", "", "", ""});
	        //options.add(new String[]{"", "-p", "", "", "", ""});
	        options.add(new String[]{"-s", "-p", "", "", "", ""});
	        //options.add(new String[]{"-s", "", "-t", "", "", ""});
	        //options.add(new String[]{"-s", "", "", "-r", "", ""});
	        //options.add(new String[]{"", "-p", "-t", "", "", ""});
	        //options.add(new String[]{"", "-p", "", "-r", "", ""});
	        //options.add(new String[]{"", "", "", "-r", "", ""});
	        //options.add(new String[]{"", "", "-t", "", "", ""});
	        
	        //options.add(new String[]{"-s", "", "", "", "-b", "2"});
	        //options.add(new String[]{"", "-p", "", "", "-b", "2"});
	        //options.add(new String[]{"-s", "-p", "", "", "-b", "2"});
	        //options.add(new String[]{"-s", "", "-t", "", "-b", "2"});
	        //options.add(new String[]{"-s", "", "", "-r", "-b", "2"});
	        //options.add(new String[]{"", "-p", "-t", "", "-b", "2"});
	        //options.add(new String[]{"", "-p", "", "-r", "-b", "2"});
	        //options.add(new String[]{"", "", "", "-r", "-b", "2"});
	        //options.add(new String[]{"", "", "-t", "", "-b", "2"});
	            
     
	        
	       
	       
	        sizes.add("1");
	        
	        int pcount = 0;

	        for (String[] o : options) {
	        	pcount +=1;
	            for (String s: sizes) {


	                System.out.println("Running: "+pcount+" for grid: "+s);
	                File mem_file = new File("results/"+pcount+"_"+s+"_mem_usage.csv");
	                File time_file = new File("results/"+pcount+"_"+s+"_execution_time.csv");
	                if (!mem_file.exists()) {
	                	try {
	                		mem_file.createNewFile();
	                	} catch (IOException e) {
	                		e.printStackTrace();
	                	}
	                }

	                if (!time_file.exists()) {
	                	try {
	                		time_file.createNewFile();
	                	} catch (IOException e) {
	                		e.printStackTrace();
	                	}
	                }
	                BufferedWriter mem_bw = null;
	                BufferedWriter time_bw = null;
	                try {
	                	FileWriter mem_fw = new FileWriter(mem_file.getAbsoluteFile());
	                	mem_bw = new BufferedWriter(mem_fw);
	                } catch (IOException e) {
	                	e.printStackTrace();
	                }
	                try {
	                	FileWriter time_fw = new FileWriter(time_file.getAbsoluteFile());
	                	time_bw = new BufferedWriter(time_fw);
	                } catch (IOException e) {
	                	e.printStackTrace();
	                }
	                String programName = "Option"+pcount;
	                long memoryUsed;
	                long startTime, stopTime, elapsedTime;
	                int iterations = Integer.parseInt(s);
	                String[] demoArgs;

	           
	                long[] executionTimeResults = new long[iterations];
	                long[] memoryResults = new long[iterations];
	                
	                for(int j =0; j<options.size(); j++)
	                {
	              
	                    
	                	for (int i = 0; i < iterations; i++) {

	                		//Demo theDemo = new Demo();
	                		startTime = System.currentTimeMillis();
	                		Demo.main(o);
	                		stopTime = System.currentTimeMillis();
	                		executionTimeResults[i] = stopTime - startTime;
	                		Runtime rt = Runtime.getRuntime();
	                		memoryResults[i] = rt.totalMemory() - rt.freeMemory();
	                		rt.gc();
	                		System.out.println("option#"+pcount+" , memory used "+memoryResults[i]);
	                 

	                		try {
	                			time_bw.write(executionTimeResults[i] + "\n");
	                		} catch (IOException e) {
	                			e.printStackTrace();
	                		}
	                		try {
	                			mem_bw.write(memoryResults[i] + "\n");
	                		} catch (IOException e) {
	                			e.printStackTrace();
	                		}
	                		System.out.println(Arrays.toString(executionTimeResults));
	                		System.out.println(Arrays.toString(memoryResults));
	                	}
	                	try {
	                		time_bw.close();
	                	} catch (IOException e) {
	                		e.printStackTrace();
	                	}
	                	try {
	                		mem_bw.close();
	                	} catch (IOException e) {
	                	e.printStackTrace();
	                	}
	                }
	            }
	        }

	    }
	    
	    
		public static String getMemoryStats()
		{
			long MEGABYTE = 1024L * 1024L;
		    // Get the Java runtime
		    Runtime runtime = Runtime.getRuntime();
		    // Run the garbage collector
		    runtime.gc();
		    // Calculate the used memory
		    long memory = runtime.totalMemory() - runtime.freeMemory();
		    String memStats = "\n Used memory is kbytes: " + memory/1024L + 
		    		" \n" + "Used memory is megabytes: "  + memory/MEGABYTE + " \n";
		    
		    return memStats;
		}
}


