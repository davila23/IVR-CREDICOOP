/**
 * 
 */
package utils;

import java.util.Date;

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
@Table(name = "contextvarlog", indexes = {
		@Index(name = "astuidIdx", columnList = "astUid", unique = false),
		@Index(name = "calldateIdx", columnList = "calldate", unique = false),
		@Index(name = "stepUUIDIdx", columnList = "uidstep", unique = false) })
public class ContextVarLog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	private String astUid;

	private Date calldate;

	private String description;

	private String value;

	private String uidstep;

	public Integer getId() {
		return id;
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

	public String getValue() {
		return value;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setValue(String value) {
		this.value = value;
	}

	public String getUidstep() {
		return uidstep;
	}

	public void setUidstep(String uidstep) {
		this.uidstep = uidstep;
	}
}
