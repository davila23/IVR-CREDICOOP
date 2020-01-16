package utils;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;

/**
 * The persistent class for the IVRCMoraTemprana database table.
 * 
 */
@Entity
@Table(name = "IVRCMoraTemprana")
public class IVRCMoraTemprana implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "Horario", nullable = false, length = 20)
	private String horario;

	@Column(nullable = false)
	private int id_Estudio;

	@Id
	@Column(nullable = false, columnDefinition = "bigint(20)")
	private BigInteger id_T24;

	@Column(name = "OnLine", nullable = false, columnDefinition = "BIT")
	private byte onLine;

	@Column(nullable = false, length = 14)
	private String tel_Estudio;

	public IVRCMoraTemprana() {
	}

	public String getHorario() {
		return this.horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public int getId_Estudio() {
		return this.id_Estudio;
	}

	public void setId_Estudio(int id_Estudio) {
		this.id_Estudio = id_Estudio;
	}

	public BigInteger getId_T24() {
		return this.id_T24;
	}

	public void setId_T24(BigInteger id_T24) {
		this.id_T24 = id_T24;
	}

	public byte getOnLine() {
		return this.onLine;
	}

	public void setOnLine(byte onLine) {
		this.onLine = onLine;
	}

	public String getTel_Estudio() {
		return this.tel_Estudio;
	}

	public void setTel_Estudio(String tel_Estudio) {
		this.tel_Estudio = tel_Estudio;
	}

}