package EarthSim.persistance;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DataManager {
	SessionFactory sessionFactory;
	Session session;
	
	public DataManager() {
		Configuration config = new Configuration();
		config.configure();
		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
				.applySettings(config.getProperties());
		sessionFactory = new Configuration().configure()
				.buildSessionFactory(registryBuilder.build());
		 session = sessionFactory.openSession();
	}
	
	public void store(List<GridCellStorage> gridCell) {
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
	
	public void store(SimulationStorage simulation) {
		if (simulation != null) {
			try {
				Transaction tx = session.beginTransaction();
				session.save(simulation);
				tx.commit();
				session.clear();
			} catch (Exception ex) {
				session.clear();
				session.close();
				ex.printStackTrace();
			}
		}
	}
}
