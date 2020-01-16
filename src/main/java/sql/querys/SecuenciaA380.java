package sql.querys;

import org.hibernate.Query;
import org.hibernate.Session;

import utils.HibernateUtil2;

public class SecuenciaA380 {
	public static String obtenerSecuenciaA380() {
		String retval;
		Session session = HibernateUtil2.getSessionFactory().openSession();
		session.getTransaction().begin();
		Query query = session.createSQLQuery("select nextval('opertx_seq')");
		Object result = query.uniqueResult();
		session.getTransaction().commit();
		if (result == null)
			retval = String.format("%06d", 0);
		else
			retval = String.format("%06d", Integer.parseInt(result.toString()));
		session.close();
		return retval;

	}
}
