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
@Table(name = "authlog", indexes = {
		@Index(name = "astuidIdx", columnList = "astUid", unique = false),
		@Index(name = "calldateIdx", columnList = "calldate", unique = false),
		@Index(name = "eventIdx", columnList = "event", unique = false),
		@Index(name = "idcrecerIdx", columnList = "idcrecer", unique = false) })
public class AuthLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	private String astUid;

	private Date calldate;

	private String idcrecer;

	private String event;

	private String value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAstUid() {
		return astUid;
	}

	public void setAstUid(String astUid) {
		this.astUid = astUid;
	}

	public Date getCalldate() {
		return calldate;
	}

	public void setCalldate(Date calldate) {
		this.calldate = calldate;
	}

	public String getIdcrecer() {
		return idcrecer;
	}

	public void setIdcrecer(String idcrecer) {
		this.idcrecer = idcrecer;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
