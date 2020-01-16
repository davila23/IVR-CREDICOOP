package utils;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the usuariocuenta database table.
 * 
 */
@Entity
@Table(name = "usuariocuenta")
public class Usuariocuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private long id;

	@Column(nullable = false, length = 50)
	private String cuenta;

	@Column(nullable = false)
	private long idcrecer;

	public Usuariocuenta() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCuenta() {
		return this.cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public long getIdcrecer() {
		return this.idcrecer;
	}

	public void setIdcrecer(long idcrecer) {
		this.idcrecer = idcrecer;
	}

}