package EarthSim.main;

import java.awt.Color;
import java.awt.Dimension;
import com.mysql.jdbc.*;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HeatedEarthPresentation extends JPanel {

	private double[][] grid;

	private Image img;
	private int gridSize;
	private Integer displayRate;
	private boolean running;
	private boolean paused;

	private BlockingQueue<Message> queue;

	private HeatedEarthSimulation simulation = null;
	private Long startTime;
	private Dimension size;
	double low = 284;
	double high = 290;
	private int lon = 360;
	private int lat = 180;
	private boolean testing;
	private Integer sunsLongitude = 0;
	private double sunsLatitude = 63;
	private JLabel time;
	private static final long serialVersionUID = 1L;
	private String path = "images/worldmap.png";
	private Long lastupdate = null;
	private int statsTimer = 0;
	private int stabilizationCounter = 0;
	private boolean hasPrintedStabilization = false;
	ArrayList<Long> waitList = new ArrayList<Long>();

	ArrayList<Long> displayTimeList = new ArrayList<Long>();
	private double tilt = 0;
	private double orbit = 0;
	private int simulationLength = 0;
	private String simulationName = "";
	
	private final static Logger LOGGER = Logger
			.getLogger(HeatedEarthPresentation.class.getName());

	public HeatedEarthPresentation(int gridSpacing,
			BlockingQueue<Message> queue, Integer displayRate, boolean paused) {
		super();
		this.queue = queue;
		this.displayRate = displayRate;
		this.gridSize = gridSpacing;
		this.paused = paused;

		img = new ImageIcon(path).getImage();
		size = new Dimension(img.getWidth(null), img.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}

	public void setTime(JLabel time) {
		this.time = time;
	}

	public void setTilt(double tilt) {
		this.tilt = tilt;

	}

	public void setOrbit(double orbit) {
		this.orbit = orbit;
	}

	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;

	}

	public void reset() {
		initGrid(gridSize);
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setRunning(boolean running) {

		startTime = (new Date()).getTime();
		hasPrintedStabilization = false;
		this.running = running;

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
			System.out.println("Average presentation idle time: " + averageWait
					+ " ms");
		}
		totalTime = 0L;
		count = 0;
		if (!displayTimeList.isEmpty()) {
			for (Long waitTime : displayTimeList) {
				count++;
				totalTime += waitTime;
			}
			Long averageWait = totalTime / count;
			System.out.println("Average presentation display time: "
					+ averageWait + " ms");
		}
	}

	private void initGrid(int gridSpacing) {
		grid = new double[180 / gridSpacing][360 / gridSpacing];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = 0;
			}

		}

	}

	public void setSimulation(HeatedEarthSimulation s) {
		simulation = s;
	}

	// Starts the presentation updating
	public void run() {
		running = true;
		paused = false;
		while (running) {
			if (!paused) {
				try {
					if (simulation != null) {
						simulation.update();
					}
					Long before = (new Date()).getTime();
					Message update = queue.take();
					waitList.add((new Date()).getTime() - before);
					grid = update.getGrid();
					sunsLongitude = update.getSunsLongitude().intValue();
					sunsLatitude = update.getSunsLatitude();
					

					this.repaint();
					Thread.currentThread().sleep(displayRate);

					statsTimer++;
					if (statsTimer == 1400) {
						// LOGGER.log(Level.INFO, Analyzer.getMemoryStats());
						statsTimer = 0;
					}

				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

		}

	}

	public void setDisplayRate(Integer displayRate) {
		this.displayRate = displayRate;
	}

	public void update() {
		try {
			if (lastupdate == null)
				lastupdate = (new Date()).getTime();

			while ((new Date()).getTime() - lastupdate < displayRate)
				Thread.currentThread().sleep(1);

			lastupdate = (new Date()).getTime();
			Long before = (new Date()).getTime();
			Message update = queue.take();
			waitList.add((new Date()).getTime() - before);
			grid = update.getGrid();
			
			sunsLongitude = update.getSunsLongitude().intValue();
			sunsLatitude = update.getSunsLatitude();
			this.repaint();

		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	// draws the grid everytime repaint() is called on this Ojbect
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Long before = (new Date()).getTime();
		g.drawImage(img, 0, 0, null);
		Graphics2D g2d = (Graphics2D) g;

		if (gridSize != 0) {
			double prevLow = low;
			double prevHigh = high;
			high -= 10;
			low += 10;
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					Double value = grid[i][j];
					if (value == 0) {
						continue;
					}

					if (value > high) {
						high = value;
					}
					if (value < low) {
						low = value;
					}
				}
			}
			if (!hasPrintedStabilization && (int) prevHigh == (int) high
					&& (int) prevLow == (int) low) {
				stabilizationCounter++;
				if (stabilizationCounter > 5) {
					Long totalTime = ((new Date()).getTime() - startTime);
					System.out.println("Simulation stabilized in:"
							+ totalTime.intValue() + " ms");
					hasPrintedStabilization = true;
				}
			} else {
				stabilizationCounter--;
			}
			Long height = new Long(size.height) / new Long(grid.length);
			Long width = new Long(size.width) / new Long(grid[0].length);
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					Double value = grid[i][j];
					if (value == 0) {
						continue;
					}
					Double v = (high - grid[i][j]) / (high - low);
					Color c = Color.getHSBColor(.666f * v.floatValue(), 1f, 1f);

					g2d.setColor(new Color(c.getRed(), c.getGreen(), c
							.getBlue(), 120));

					g2d.fillRect(j * width.intValue(), i * height.intValue(),
							width.intValue(), height.intValue());
					g2d.setColor(Color.black);
					g2d.draw(new Rectangle(j * width.intValue(), i
							* height.intValue(), width.intValue(), height
							.intValue()));
				}
			}
			g2d.setColor(new Color(255, 255, 0, 100));
			Long newLong = (long) ((((float) sunsLongitude + 180) / 360) * size.width);

			Long newLat = (long) ((((float) sunsLatitude + 90) / 180) * size.height);

			// System.out.println(newLat + " lewlat   " + sunsLatitude);

			g2d.fillOval(newLong.intValue() - 50, newLat.intValue() - 50, 100,
					100);
		}
		if (time != null && startTime != null) {
			Long runningTime = ((new Date()).getTime() - startTime) / 1000;
			time.setText(runningTime.intValue() + " s");
			time.repaint();
		}
		Long wait = (new Date()).getTime() - before;
		displayTimeList.add(wait);
	}

	public boolean getRunning() {
		return running;
	}

	public int getSimulationLength() {
		return simulationLength;
	}

	public void setSimulationLength(int simulationLength) {
		this.simulationLength = simulationLength;
	}

	public String getSimulationName() {
		return simulationName;
	}

	public void setSimulationName(String simulationName) {
		this.simulationName = simulationName;
	}


}
