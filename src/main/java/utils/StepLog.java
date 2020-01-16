/**
 * 
 */
package utils;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author davila
 * 
 */
@Entity
@Table(name = "steplog", indexes = {
		@Index(name = "astuidIdx", columnList = "astUid", unique = false),
		@Index(name = "calldateIdx", columnList = "calldate", unique = false),
		@Index(name = "stepTypeIdx", columnList = "stepType", unique = false),
		@Index(name = "stepUUIDIdx", columnList = "uidstep", unique = false) })
public class StepLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	private String stepType;

	private String astUid;

	private String server;

	private Date calldate;

	private String description;

	private String valor;

	private String uidstep;

	public Integer getId() {
		return id;
	}

	public String getStepType() {
		return stepType;
	}

	public String getAstUid() {
		return astUid;
	}

	public Date getCalldate() {
		return calldate;
	}

	public String getDescription() {
		return description;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public void setAstUid(String astUid) {
		this.astUid = astUid;
	}

	public void setCalldate(Date calldate) {
		this.calldate = calldate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getUidstep() {
		return uidstep;
	}

	public void setUidstep(String uidstep) {
		this.uidstep = uidstep;
	}

}
