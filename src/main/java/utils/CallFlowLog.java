package utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "callflowlog", indexes = { @Index(name = "astuidIdx", columnList = "astUid", unique = false) })
public class CallFlowLog {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	private String astUid;

	private String callFlowClassName;

	private String callerId;

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

	public String getCallFlowClassName() {
		return callFlowClassName;
	}

	public void setCallFlowClassName(String callFlowClassName) {
		this.callFlowClassName = callFlowClassName;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

}
