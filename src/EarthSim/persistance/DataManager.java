package EarthSim.persistance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
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

	public List<SimulationStorage> getAllSimulations() {
		List<SimulationStorage> simList = null;

		String sql = "FROM SimulationStorage AS sim";
		sql += " ORDER BY name";

		Query query = session.createQuery(sql);

		List<SimulationStorage> list = query.list();

		if (list.isEmpty()) {
			return null;
		}
		simList = new ArrayList<SimulationStorage>();
		for (Object obj : list) {
			SimulationStorage sim = (SimulationStorage) obj;
			sim.setGridCells(this.readAllGridCells(sim));
			simList.add(sim);
		}

		return simList;
	}

	public List<SimulationStorage> readSimulation(
			QueryParameters queryParameters) {
		List<SimulationStorage> simList = null;
		if (queryParameters != null) {
			String sql = "FROM SimulationStorage AS sim";
			
			List<String> sqlCriteria = new ArrayList<String>();
			if (queryParameters.getOrbit() != null) {
				sqlCriteria.add(" sim.eccentricity = "
						+ queryParameters.getOrbit());
			}
			if (queryParameters.getTilt() != null) {
				sqlCriteria.add(" sim.axialTilt = " + queryParameters.getTilt());
			}
			if (queryParameters.getName() != null) {
				sqlCriteria.add(" sim.name = :simulationName");
			}
			
			sql += this.createSQLStatement(sqlCriteria);
			sql += " ORDER BY GeoPrecision, TemporalPrecision, name";

			Query query = session.createQuery(sql);
			if (queryParameters.getName() != null) {
				query.setParameter("simulationName", queryParameters.getName());
			}
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

	private List<GridCellStorage> readAllGridCells(
			SimulationStorage methodSimulation) {
		List<GridCellStorage> gcStorage = null;

		gcStorage = new ArrayList<GridCellStorage>();
		String sql = "FROM GridCellStorage AS GridCell";
		sql += " WHERE GridCell.storage.id = " + methodSimulation.getId();
		Query query = session.createQuery(sql);
		List list = query.list();
		if (list.isEmpty()) {
			return null;
		}
		for (Object obj : list) {
			GridCellStorage gridCellStorage = (GridCellStorage) obj;
			gcStorage.add(gridCellStorage);
		}

		return gcStorage;
	}

	private List<GridCellStorage> readGridCells(
			QueryParameters queryParameters, SimulationStorage methodSimulation) {
		List<GridCellStorage> gcStorage = null;
		if (queryParameters != null) {
			gcStorage = new ArrayList<GridCellStorage>();
			String sql = "FROM GridCellStorage GridCell";
			sql += " WHERE GridCell.storage.id = " + methodSimulation.getId();
			if (queryParameters.getLowerLatitude() != null) {
				sql += " AND GridCell.latitude >= "
						+ queryParameters.getLowerLatitude();
			}
			if (queryParameters.getUpperLatitude() != null) {
				sql += " AND GridCell.latitude <= "
						+ queryParameters.getUpperLatitude();
			}
			if (queryParameters.getLowerLongitude() != null) {
				sql += " AND GridCell.longitude >= "
						+ queryParameters.getLowerLongitude();
			}
			if (queryParameters.getUpperLongitude() != null) {
				sql += " AND GridCell.longitude <= "
						+ queryParameters.getUpperLongitude();
			}
			if (queryParameters.getStartDate() != null) {
				sql += " AND GridCell.time >= :StartDate";
			}
			if (queryParameters.getEndDate() != null) {
				sql += " AND GridCell.time <= :EndDate";
			}
			Query query = session.createQuery(sql);

			if (queryParameters.getStartDate() != null) {
				query.setParameter("StartDate", queryParameters.getStartDate());
			}
			if (queryParameters.getEndDate() != null) {
				query.setParameter("EndDate", queryParameters.getEndDate());
			}
			List list = query.list();
			if (list.isEmpty()) {
				return null;
			}
			for (Object obj : list) {
				GridCellStorage gridCellStorage = (GridCellStorage) obj;
				gcStorage.add(gridCellStorage);
			}
		}
		return gcStorage;
	}

	private String createSQLStatement(List<String> filterCriteria) {
		String returnString = "";
		for (String string : filterCriteria) {
			returnString += " AND " + string;
		}
		if (!returnString.isEmpty()) {
			returnString = "WHERE " + returnString;
		}
		return returnString;
	}

	public SimulationStorage getSimulation() {
		return _simulation;
	}

	public void setSimulation(SimulationStorage _simulation) {
		this._simulation = _simulation;
	}
}
