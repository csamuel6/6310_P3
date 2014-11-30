package EarthSim.main;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.Buffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import EarthSim.persistance.DataManager;
import EarthSim.persistance.QueryParameters;
import EarthSim.persistance.SimulationStorage;

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
	JButton runQueryButton = new JButton("Run Query");
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
//	JTextField lowerLatitude = new JTextField();
//	JTextField upperLatitude = new JTextField();
//	JTextField lowerLongitude = new JTextField();
//	JTextField upperLongitude = new JTextField();
//	JTextField startDateTextField = new JTextField();
//	JTextField endDateTextField = new JTextField();
	private JList list;
	private DefaultListModel listModel;
	Date startDate;
	Date endDate;
	
	int dataPrecision = 0;
    int geographicalPrecision = 0;
    int temporalPrecision = 0;
    
    private static HeatedEarthGUI heatedEarthGUI = null;

	private HeatedEarthGUI(boolean presentationThread,
			boolean simulatorOwnThread, String initiative, Integer bufferSize) {
		super();
		this.presentationOwnThread = presentationThread;
		this.simulatorOwnThread = simulatorOwnThread;
		this.queue = new ArrayBlockingQueue<Message>(bufferSize);
		this.initiative = initiative;
		display = new HeatedEarthPresentation(0, queue, 1, paused);

	}
	
	
	public static void createInstance(int dataPrecision, int geographicalPrecision, int temporalPrecision, String initiative, Integer bufferSize)
	{
		
		if (heatedEarthGUI == null)
		{
			heatedEarthGUI =  new HeatedEarthGUI(dataPrecision, geographicalPrecision, temporalPrecision,  "G", 25);
		}

	}
	
	
	public static HeatedEarthGUI getinstance ()
	{
		return heatedEarthGUI;
	}
	
	
	private HeatedEarthGUI(int dataPrecision, int geographicalPrecision, int temporalPrecision, String initiative, Integer bufferSize) {
		super();
		
		this.dataPrecision = dataPrecision;
		this.geographicalPrecision = geographicalPrecision;
		this.temporalPrecision = temporalPrecision;
		
		this.presentationOwnThread = true;
		this.simulatorOwnThread = true;
		this.queue = new ArrayBlockingQueue<Message>(bufferSize);
		this.initiative = initiative;
		display = new HeatedEarthPresentation(0, queue, 1, paused);

	}

	public void displayGui() {
		
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
//		
////		JMenuItem mntmNewMenuItem = new JMenuItem("File");
////		menuBar.add(mntmNewMenuItem);
//		
		JMenu userQueryOption = new JMenu("UserQueryInterface");
		menuBar.add(userQueryOption);
		
//		
		userQueryOption.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				JFrame frame = new UserQueryInterface();
				
				frame.setVisible(true);
//				
			}
		});
//		
		userQueryOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new UserQueryInterface();
				
				frame.setVisible(true);
				
			}
		});
//		
//		
//		
//		

		
		
		
		
		this.add(createMainGrid());
		this.pack();
		this.setTitle("Heated Earth Simulation");
		this.setResizable(false);
		this.setVisible(true);
		sim = new HeatedEarthSimulation(Integer.valueOf(gridSize.getText()),
		Integer.valueOf(simTimeStep.getText()), Double.valueOf(orbit
				.getText()), Double.valueOf(tilt.getText()), queue);

		

		runButton.setEnabled(true);
		// Set initiative
		if ("S".equalsIgnoreCase(initiative)) {
			sim.setPresentation(display);
		} else if ("P".equalsIgnoreCase(initiative)) {
			display.setSimulation(sim);
		}
		display.setTime(time);

	}

	public JScrollPane createMainGrid() {
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

		JScrollPane scrollpane = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
		return scrollpane;
	}

	private JPanel createMenu() {
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new FlowLayout());

		JPanel smallGrid = new JPanel();
		smallGrid.setFont(new Font("Arial", 0, textSize));
		smallGrid.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		smallGrid.setLayout(new GridLayout(0, 1));

		JLabel gridSizeLabel = new JLabel("Dimension of Grid(Degrees):");
		gridSizeLabel.setFont(new Font("Arial", 0, textSize20));

		smallGrid.add(gridSizeLabel);
		gridSize.setFont(new Font("Arial", 0, textSize20));
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
		topLabel.setFont(new Font("Arial", 0, textSize20));
		smallGrid.add(topLabel);
		simTimeStep.setFont(new Font("Arial", 0, textSize20));
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
		bottomLabel.setFont(new Font("Arial", 0, textSize20));
		smallGrid.add(bottomLabel);
		displayRate.setFont(new Font("Arial", 0, textSize20));
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
					Double value = Double.valueOf(text); // to fix
					if (value < 0 || value > 1) {
						orbit.setText("0.0167");
						display.setOrbit(0.0167);
					}

					try {
						Double.valueOf(orbit.getText());
						
					} catch (NumberFormatException e) {
						orbit.setText("0.0167");
						display.setOrbit(0.0167);

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
		
//
//		JLABEL LOWERLATITUDE = NEW JLABEL("LOWER LATITUDE");
//		LOWERLATITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
//		SMALLGRID.ADD(LOWERLATITUDE);
//		LOWERLATITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
//		LOWERLATITUDE.SETTOOLTIPTEXT("ENTER A LOWER LATITUDE.");
//		LOWERLATITUDE.SETINPUTVERIFIER(NEW INPUTVERIFIER() {
//			@OVERRIDE
//			PUBLIC BOOLEAN VERIFY(JCOMPONENT INPUT) {
//				STRING TEXT = ((JTEXTFIELD) INPUT).GETTEXT();
//				TRY {
//					INTEGER VALUE = INTEGER.VALUEOF(TEXT); // TO FIX
//					IF (VALUE < -180 || VALUE > 180) {
//						LOWERLATITUDE.SETTEXT("");
//					}
//
//					TRY {
//						INTEGER.VALUEOF(LOWERLATITUDE.GETTEXT());
//					} CATCH (NUMBERFORMATEXCEPTION E) {
//						LOWERLATITUDE.SETTEXT("");
//					}
//					RETURN TRUE;
//
//				} CATCH (NUMBERFORMATEXCEPTION E) {
//					SIMULATIONLENGTH.SETTEXT("");
//					RETURN FALSE;
//				}
//			}
//			});
//		SMALLGRID.ADD(LOWERLATITUDE);
////		
////		JLABEL UPPERLATITUDE = NEW JLABEL("UPPER LATITUDE");
////		UPPERLATITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		SMALLGRID.ADD(UPPERLATITUDE);
////		UPPERLATITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		UPPERLATITUDE.SETTOOLTIPTEXT("ENTER A UPPER LATITUDE.");
////		UPPERLATITUDE.SETINPUTVERIFIER(NEW INPUTVERIFIER() {
////			@OVERRIDE
////			PUBLIC BOOLEAN VERIFY(JCOMPONENT INPUT) {
////				STRING TEXT = ((JTEXTFIELD) INPUT).GETTEXT();
////				TRY {
////					INTEGER VALUE = INTEGER.VALUEOF(TEXT); // TO FIX
////					IF (VALUE < -180 || VALUE > 180) {
////						UPPERLATITUDE.SETTEXT("");
////					}
////
////					TRY {
////						INTEGER.VALUEOF(UPPERLATITUDE.GETTEXT());
////					} CATCH (NUMBERFORMATEXCEPTION E) {
////						UPPERLATITUDE.SETTEXT("");
////					}
////					RETURN TRUE;
////
////				} CATCH (NUMBERFORMATEXCEPTION E) {
////					UPPERLATITUDE.SETTEXT("");
////					RETURN FALSE;
////				}
////			}
////			});
////		SMALLGRID.ADD(UPPERLATITUDE);
////		
////		JLABEL LOWERLONGITUDE = NEW JLABEL("LOWER LONGITUDE");
////		LOWERLONGITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		SMALLGRID.ADD(LOWERLONGITUDE);
////		LOWERLONGITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		LOWERLONGITUDE.SETTOOLTIPTEXT("ENTER A LOWER LONGITUDE.");
////		LOWERLONGITUDE.SETINPUTVERIFIER(NEW INPUTVERIFIER() {
////			@OVERRIDE
////			PUBLIC BOOLEAN VERIFY(JCOMPONENT INPUT) {
////				STRING TEXT = ((JTEXTFIELD) INPUT).GETTEXT();
////				TRY {
////					INTEGER VALUE = INTEGER.VALUEOF(TEXT); // TO FIX
////					IF (VALUE < -90 || VALUE > 90) {
////						LOWERLONGITUDE.SETTEXT("");
////					}
////
////					TRY {
////						INTEGER.VALUEOF(LOWERLONGITUDE.GETTEXT());
////					} CATCH (NUMBERFORMATEXCEPTION E) {
////						LOWERLONGITUDE.SETTEXT("");
////					}
////					RETURN TRUE;
////
////				} CATCH (NUMBERFORMATEXCEPTION E) {
////					LOWERLONGITUDE.SETTEXT("");
////					RETURN FALSE;
////				}
////			}
////			});
////		SMALLGRID.ADD(LOWERLONGITUDE);
////		
////		JLABEL UPPERLONGITUDE = NEW JLABEL("UPPER LONGITUDE");
////		UPPERLONGITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		SMALLGRID.ADD(UPPERLONGITUDE);
////		UPPERLONGITUDE.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		UPPERLONGITUDE.SETTOOLTIPTEXT("ENTER A UPPER LONGITUDE.");
////		UPPERLONGITUDE.SETINPUTVERIFIER(NEW INPUTVERIFIER() {
////			@OVERRIDE
////			PUBLIC BOOLEAN VERIFY(JCOMPONENT INPUT) {
////				STRING TEXT = ((JTEXTFIELD) INPUT).GETTEXT();
////				TRY {
////					INTEGER VALUE = INTEGER.VALUEOF(TEXT); // TO FIX
////					IF (VALUE < -90 || VALUE > 90) {
////						UPPERLONGITUDE.SETTEXT("");
////					}
////
////					TRY {
////						INTEGER.VALUEOF(UPPERLONGITUDE.GETTEXT());
////					} CATCH (NUMBERFORMATEXCEPTION E) {
////						UPPERLONGITUDE.SETTEXT("");
////					}
////					RETURN TRUE;
////
////				} CATCH (NUMBERFORMATEXCEPTION E) {
////					UPPERLONGITUDE.SETTEXT("");
////					RETURN FALSE;
////				}
////			}
////			});
////		SMALLGRID.ADD(UPPERLONGITUDE);
////		
////		JLABEL STARTDATELABEL = NEW JLABEL("STARTING DATE (MM/DD/YYYY) AND TIME (24 HOUR HH:MM:SS)");
////		TOPLABEL.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE));
////		SMALLGRID.ADD(STARTDATELABEL);
////		STARTDATETEXTFIELD.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		STARTDATETEXTFIELD.SETTOOLTIPTEXT("ENTER A DATE AND TIME (24 HOUR) IN THE FORMAT MM/DD/YYYY HH:MM:SS.");
////		STARTDATETEXTFIELD.SETINPUTVERIFIER(NEW INPUTVERIFIER() {
////			@OVERRIDE
////			PUBLIC BOOLEAN VERIFY(JCOMPONENT INPUT) {
////				STRING TEXT = ((JTEXTFIELD) INPUT).GETTEXT();
////				TRY {
////					SIMPLEDATEFORMAT FORMATTER = NEW SIMPLEDATEFORMAT("MM/DD/YYYY HH:MM:SS"); // TO FIX
////					STARTDATE = FORMATTER.PARSE(TEXT);
////					RETURN TRUE;
////				}  CATCH (PARSEEXCEPTION E) {
////					// TODO AUTO-GENERATED CATCH BLOCK
////					E.PRINTSTACKTRACE();
////					RETURN FALSE;
////				}
////			}
////		});
////		SMALLGRID.ADD(STARTDATETEXTFIELD);
////		
////		JLABEL ENDDATELABEL = NEW JLABEL("ENDING DATE (MM/DD/YYYY) AND TIME (24 HOUR HH:MM:SS)");
////		TOPLABEL.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE));
////		SMALLGRID.ADD(ENDDATELABEL);
////		ENDDATETEXTFIELD.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE20));
////		ENDDATETEXTFIELD.SETTOOLTIPTEXT("ENTER A DATE AND TIME (24 HOUR) IN THE FORMAT MM/DD/YYYY HH:MM:SS.");
////		ENDDATETEXTFIELD.SETINPUTVERIFIER(NEW INPUTVERIFIER() {
////			@OVERRIDE
////			PUBLIC BOOLEAN VERIFY(JCOMPONENT INPUT) {
////				STRING TEXT = ((JTEXTFIELD) INPUT).GETTEXT();
////				TRY {
////					SIMPLEDATEFORMAT FORMATTER = NEW SIMPLEDATEFORMAT("MM/DD/YYYY HH:MM:SS"); // TO FIX
////					ENDDATE = FORMATTER.PARSE(TEXT);
////					RETURN TRUE;
////				}  CATCH (PARSEEXCEPTION E) {
////					// TODO AUTO-GENERATED CATCH BLOCK
////					E.PRINTSTACKTRACE();
////					RETURN FALSE;
////				}
////			}
////		});
////		SMALLGRID.ADD(ENDDATETEXTFIELD);
////		
////		RUNQUERYBUTTON.SETFONT(NEW FONT("ARIAL", 0, TEXTSIZE));
////		RUNQUERYBUTTON.SETENABLED(FALSE);
////		RUNQUERYBUTTON.ADDACTIONLISTENER(NEW ACTIONLISTENER() {
////
////			PUBLIC VOID ACTIONPERFORMED(ACTIONEVENT E) {
////				DATAMANAGER DATAMANAGER = NEW DATAMANAGER();
////				QUERYPARAMETERS QUERYPARAMETERS = NEW QUERYPARAMETERS();
////				QUERYPARAMETERS.SETNAME(SIMULATIONNAME.GETTEXT());
////				QUERYPARAMETERS.SETGRIDSPACING(INTEGER.VALUEOF(GRIDSIZE.GETTEXT()));
////				QUERYPARAMETERS.SETTILT(DOUBLE.VALUEOF(TILT.GETTEXT()));
////				QUERYPARAMETERS.SETORBIT(DOUBLE.VALUEOF(ORBIT.GETTEXT()));
////				QUERYPARAMETERS.SETTIMESTEP(INTEGER.VALUEOF(SIMTIMESTEP.GETTEXT()));
////				QUERYPARAMETERS.SETLOWERLATITUDE(-10);
////				QUERYPARAMETERS.SETUPPERLATITUDE(10);
////				QUERYPARAMETERS.SETLOWERLONGITUDE(-60);
////				QUERYPARAMETERS.SETUPPERLONGITUDE(60);
////
////				QUERYPARAMETERS.SETSTARTDATE(STARTDATE);
////				QUERYPARAMETERS.SETENDDATE(ENDDATE);
////				
////				LISTMODEL.REMOVEALLELEMENTS();
////				LIST<SIMULATIONSTORAGE> SIMULATIONLIST = DATAMANAGER.READSIMULATION(QUERYPARAMETERS);
////				FOR (SIMULATIONSTORAGE STORAGE : SIMULATIONLIST) {
////					LISTMODEL.ADDELEMENT(STORAGE);
////				}
////				QUEUE.CLEAR();
////				REPAINT();
////
////				DISPLAY.SETGRIDSIZE(INTEGER.VALUEOF(GRIDSIZE.GETTEXT()));
////				SIM.SETGRIDSIZE(INTEGER.VALUEOF(GRIDSIZE.GETTEXT()));
////				SIM.SETTIMESTEP(INTEGER.VALUEOF(SIMTIMESTEP.GETTEXT()));
////				SIM.SETTILT(DOUBLE.VALUEOF(TILT.GETTEXT()));
////				SIM.SETORBIT(DOUBLE.VALUEOF(ORBIT.GETTEXT()));
////				SIM.SETLENGTH(INTEGER.VALUEOF(SIMULATIONLENGTH.GETTEXT()));
////				SIM.SETNAME(SIMULATIONNAME.GETTEXT());
////				RUN();
////			}
////		});
////		
		smallGrid.add(runButton);
		
		runButton.setFont(new Font("Arial", 0, textSize));
		runButton.setEnabled(false);
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
	
	
    public int getDataPrecision() {
		return dataPrecision;
	}


	public void setDataPrecision(int dataPrecision) {
		this.dataPrecision = dataPrecision;
	}


	public int getGeographicalPrecision() {
		return geographicalPrecision;
	}


	public void setGeographicalPrecision(int geographicalPrecision) {
		this.geographicalPrecision = geographicalPrecision;
	}


	public int getTemporalPrecision() {
		return temporalPrecision;
	}


	public void setTemporalPrecision(int temporalPrecision) {
		this.temporalPrecision = temporalPrecision;
	}

}
