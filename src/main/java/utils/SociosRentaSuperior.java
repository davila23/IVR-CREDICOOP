package utils;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * The persistent class for the SociosRentaSuperior database table.
 * 
 */
@Entity
@Table(name = "SociosRentaSuperior")
public class SociosRentaSuperior implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private Timestamp alta;

	@Id
	@Column(nullable = false)
	private int idcrecer;

	@Column(nullable = false, columnDefinition = "char(1)")
	private String vip;

	public SociosRentaSuperior() {
	}

	public Timestamp getAlta() {
		return this.alta;
	}

	public void setAlta(Timestamp alta) {
		this.alta = alta;
	}

	public int getIdcrecer() {
		return this.idcrecer;
	}

	public void setIdcrecer(int idcrecer) {
		this.idcrecer = idcrecer;
	}

	public String getVip() {
		return this.vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

}