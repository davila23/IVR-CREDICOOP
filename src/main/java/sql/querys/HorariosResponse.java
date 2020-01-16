package sql.querys;

import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HorariosResponse {
	private boolean dentroDeHorario = false;
	private String audioFueraHorario = "";

	public boolean estaDentroDeHorario() {
		return dentroDeHorario;
	}

	public String getAudioFueraHorario() {
		return audioFueraHorario;
	}

	public void loadTimeCondition(Session dbSession, String idEmpresa,
			String idServicio) {
		StringBuilder sqlFeriado = new StringBuilder();
		sqlFeriado
				.append("select time(now()) between f.horainicio and f.horafin as esHora, 1  as esDia , sf.audioFueraHorario");
		sqlFeriado.append("	from feriado f");
		sqlFeriado.append("		join servicioferiado sf on f.id=sf.idferiado");
		sqlFeriado.append("		join servicio s on s.id=sf.idservicio");
		sqlFeriado.append("		join empresa e on  e.id=s.idempresa");
		sqlFeriado.append("	where e.id=").append(idEmpresa);
		sqlFeriado
				.append(" and s.id=")
				.append(idServicio)
				.append(" and now() between date_format(f.fecha,'%Y-%m-%d 00:00') and  date_format(f.fecha,'%Y-%m-%d 23:59')");
		dbSession.getTransaction().begin();
		SQLQuery query = dbSession.createSQLQuery(sqlFeriado.toString());
		List<Object[]> entities = query.list();
		dbSession.getTransaction().commit();
		if (entities.size() > 0) {
			this.dentroDeHorario = entities.get(0)[0].toString().equals("1")
					&& entities.get(0)[1].toString().equals("1");
			this.audioFueraHorario = entities.get(0)[2].toString();
		} else {
			StringBuilder sqlHorario = new StringBuilder();
			sqlHorario
					.append("select  time(now()) between h.horainicio and h.horafin as hora ,case dayofweek(now()) when 1 then h.domingo ");
			sqlHorario.append(" when 2 then h.lunes ");
			sqlHorario.append(" when 3 then h.martes ");
			sqlHorario.append(" when 4 then h.miercoles ");
			sqlHorario.append(" when 5 then h.jueves ");
			sqlHorario.append(" when 6 then h.viernes ");
			sqlHorario.append(" when 7 then h.sabado ");
			sqlHorario.append(" end as dia, sh.audiofuerahorario ");
			sqlHorario.append(" from horario h ");
			sqlHorario.append(" join serviciohorario sh on h.id=sh.idhorario ");
			sqlHorario.append(" join servicio s on s.id=sh.idservicio ");
			sqlHorario.append(" join empresa e on  e.id=s.idempresa ");
			sqlHorario.append(" where e.id=").append(idEmpresa)
					.append(" and s.id=").append(idServicio);
			dbSession.getTransaction().begin();
			query = dbSession.createSQLQuery(sqlHorario.toString());
			entities = query.list();
			dbSession.getTransaction().commit();
			if (entities.size() > 0) {
				this.dentroDeHorario = entities.get(0)[0].toString()
						.equals("1")
						&& entities.get(0)[1].toString().equals("1");
				this.audioFueraHorario = entities.get(0)[2].toString();
			}
			dbSession.flush();
		}
	}
}
