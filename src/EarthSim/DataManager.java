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
				session = sessionFactory.openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < gridCell.size(); i++) {
					session.save(gridCell.get(i));
				}
				tx.commit();
				session.clear();
				session = sessionFactory.openSession();
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

	public List<SimulationStorage> readSimulation(QueryParameters queryParameters) {
		List<SimulationStorage> simList = null;
		if (queryParameters != null) {
			String sql = "FROM SimulationStorage AS sim";
			sql += " WHERE sim.eccentricity = " + queryParameters.getOrbit();
			sql += " AND sim.axialTilt = " + queryParameters.getTilt();
			sql += " AND sim.gridSpacing = " + queryParameters.getGridSpacing();
			sql += " AND sim.timeStep = " + queryParameters.getTimeStep();
			sql += " and sim.name = :simulationName";
			
			Query query = session.createQuery(sql);
			query.setParameter("simulationName", queryParameters.name);
			List list = query.list();

			if (list.isEmpty()) {
				return null;
			}
			simList = new ArrayList<SimulationStorage>();
			for (Object obj : list) {
				SimulationStorage sim = (SimulationStorage) obj;
				sim.setGridCells(this.readGridCells(queryParameters, sim));
				simList.add(sim);
			}
		}
		return simList;
	}

	private List<GridCellStorage> readGridCells(QueryParameters queryParameters, SimulationStorage methodSimulation) {
		List<GridCellStorage> gcStorage = null;
		if (queryParameters != null) {
			gcStorage = new ArrayList<GridCellStorage>();
			String sql = "FROM GridCellStorage GridCell";
			sql += " WHERE GridCell.latitude >= "
					+ queryParameters.getLowerLatitude()
					+ " AND GridCell.latitude <= "
					+ queryParameters.getUpperLatitude();

			sql += " AND GridCell.longitude >= "
					+ queryParameters.getLowerLongitude()
					+ " AND GridCell.longitude <= "
					+ queryParameters.getUpperLongitude();
			sql += " AND GridCell.storage.id = " + methodSimulation.getId();
			sql += " AND GridCell.time >= :StartDate AND GridCell.time <=:EndDate";
			Query query = session.createQuery(sql);
			query.setParameter("StartDate", queryParameters.getStartDate());
			query.setParameter("EndDate", queryParameters.getEndDate());
			
			List list = query.list();
			if (list.isEmpty()) {
				return null;
			}
			gcStorage = new ArrayList<GridCellStorage>();
			for (Object obj : list) {
				GridCellStorage gridCellStorage = (GridCellStorage) obj;
				gcStorage.add(gridCellStorage);
			}
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
