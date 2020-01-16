package sql.querys;

import main.Daemon;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtilHorarios {
	private static SessionFactory sessionFactory = buildSessionFactory();

	public static SessionFactory getSessionFactoryHorarios() {
		return sessionFactory;
	}

	private static SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration();

		configuration.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQLDialect");
		configuration.setProperty("hibernate.connection.driver_class",
				"com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url",
				Daemon.getConfig("HORARIOSCONNSTRING"));
		configuration.setProperty("hibernate.connection.username",
				Daemon.getConfig("HORARIOSUSER"));
		configuration.setProperty("hibernate.connection.password",
				Daemon.getConfig("HORARIOSPWD"));
		// configuration.setProperty("hibernate.connection.url","jdbc:mysql://10.64.42.57:3306/horarios?zeroDateTimeBehavior=convertToNull");
		// configuration.setProperty("hibernate.connection.username","lsantagata");
		// configuration.setProperty("hibernate.connection.password","luciano123");

		configuration.setProperty("hibernate.connection.autoReconnect", "true");
		configuration.setProperty("hibernate.connection.autoReconnectForPools",
				"true");
		configuration.setProperty(
				"hibernate.connection.is-connection-validation-required",
				"true");
		configuration.setProperty("hibernate.c3p0.min_size", "10");
		configuration.setProperty("hibernate.c3p0.max_size", "20");
		configuration.setProperty("hibernate.c3p0.acquire_increment", "1");
		// configuration.setProperty("hibernate.c3p0.idle_test_period","30");
		configuration.setProperty("hibernate.c3p0.max_statements", "50");
		configuration.setProperty("hibernate.c3p0.maxIdleTime", "80");
		configuration.setProperty("hibernate.c3p0.idleConnectionTestPeriod",
				"90");
		configuration.setProperty("hibernate.c3p0.checkoutTimeout", "10000");
		configuration.setProperty("hibernate.c3p0.timeout", "180");
		configuration.setProperty("hibernate.c3p0.preferredTestQuery",
				"SELECT 1");
		configuration.setProperty("hibernate.c3p0.testConnectionOnCheckout",
				"true");
		configuration.setProperty("hibernate.hbm2ddl.auto", "validate");

		StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
		serviceRegistryBuilder.applySettings(configuration.getProperties());
		ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		SessionFactory sessionFactory = configuration
				.buildSessionFactory(serviceRegistry);
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactoryHorarios().close();
	}
}
