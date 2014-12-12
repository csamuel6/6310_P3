CS6310-Project3
===============


Description
===============
This program simulates temperature of the planet Earth as it is heated by the Sun containing varying degrees of temporal, geographical and temperature related precision as specified by the user.


Installation, Compilation, and Execution of the HeatedEarth Simulator:
===============

1.	 Open the tarball in a directory of your choosing.
2.	Using phpAdmin, import the HeatedEarth.sql file from the extracted folder and press Go. (This will create the necessary database schema.)
3.	cd into the folder that holds the source code
4.	cd into the src folder
5.	Run the following command from the terminal: javac -d bin/src/EarthSim/*.java
6.	Then run this command: java -classpath ".;*;*.jar" EarthSim.main.Demo -p 2 -t 100 -g 100  


