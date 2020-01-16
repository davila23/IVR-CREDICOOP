package utils;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the usuarioclave database table.
 * 
 */
@Entity
@Table(name = "usuarioclave")
public class Usuarioclave implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false)
	private Long idcrecer;

	@Column(nullable = false, length = 30)
	private String clave;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date fechamigracion;

	@Column(nullable = false)
	private Boolean migrado = false;

	public Usuarioclave() {
	}

	public long getIdcrecer() {
		return this.idcrecer;
	}

	public void setIdcrecer(long idcrecer) {
		this.idcrecer = idcrecer;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Date getFechamigracion() {
		return this.fechamigracion;
	}

	public void setFechamigracion(Date fechamigracion) {
		this.fechamigracion = fechamigracion;
	}

	public Boolean getMigrado() {
		return migrado;
	}

	public void setMigrado(Boolean migrado) {
		this.migrado = migrado;
	}

}