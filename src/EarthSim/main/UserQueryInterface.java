package EarthSim.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import EarthSim.persistance.DataManager;
import EarthSim.persistance.GridCellStorage;
import EarthSim.persistance.QueryParameters;
import EarthSim.persistance.SimulationStorage;

public class UserQueryInterface extends JFrame {

	private JPanel contentPane;
	DataManager dataManager = new DataManager();
	JTextField simulationName = new JTextField();
	JTextField simulationLength = new JTextField();
	private JScrollPane gcTableScrollPane = new JScrollPane();
	final JButton start = new JButton();
	JPanel rightPanel = new JPanel();
	JTextField upperLatitude = new JTextField();
	JTextField lowerLatitude = new JTextField();
	JTextField lowerLongitude = new JTextField();
	JTextField upperLongitude = new JTextField();
	private DefaultTableModel simulationTableModel = new DefaultTableModel();
	private JTable simulationTable = new JTable(simulationTableModel);
	private DefaultListModel simulationListModel = new DefaultListModel();
	Date startDate;
	Date endDate;
	int textSize = 10;
	int simulationFontSize = 12;
	int textSize20 = 15;
	JTextField tilt = new JTextField();
	double tiltValue;
	double OrbitValue;
	int lowerLatitudeValue;
	int upperLatitudeValue;
	int lowerLongitudeValue;
	int upperLongitudeValue;
	JTextField orbit = new JTextField();
	private JTextField startTime = new JTextField();
	private JTextField endTime = new JTextField();
	private javax.swing.JList simulationList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserQueryInterface frame = new UserQueryInterface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserQueryInterface() {

		setBounds(100, 100, 950, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		contentPane.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();

		panel.setLayout(gridBagLayout);

		contentPane.add(panel, BorderLayout.WEST);

		simulationList = new javax.swing.JList(simulationListModel);

		simulationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		simulationList.setFont(new Font("Arial", 0, simulationFontSize));

		ListSelectionModel listSelectionModel = simulationList
				.getSelectionModel();
		listSelectionModel
				.addListSelectionListener(new JListSelectionHandler());

		List<SimulationStorage> simulationStorageList = dataManager
				.getAllSimulations();

		for (SimulationStorage s : simulationStorageList) {

			// System.out.println(simulationStorageList.size() + " dfdfd ");

			if (s.getName() != null && s.getName().length() > 1) {
				// System.out.println(simulationStorageList.size() +
				// " 1111111111111111111111111 ");

				simulationListModel.addElement(s);
			}

		}

		// Set up column for storing cell data
		simulationTableModel.addColumn("Latitude");
		simulationTableModel.addColumn("Longitude");
		simulationTableModel.addColumn("Temperature");
		simulationTableModel.addColumn("Time");


		Border lineBorder = BorderFactory.createLineBorder(Color.black);
		Border listBorder = BorderFactory.createTitledBorder(lineBorder,
				" Simulations ");
		simulationList.setBorder(listBorder);

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagConstraints.gridheight = 2;

		GridBagConstraints simulationConstraints = new GridBagConstraints();
		simulationConstraints.fill = GridBagConstraints.BOTH;
		simulationConstraints.gridwidth = GridBagConstraints.REMAINDER;
		simulationConstraints.gridheight = 5;
		gridBagLayout.setConstraints(simulationList, simulationConstraints);
		panel.add(simulationList);

//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setViewportView(simulationList);
//		
//		
//		panel.add(scrollPane);

		JLabel name = new JLabel("Name:");
		// name.setBounds(10, 62, 57, 24);
		name.setFont(new Font("Arial", 0, textSize20));
		gridBagLayout.setConstraints(name, gridBagConstraints);
		panel.add(name);

		simulationName.setFont(new Font("Arial", 0, textSize20));
		simulationName.setToolTipText("Enter a name.");
		gridBagLayout.setConstraints(simulationName, gridBagConstraints);
		panel.add(simulationName);

		JLabel tiltLbl = new JLabel("Axial Tilt");
		tiltLbl.setFont(new Font("Arial", 0, textSize20));
		gridBagLayout.setConstraints(tiltLbl, gridBagConstraints);
		panel.add(tiltLbl);
		tilt.setFont(new Font("Arial", 0, textSize20));
		tilt.setToolTipText("Enter an axial tilt.");
		tilt.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Double value = Double.valueOf(text); // to fix
					if (value < -180 || value > 180) {
						tilt.setText("");
					} else {
						tiltValue = value;
					}
					return true;
				} catch (NumberFormatException e) {
					tilt.setText("");
					return false;
				}
			}
		});
		gridBagLayout.setConstraints(tilt, gridBagConstraints);
		panel.add(tilt);

		JLabel OrbitLbl = new JLabel("Orbital Eccentricity");
		OrbitLbl.setFont(new Font("Arial", 0, textSize20));
		gridBagLayout.setConstraints(OrbitLbl, gridBagConstraints);
		panel.add(OrbitLbl);
		orbit.setFont(new Font("Arial", 0, textSize20));
		orbit.setToolTipText("Enter an Orbital Eccentricity.");
		orbit.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Double value = Double.valueOf(text);
					if (value >= 1 || value < 0) {
						orbit.setText("");
					} else {
						OrbitValue = value;
					}
					return true;
				} catch (NumberFormatException e) {
					orbit.setText("");
					return false;
				}
			}
		});
		gridBagLayout.setConstraints(orbit, gridBagConstraints);
		panel.add(orbit);

		JLabel LowerLatitude = new JLabel("Lower Latitude");
		LowerLatitude.setBounds(10, 229, 129, 24);
		LowerLatitude.setFont(new Font("Arial", 0, textSize20));

		JLabel UpperLatitude = new JLabel("Upper Latitude");
		UpperLatitude.setBounds(10, 229, 129, 24);
		UpperLatitude.setFont(new Font("Arial", 0, textSize20));

		JLabel LowerLongitude = new JLabel("Lower Longitude");
		LowerLongitude.setBounds(10, 270, 146, 24);
		LowerLongitude.setFont(new Font("Arial", 0, textSize20));

		JLabel UpperLongitude = new JLabel("Upper Longitude");
		UpperLongitude.setBounds(10, 312, 144, 26);
		UpperLongitude.setFont(new Font("Arial", 0, textSize20));

		gridBagLayout.setConstraints(LowerLatitude, gridBagConstraints);
		panel.add(LowerLatitude);
		lowerLatitude.setBounds(207, 549, -116, 295);
		lowerLatitude.setFont(new Font("Arial", 0, textSize20));
		lowerLatitude.setToolTipText("Enter a lower latitude.");
		lowerLatitude.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text); // to fix
					if (value < -180 || value > 180) {
						lowerLatitude.setText("");
					} else {
						lowerLatitudeValue = value;
					}
					return true;

				} catch (NumberFormatException e) {
					lowerLatitude.setText("");
					return false;
				}
			}
		});
		gridBagLayout.setConstraints(lowerLatitude, gridBagConstraints);
		panel.add(lowerLatitude);

		gridBagLayout.setConstraints(UpperLatitude, gridBagConstraints);
		panel.add(UpperLatitude);
		upperLatitude.setBounds(217, 226, 166, 30);
		upperLatitude.setFont(new Font("Arial", 0, textSize20));
		upperLatitude.setToolTipText("Enter a upper latitude.");
		upperLatitude.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text); // to fix
					if (value < -180 || value > 180) {
						upperLatitude.setText("");
					} else {
						upperLatitudeValue = value;
					}
					return true;

				} catch (NumberFormatException e) {
					upperLatitude.setText("");
					return false;
				}
			}
		});
		gridBagLayout.setConstraints(upperLatitude, gridBagConstraints);
		panel.add(upperLatitude);

		gridBagLayout.setConstraints(UpperLongitude, gridBagConstraints);
		panel.add(UpperLongitude);
		upperLongitude.setBounds(217, 308, 166, 30);
		upperLongitude.setFont(new Font("Arial", 0, textSize20));
		upperLongitude.setToolTipText("Enter a upper longitude.");
		upperLongitude.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text); // to fix
					if (value < -90 || value > 90) {
						upperLongitude.setText("");
					} else {
						upperLongitudeValue = value;
					}
					return true;

				} catch (NumberFormatException e) {
					upperLongitude.setText("");
					return false;
				}
			}
		});
		gridBagLayout.setConstraints(upperLongitude, gridBagConstraints);
		panel.add(upperLongitude);

		gridBagLayout.setConstraints(LowerLongitude, gridBagConstraints);
		panel.add(LowerLongitude);
		lowerLongitude.setBounds(217, 267, 166, 30);
		lowerLongitude.setFont(new Font("Arial", 0, textSize20));
		lowerLongitude.setToolTipText("Enter a lower Longitude.");
		lowerLongitude.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				String text = ((JTextField) input).getText();
				try {
					Integer value = Integer.valueOf(text); // to fix
					if (value < -90 || value > 90) {
						lowerLongitude.setText("");
					} else {
						lowerLongitudeValue = value;
					}
					return true;
				} catch (NumberFormatException e) {
					lowerLongitude.setText("");
					return false;
				}
			}
		});
		gridBagLayout.setConstraints(lowerLongitude, gridBagConstraints);
		panel.add(lowerLongitude);

		// Start Date
		JLabel startDateLabel = new JLabel(
				"Starting date (MM/dd/YYYY) and time (24 hour HH:mm:ss)");
		startDateLabel.setBounds(10, 397, 365, 71);
		startDateLabel.setFont(new Font("Arial", Font.PLAIN, textSize20));
		gridBagLayout.setConstraints(startDateLabel, gridBagConstraints);
		panel.add(startDateLabel);

		startTime = new JTextField();
		startTime.setBounds(10, 450, 212, 40);
		startTime
				.setToolTipText("Enter a date and time (24 hour) in the format MM/dd/YYYY HH:mm:ss.");
		startTime.setFont(new Font("Arial", Font.PLAIN, textSize20));
		gridBagLayout.setConstraints(startTime, gridBagConstraints);
		panel.add(startTime);

		// End Date
		JLabel endDatelabel = new JLabel(
				"Ending date (MM/dd/YYYY) and time (24 hour HH:mm:ss)");
		endDatelabel.setBounds(10, 514, 511, 24);
		endDatelabel.setFont(new Font("Arial", Font.PLAIN, textSize20));
		gridBagLayout.setConstraints(endDatelabel, gridBagConstraints);
		panel.add(endDatelabel);

		endTime = new JTextField();
		endTime.setBounds(10, 561, 212, 40);
		endTime.setToolTipText("Enter a date and time (24 hour) in the format MM/dd/YYYY HH:mm:ss.");
		endTime.setFont(new Font("Arial", Font.PLAIN, textSize20));
		gridBagLayout.setConstraints(endTime, gridBagConstraints);
		panel.add(endTime);

		JButton btnRunQuery = new JButton("Run Query");
		btnRunQuery.setBounds(188, 641, 89, 23);
		gridBagLayout.setConstraints(btnRunQuery, gridBagConstraints);
		panel.add(btnRunQuery);

		gcTableScrollPane.getViewport().add(simulationTable);
		contentPane.add(gcTableScrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// int textSize = 15;

		btnRunQuery.setFont(new Font("Arial", 0, 20));
		btnRunQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryParameters queryParameters = new QueryParameters();
				queryParameters.setName(simulationName.getText());
				queryParameters.setTilt(tiltValue);
				queryParameters.setOrbit(OrbitValue);
				queryParameters.setLowerLatitude(lowerLatitudeValue);
				queryParameters.setUpperLatitude(upperLatitudeValue);
				queryParameters.setLowerLongitude(lowerLongitudeValue);
				queryParameters.setUpperLongitude(upperLongitudeValue);

				queryParameters.setStartDate(startDate);
				queryParameters.setEndDate(endDate);

				simulationListModel.removeAllElements();
				List<SimulationStorage> simulationList = dataManager
						.readSimulation(queryParameters);
				if (simulationList != null) {
					for (SimulationStorage storage : simulationList) {
						simulationListModel.addElement(storage);
					}
				}
			}
		});

	}

	class JListSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {

			// Clear all rows
			for (int rowIndex = simulationTableModel.getRowCount() - 1; rowIndex >= 0; rowIndex--) {
				simulationTableModel.removeRow(rowIndex);
			}
			int selectedIndex = simulationList.getSelectedIndex();
			SimulationStorage selectedSimulationStorage = (SimulationStorage) simulationListModel
					.get(selectedIndex);
			// Load new rows
			if (selectedSimulationStorage != null) {
				for (GridCellStorage gridCell : selectedSimulationStorage
						.getGridCells()) {
					Object[] gridCellGetters = new Object[] {
							gridCell.getLatitude(), gridCell.getLongitude(),
							gridCell.getTemperature(), gridCell.getTime() };
					simulationTableModel.addRow(gridCellGetters);
				}
			}
		}
	}
}
