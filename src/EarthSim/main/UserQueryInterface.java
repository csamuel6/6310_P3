package EarthSim.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import EarthSim.persistance.DataManager;
import EarthSim.persistance.GridCellStorage;
import EarthSim.persistance.QueryParameters;
import EarthSim.persistance.SimulationStorage;

import java.awt.GridLayout;

import javax.swing.JTextArea;

public class UserQueryInterface extends JFrame {

	private JPanel contentPane;
	DataManager dataManager = new DataManager();
	JTextField simulationName = new JTextField();
	JTextField simulationLength = new JTextField();
	private JScrollPane gcTableScrollPane = new JScrollPane();
	final JButton start = new JButton();
	JPanel rightPanel = new JPanel();
	private JLabel time = new JLabel();
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
	int textSize20 = 15;
	JTextField gridSize = new JTextField();
	JTextField simTimeStep = new JTextField();
	JTextField tilt = new JTextField();
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
		contentPane.add(panel, BorderLayout.WEST);

		simulationList = new javax.swing.JList(simulationListModel);
		simulationList.setBounds(100, 53, 262, 119);
		simulationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel listSelectionModel = simulationList
				.getSelectionModel();
		listSelectionModel
				.addListSelectionListener(new JListSelectionHandler());

		List<SimulationStorage> simulationStorageList = dataManager
				.getAllSimulations();

		for (SimulationStorage simulationStorage : simulationStorageList) {
			simulationListModel.addElement(simulationStorage);
		}

		// Set up column for storing cell data
		simulationTableModel.addColumn("Latitude");
		simulationTableModel.addColumn("Longitude");
		simulationTableModel.addColumn("Temperature");
		simulationTableModel.addColumn("Time");

		simulationList.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(simulationList);

		JLabel name = new JLabel("Name:");
		// name.setBounds(10, 62, 57, 24);
		name.setFont(new Font("Arial", 0, textSize20));
		panel.add(name);

		simulationName.setFont(new Font("Arial", 0, textSize20));
		simulationName.setToolTipText("Enter a name.");
		panel.add(simulationName);
		
		JLabel LowerLatitude = new JLabel("Lower Latitude");
		LowerLatitude.setBounds(10, 229, 129, 24);
		LowerLatitude.setFont(new Font("Arial", 0, textSize20));
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
					}

					try {
						Integer.valueOf(lowerLatitude.getText());
					} catch (NumberFormatException e) {
						lowerLatitude.setText("");
					}
					return true;

				} catch (NumberFormatException e) {
					simulationLength.setText("");
					return false;
				}
			}
		});

		JLabel UpperLatitude = new JLabel("Upper Latitude");
		UpperLatitude.setBounds(10, 229, 129, 24);
		UpperLatitude.setFont(new Font("Arial", 0, textSize20));

		JLabel LowerLongitude = new JLabel("Lower Longitude");
		LowerLongitude.setBounds(10, 270, 146, 24);
		LowerLongitude.setFont(new Font("Arial", 0, textSize20));

		JLabel UpperLongitude = new JLabel("Upper Longitude");
		UpperLongitude.setBounds(10, 312, 144, 26);
		UpperLongitude.setFont(new Font("Arial", 0, textSize20));

		panel.setLayout(new GridLayout(0, 1, 50, 5));

		panel.add(upperLongitude);
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
					}

					try {
						Integer.valueOf(upperLongitude.getText());
					} catch (NumberFormatException e) {
						upperLongitude.setText("");
					}
					return true;

				} catch (NumberFormatException e) {
					upperLongitude.setText("");
					return false;
				}
			}
		});
		panel.add(upperLongitude);

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
					}

					try {
						Integer.valueOf(lowerLongitude.getText());
					} catch (NumberFormatException e) {
						lowerLongitude.setText("");
					}
					return true;

				} catch (NumberFormatException e) {
					lowerLongitude.setText("");
					return false;
				}
			}
		});
		panel.add(lowerLongitude);

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
					}

					try {
						Integer.valueOf(upperLatitude.getText());
					} catch (NumberFormatException e) {
						upperLatitude.setText("");
					}
					return true;

				} catch (NumberFormatException e) {
					upperLatitude.setText("");
					return false;
				}
			}
		});
		panel.add(upperLatitude);

		JLabel startDateLabel = new JLabel(
				"Starting date (MM/dd/YYYY) and time (24 hour HH:mm:ss)");
		startDateLabel.setBounds(10, 397, 365, 71);
		startDateLabel.setFont(new Font("Arial", Font.PLAIN, textSize20));
		panel.add(startDateLabel);

		startTime = new JTextField();
		startTime.setBounds(10, 450, 212, 40);
		startTime
				.setToolTipText("Enter a date and time (24 hour) in the format MM/dd/YYYY HH:mm:ss.");
		startTime.setFont(new Font("Arial", Font.PLAIN, textSize20));
		panel.add(startTime);

		JLabel endDatelabel = new JLabel(
				"Ending date (MM/dd/YYYY) and time (24 hour HH:mm:ss)");
		endDatelabel.setBounds(10, 514, 511, 24);
		endDatelabel.setFont(new Font("Arial", Font.PLAIN, textSize20));
		panel.add(endDatelabel);

		endTime = new JTextField();
		endTime.setBounds(10, 561, 212, 40);
		endTime.setToolTipText("Enter a date and time (24 hour) in the format MM/dd/YYYY HH:mm:ss.");
		endTime.setFont(new Font("Arial", Font.PLAIN, textSize20));
		panel.add(endTime);

		JButton btnRanQuery = new JButton("Ran Query");
		btnRanQuery.setBounds(188, 641, 89, 23);
		panel.add(btnRanQuery);

		gcTableScrollPane.getViewport().add(simulationTable);
		contentPane.add(gcTableScrollPane, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		int textSize = 15;

		btnRanQuery.setFont(new Font("Arial", 0, textSize));
		btnRanQuery.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				QueryParameters queryParameters = new QueryParameters();
				queryParameters.setName(simulationName.getText());
				queryParameters.setGridSpacing(Integer.valueOf(gridSize
						.getText()));
				queryParameters.setTilt(Double.valueOf(tilt.getText()));
				queryParameters.setOrbit(Double.valueOf(orbit.getText()));
				queryParameters.setTimeStep(Integer.valueOf(simTimeStep
						.getText()));
				queryParameters.setLowerLatitude(Integer.valueOf(lowerLatitude
						.getText()));
				queryParameters.setUpperLatitude(Integer.valueOf(upperLatitude
						.getText()));
				queryParameters.setLowerLongitude(Integer
						.valueOf(lowerLongitude.getText()));
				queryParameters.setUpperLongitude(Integer
						.valueOf(upperLongitude.getText()));

				queryParameters.setStartDate(startDate);
				queryParameters.setEndDate(endDate);

				simulationListModel.removeAllElements();
				List<SimulationStorage> simulationList = dataManager
						.readSimulation(queryParameters);
				for (SimulationStorage storage : simulationList) {
					simulationListModel.addElement(storage);
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

			ListSelectionModel lsm = (ListSelectionModel) e.getSource();
			int firstIndex = e.getFirstIndex();
			int lastIndex = e.getLastIndex();

			if (!lsm.isSelectionEmpty()) {
				if (firstIndex != lastIndex) {
					try {
						throw new Exception(
								"Mismatch between selected list indexes.");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				SimulationStorage selectedSimulationStorage = (SimulationStorage) simulationListModel
						.get(firstIndex);
				// Load new rows
				if (selectedSimulationStorage != null) {
					for (GridCellStorage gridCell : selectedSimulationStorage
							.getGridCells()) {
						Object[] gridCellGetters = new Object[] {
								gridCell.getLatitude(),
								gridCell.getLongitude(),
								gridCell.getTemperature(), gridCell.getTime() };
						simulationTableModel.addRow(gridCellGetters);
					}
				}
			}
		}
	}
}
