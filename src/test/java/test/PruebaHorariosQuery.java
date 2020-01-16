package test;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.junit.Test;

import sql.querys.HibernateUtilHorarios;
import sql.querys.HorariosResponse;

public class PruebaHorariosQuery {
	@Test
	public void getTimeCondition() {

		Session dbsessionHorarios = HibernateUtilHorarios
				.getSessionFactoryHorarios().openSession();
		HorariosResponse hr = new HorariosResponse();
		String idEmpresa = "1";
		String idServicio = "1";
		hr.loadTimeCondition(dbsessionHorarios, idEmpresa, idServicio);
		assertNotNull(hr);
		System.out.println("Dentro horario: " + hr.estaDentroDeHorario());
		System.out.println("Audio Fuera Horario: " + hr.getAudioFueraHorario());

	}
}
