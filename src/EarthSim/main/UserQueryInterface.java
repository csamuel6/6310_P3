package EarthSim.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import org.hibernate.engine.spi.QueryParameters;

import EarthSim.persistance.DataManager;
import EarthSim.persistance.SimulationStorage;

import java.awt.GridLayout;

import javax.swing.JTextArea;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.SpringLayout;

//import net.miginfocom.swing.MigLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
//
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.factories.FormFactory;
//import com.jgoodies.forms.layout.RowSpec;

import java.awt.List;

public class UserQueryInterface extends JFrame {

	private JPanel contentPane;
	JTextField simulationName = new JTextField();
	JTextField simulationLength = new JTextField();
	final JButton start = new JButton();
	JPanel rightPanel = new JPanel();
	private JLabel time = new JLabel();
	JTextField upperLatitude = new JTextField();
	JTextField lowerLongitude = new JTextField();
	JTextField upperLongitude = new JTextField();
	private JList list;
	private DefaultListModel listModel;
	Date startDate;
	Date endDate;
	int textSize = 20;
	int textSize20 = 20;
	JButton runQueryButton = new JButton("Run Query");
	private JTextField startTime;
	private JTextField lowerLattidue;
	private JTextField endTime;
	
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
		contentPane.setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		
		JLabel name = new JLabel("Name:");
		name.setBounds(10, 62, 57, 24);
		name.setFont(new Font("Arial", 0, textSize20));
//		lowerLatitude.setBounds(207, 549, -116, 295);
//		lowerLatitude.setFont(new Font("Arial", 0, textSize20));
//		lowerLatitude.setToolTipText("Enter a lower latitude.");
//		lowerLatitude.setInputVerifier(new InputVerifier() {
//			@Override
//			public boolean verify(JComponent input) {
//				String text = ((JTextField) input).getText();
//				try {
//					Integer value = Integer.valueOf(text); // to fix
//					if (value < -180 || value > 180) {
//						lowerLatitude.setText("");
//					}
//
//					try {
//						Integer.valueOf(lowerLatitude.getText());
//					} catch (NumberFormatException e) {
//						lowerLatitude.setText("");
//					}
//					return true;
//
//				} catch (NumberFormatException e) {
//					simulationLength.setText("");
//					return false;
//				}
//			}
//			});
//		
		JLabel UpperLatitude = new JLabel("Upper Latitude");
		UpperLatitude.setBounds(10, 229, 129, 24);
		UpperLatitude.setFont(new Font("Arial", 0, textSize20));
		
		JLabel LowerLongitude = new JLabel("Lower Longitude");
		LowerLongitude.setBounds(10, 270, 146, 24);
		LowerLongitude.setFont(new Font("Arial", 0, textSize20));
		
		JLabel UpperLongitude = new JLabel("Upper Longitude");
		UpperLongitude.setBounds(10, 312, 144, 26);
		UpperLongitude.setFont(new Font("Arial", 0, textSize20));
				panel.setLayout(null);
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
				
				JLabel startDateLabel = new JLabel("Starting date (MM/dd/YYYY) and time (24 hour HH:mm:ss)");
				startDateLabel.setBounds(10, 397, 365, 71);
				startDateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
				panel.add(startDateLabel);
				panel.add(name);
				panel.add(UpperLatitude);
				panel.add(LowerLongitude);
				panel.add(UpperLongitude);
				
				startTime = new JTextField();
				startTime.setBounds(10, 450, 212, 40);
				startTime.setToolTipText("Enter a date and time (24 hour) in the format MM/dd/YYYY HH:mm:ss.");
				startTime.setFont(new Font("Arial", Font.PLAIN, 20));
				panel.add(startTime);
				
				JButton btnRanQuery = new JButton("Ran Query");
				btnRanQuery.setBounds(188, 641, 89, 23);
				panel.add(btnRanQuery);
				
				JLabel label = new JLabel("Ending date (MM/dd/YYYY) and time (24 hour HH:mm:ss)");
				label.setBounds(10, 514, 511, 24);
				label.setFont(new Font("Arial", Font.PLAIN, 14));
				panel.add(label);
				
				JLabel label_1 = new JLabel("Lower Latitude");
				label_1.setBounds(10, 363, 131, 24);
				label_1.setFont(new Font("Arial", Font.PLAIN, 20));
				panel.add(label_1);
				
				lowerLattidue = new JTextField();
				lowerLattidue.setBounds(217, 357, 166, 30);
				lowerLattidue.setToolTipText("Enter a upper longitude.");
				lowerLattidue.setFont(new Font("Arial", Font.PLAIN, 20));
				panel.add(lowerLattidue);
				
				endTime = new JTextField();
				endTime.setBounds(10, 561, 212, 40);
				endTime.setToolTipText("Enter a date and time (24 hour) in the format MM/dd/YYYY HH:mm:ss.");
				endTime.setFont(new Font("Arial", Font.PLAIN, 20));
				panel.add(endTime);
				
				List simulationList = new List();
				simulationList.setBounds(93, 53, 262, 119);
				
				simulationList.add("testing");
				addSimulations(simulationList);
				
				
				panel.add(simulationList);
				
				JTextArea textArea = new JTextArea();
				contentPane.add(textArea);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		int textSize20 = 20;
		
		int textSize = 15;
		
		runQueryButton.setFont(new Font("Arial", 0, textSize));
		runQueryButton.setEnabled(false);
		runQueryButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				DataManager dataManager = new DataManager();
				QueryParameters queryParameters = new QueryParameters();
//				queryParameters.setName(simulationName.getText());
//				queryParameters.setGridSpacing(Integer.valueOf(gridSize.getText()));
//				queryParameters.setTilt(Double.valueOf(tilt.getText()));
//				queryParameters.setOrbit(Double.valueOf(orbit.getText()));
//				queryParameters.setTimeStep(Integer.valueOf(simTimeStep.getText()));
//				queryParameters.setLowerLatitude(-10);
//				queryParameters.setUpperLatitude(10);
//				queryParameters.setLowerLongitude(-60);
//				queryParameters.setUpperLongitude(60);

//				queryParameters.setStartDate(startDate);
//				queryParameters.setEndDate(endDate);
//				
//				listModel.removeAllElements();
//				List<SimulationStorage> simulationList = dataManager.readSimulation(queryParameters);
//				for (SimulationStorage storage : simulationList) {
//					listModel.addElement(storage);
//				}
//				queue.clear();
//				repaint();
//
//				display.setGridSize(Integer.valueOf(gridSize.getText()));
//				sim.setGridSize(Integer.valueOf(gridSize.getText()));
//				sim.setTimeStep(Integer.valueOf(simTimeStep.getText()));
//				sim.setTilt(Double.valueOf(tilt.getText()));
//				sim.setOrbit(Double.valueOf(orbit.getText()));
//				sim.setLength(Integer.valueOf(simulationLength.getText()));
//				sim.setName(simulationName.getText());
//				run();
			}
		});
		

		
		
		
		
	}
	
	
	public void addSimulations(List simulationList)
	{
		DataManager dataManager = new DataManager();
		QueryParameters queryParameters = new QueryParameters();
//		queryParameters.setName(simulationName.getText());
		
		
		java.util.List<SimulationStorage> list = dataManager.getAllSimulations();
		
		for (SimulationStorage s : list)
		{
			if (s.getName() != null && s.getName().length() > 0)
			{
				simulationList.add(s.getName());
			}
			
		}

	}
}
