package EarthSim;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.Buffer;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HeatedEarthGUI extends JFrame {

	private boolean simulatorOwnThread = true;
	private boolean presentationOwnThread = true;
	private BlockingQueue<Message> queue;
	private HeatedEarthPresentation display;
	private HeatedEarthSimulation sim;
	private String initiative;
	private Long startTime;
	private boolean paused = false;

	JButton runButton = new JButton("Run Simulation");
	int textSize = 30;
	int textSize20 = 20;
	JTextField gridSize = new JTextField();
	JTextField simTimeStep = new JTextField();
	JTextField displayRate = new JTextField();
	JTextField tilt = new JTextField();
	JTextField orbit = new JTextField();
	JTextField simulationName = new JTextField();
	JTextField simulationLength = new JTextField();
	final JButton start = new JButton();
	JPanel rightPanel = new JPanel();
	private JLabel time = new JLabel();

	public HeatedEarthGUI(boolean presentationThread,
			boolean simulatorOwnThread, String initiative, Integer bufferSize) {
		super();
		this.presentationOwnThread = presentationThread;
		this.simulatorOwnThread = simulatorOwnThread;
		this.queue = new ArrayBlockingQueue<Message>(bufferSize);
		this.initiative = initiative;
		display = new HeatedEarthPresentation(0, queue, 1, paused);

	}

	public void displayGui() {
		this.add(createMainGrid());
		this.pack();
		this.setTitle("Heated Earth Simulation");
		this.setResizable(false);
		this.setVisible(true);
		sim = new HeatedEarthSimulation(Integer.valueOf(gridSize.getText()),
				Integer.valueOf(simTimeStep.getText()), Double.valueOf(orbit
						.getText()), Double.valueOf(tilt.getText()), queue);
		// Set initiative
		if ("S".equalsIgnoreCase(initiative)) {
			sim.setPresentation(display);
		} else if ("P".equalsIgnoreCase(initiative)) {
			display.setSimulation(sim);
		}
		display.setTime(time);

	}

	public JPanel createMainGrid() {
		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		JLabel label = new JLabel();
		label.setText("   ");

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		pane.add(label, c);
		label = new JLabel();
		label.setText("   ");

		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(label, c);

		c.weightx = .0;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(createMenu(), c);

		JPanel displayControls = new JPanel();
		start.setIcon(new ImageIcon("images/play.png"));
		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (display.getRunning()) {
					if (paused) {
						paused = false;
						display.setPaused(false);
						sim.setPaused(false);
						start.setIcon(new ImageIcon("images/pause.png"));
					} else {
						paused = true;
						display.setPaused(true);
						sim.setPaused(true);
						start.setIcon(new ImageIcon("images/play.png"));
					}
					repaint();

				}
			}
		});
		displayControls.add(start);

		JButton stop = new JButton();
		stop.setIcon(new ImageIcon("images/stop.png"));
		stop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sim.setRunning(false);
				sim.setPaused(true);
				display.setRunning(false);
				display.setPaused(true);
				queue.clear();
				paused = true;
				repaint();

				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				sim.printRunningInfo();
				display.printRunningInfo();

			}
		});
		displayControls.add(stop);
		JButton restart = new JButton();
		restart.setIcon(new ImageIcon("images/replay.png"));
		restart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sim.setRunning(false);
				display.setRunning(false);
				paused = false;
				queue.clear();
				repaint();
				run();

			}
		});
		displayControls.add(restart);
		JLabel timeLabel = new JLabel("Running Time:");
		timeLabel.setFont(new Font("Arial", 0, textSize));
		displayControls.add(timeLabel);
		time.setFont(new Font("Arial", 0, textSize));
		time.setText("0 s");
		displayControls.add(time);

		c.gridx = 1;
		c.gridy = 2;
		pane.add(displayControls, c);

		c.weightx = .8;
		c.weighty = .8;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 3;
		// c.ipadx=20;
		pane.add(display, c);

		return pane;
	}

	private JPanel createMenu() {
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new FlowLayout());

		JPanel smallGrid = new JPanel();
		smallGrid.setFont(new Font("Arial", 0, textSize));
		smallGrid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		smallGrid.setLayout(new GridLayout(0, 1));

		JLabel gridSizeLabel = new JLabel("Dimension of Grid(Degrees):");
		gridSizeLabel.setFont(new Font("Arial", 0, textSize));

		smallGrid.add(gridSizeLabel);
		gridSize.setFont(new Font("Arial", 0, textSize));
		gridSize.setToolTipText("Enter an Integer greater than zero.");
		gridSize.setText("15");
		gridSize.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text);
					while (180 % value != 0) {
						value--;
					}
					gridSize.setText(value + "");
					if (value < 1) {
						gridSize.setText("1");
						return false;
					} else {
						return true;
					}
				} catch (NumberFormatException e) {
					gridSize.setText("");
					return false;
				}
			}
		});
		smallGrid.add(gridSize);

		JLabel topLabel = new JLabel("Simulation Time Step(m):");
		topLabel.setFont(new Font("Arial", 0, textSize));
		smallGrid.add(topLabel);
		simTimeStep.setFont(new Font("Arial", 0, textSize));
		simTimeStep.setText("1");
		simTimeStep.setToolTipText("Enter an Integer between zero and 1440.");
		simTimeStep.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text);
					if (value < 1 || value > 1440) {
						simTimeStep.setText("1");
						return false;
					} else {
						return true;
					}
				} catch (NumberFormatException e) {
					simTimeStep.setText("");
					return false;
				}
			}
		});
		smallGrid.add(simTimeStep);

		JLabel bottomLabel = new JLabel("Display Rate(ms):");
		bottomLabel.setFont(new Font("Arial", 0, textSize));
		smallGrid.add(bottomLabel);
		displayRate.setFont(new Font("Arial", 0, textSize));
		displayRate.setText("10");
		displayRate.setToolTipText("Enter an Integer between zero and 1000.");
		displayRate.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text);
					if (value < 1 || value > 10000) {
						displayRate.setText("1");
						display.setDisplayRate(1);
					}

					try {
						Integer.valueOf(displayRate.getText());
					} catch (NumberFormatException e) {
						displayRate.setText("1");
						display.setDisplayRate(1);

					}
					display.setDisplayRate(Integer.valueOf(displayRate
							.getText()));

					return true;

				} catch (NumberFormatException e) {
					displayRate.setText("");
					return false;
				}
			}
		});
		smallGrid.add(displayRate);

		JLabel fourthLabel = new JLabel("Tilt:");
		fourthLabel.setFont(new Font("Arial", 0, textSize20));
		smallGrid.add(fourthLabel);
		tilt.setFont(new Font("Arial", 0, textSize20));
		tilt.setText("23.44");
		tilt.setToolTipText("Enter a value between -180 and 180.");
		tilt.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text);
					if (value < -180 || value > 180) {
						tilt.setText("23.44");
						display.setTilt(23.44);
					}

					try {
						Integer.valueOf(tilt.getText());
					} catch (NumberFormatException e) {
						tilt.setText("23.44");
						display.setTilt(23.44);

					}
					display.setTilt(Double.valueOf(tilt.getText()));

					return true;

				} catch (NumberFormatException e) {
					tilt.setText("");
					return false;
				}
			}
		});
		smallGrid.add(tilt);

		JLabel fifthLabel = new JLabel("Orbit:");
		fifthLabel.setFont(new Font("Arial", 0, textSize20));
		smallGrid.add(fifthLabel);
		orbit.setFont(new Font("Arial", 0, textSize20));
		orbit.setText("0.0167");
		orbit.setToolTipText("Enter a value between zero and 1.");
		orbit.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text); // to fix
					if (value < 0 || value > 1) {
						orbit.setText("1");
						display.setOrbit(0.0167);
					}

					try {
						Integer.valueOf(orbit.getText());
					} catch (NumberFormatException e) {
						orbit.setText("1");
						display.setTilt(1);

					}
					display.setOrbit(Double.valueOf(orbit.getText()));

					return true;

				} catch (NumberFormatException e) {
					orbit.setText("");
					return false;
				}
			}
		});
		smallGrid.add(orbit);

		JLabel sixthLabel = new JLabel("Length (months):");
		sixthLabel.setFont(new Font("Arial", 0, textSize20));
		smallGrid.add(sixthLabel);
		simulationLength.setFont(new Font("Arial", 0, textSize20));
		simulationLength.setText("12");
		simulationLength.setToolTipText("Enter a value between 1 and 1200.");
		simulationLength.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text); // to fix
					if (value < 1 || value > 1200) {
						simulationLength.setText("12");
						display.setSimulationLength(12);
					}

					try {
						Integer.valueOf(simulationLength.getText());
					} catch (NumberFormatException e) {
						simulationLength.setText("12");
						display.setSimulationLength(12);
					}
					display.setSimulationLength(Integer
							.valueOf(simulationLength.getText()));

					return true;

				} catch (NumberFormatException e) {
					simulationLength.setText("");
					return false;
				}
			}
		});
		smallGrid.add(simulationLength);

		JLabel name = new JLabel("Name:");
		name.setFont(new Font("Arial", 0, textSize20));
		smallGrid.add(name);
		simulationName.setFont(new Font("Arial", 0, textSize20));
		simulationName.setToolTipText("Provide a Simulation Name");
		simulationName.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					display.setSimulationName(text);
					return true;
				} catch (NumberFormatException e) {
					simulationName.setText("");
					return false;
				}
			}
		});
		smallGrid.add(simulationName);

		runButton.setFont(new Font("Arial", 0, textSize));

		runButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				display.setRunning(false);
				sim.setRunning(false);
				queue.clear();
				repaint();

				display.setGridSize(Integer.valueOf(gridSize.getText()));
				sim.setGridSize(Integer.valueOf(gridSize.getText()));
				sim.setTimeStep(Integer.valueOf(simTimeStep.getText()));
				sim.setTilt(Double.valueOf(tilt.getText()));
				sim.setOrbit(Double.valueOf(orbit.getText()));
				sim.setLength(Integer.valueOf(simulationLength.getText()));
				sim.setName(simulationName.getText());
				run();
			}
		});
		smallGrid.add(runButton);
		innerPanel.add(smallGrid);
		return innerPanel;
	}

	// runs both simulator and presentation
	public void run() {
		sim.reset();
		display.reset();
		startTime = (new Date()).getTime();
		queue.clear();
		start.setIcon(new ImageIcon("images/pause.png"));
		paused = false;
		display.setDisplayRate(Integer.valueOf(displayRate.getText()));

		if (simulatorOwnThread) {
			new Thread() {
				@Override
				public void run() {
					if ("S".equalsIgnoreCase(initiative)
							|| "G".equalsIgnoreCase(initiative)) {
						sim.run();
					}

				}
			}.start();

		}

		if (presentationOwnThread) {
			new Thread() {
				@Override
				public void run() {
					if ("P".equalsIgnoreCase(initiative)
							|| "G".equalsIgnoreCase(initiative)) {
						display.run();
					}
				}
			}.start();
		}
		if ("P".equalsIgnoreCase(initiative)
				|| "G".equalsIgnoreCase(initiative)) {
			if (!presentationOwnThread) {
				display.run();
			}
		}
		if ("S".equalsIgnoreCase(initiative)
				|| "G".equalsIgnoreCase(initiative)) {
			if (!simulatorOwnThread) {
				sim.run();
			}
		}
	}
}
