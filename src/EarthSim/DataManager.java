package EarthSim;

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
	
	public void store(HeatedEarthSimulation simulation) {
		if (simulation != null) {
			try {
				Transaction tx = session.beginTransaction();
				session.save(simulation);
				tx.commit();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
