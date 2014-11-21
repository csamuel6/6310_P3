package EarthSim;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DataManager {
	SessionFactory sessionFactory;
	Session session;
	SimulationStorage _simulation;

	public DataManager() {
		Configuration config = new Configuration();
		config.configure();
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
				.applySettings(config.getProperties());
		sessionFactory = new Configuration().configure().buildSessionFactory(
				registryBuilder.build());
		session = sessionFactory.openSession();
	}

	private void storeCells(List<GridCellStorage> gridCell) {
		if (gridCell != null) {
			try {
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < gridCell.size(); i++) {
					session.save(gridCell.get(i));
				}
				tx.commit();
				session.clear();
			} catch (Exception ex) {
				session.clear();
				session.close();
				ex.printStackTrace();
			}
		}
	}

	public void storeSimulationCells() {
		this.storeCells(_simulation.getGridCells());
	}

	public void storeSimulation() {
		if (_simulation != null) {
			try {
				Transaction tx = session.beginTransaction();
				session.save(_simulation);
				tx.commit();
				session.clear();
			} catch (Exception ex) {
				session.clear();
				session.close();
				ex.printStackTrace();
			}
		}
	}

	public SimulationStorage readSimulation(QueryParameters queryParameters) {
		SimulationStorage sim = null;
		if (queryParameters != null) {
			String name = "";
			String sql = "FROM SimulationInfo sim WHERE sim.name = :simulationName";
			sql += "and sim.eccentricity = " + queryParameters.getOrbit();
			double tilt = 0;
			sql += "and sim.axialTilt = " + tilt;
			int gridSpacing = 0;
			sql += "and sim.gridSpacing = " + gridSpacing;
			int timeStep = 0;
			sql += "and sim.timeStep = " + timeStep;
//			int length = 0;
//			sql += "and sim.simulationLength = " + length;
			Query query = session.createQuery(sql);
			query.setParameter("simulationName", name);
			List list = query.list();

			sim = (SimulationStorage) list;
			sim.setGridCells(this.readGridCells(queryParameters));
		}
		return sim;
	}

	private List<GridCellStorage> readGridCells(QueryParameters queryParameters) {
		List<GridCellStorage> gcStorage = null;
		if (queryParameters != null) {
			gcStorage = new ArrayList<GridCellStorage>();
			String sql = "FROM CellData GridCell WHERE GridCell.Time >= :StartDate and GridCell.Time <=:EndDate";
			sql += "and GridCell.xCoordinate >= "
					+ queryParameters.getLowerXCoordinate()
					+ "and GridCell.xCoordinate <= "
					+ queryParameters.getUpperXCoordinate();

			sql += "and GridCell.yCoordinate >= "
					+ queryParameters.getLowerYCoordinate()
					+ " and GridCell.yCoordinate <= "
					+ queryParameters.getUpperYCoordinate();
			Query query = session.createQuery(sql);
			query.setParameter("StartDate", queryParameters.getStartDate());
			query.setParameter("EndDate", queryParameters.getEndDate());
		}
		return gcStorage;
	}

	public SimulationStorage getSimulation() {
		return _simulation;
	}

	public void setSimulation(SimulationStorage _simulation) {
		this._simulation = _simulation;
	}
}
