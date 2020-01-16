/**
 * 
 */
package utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author davila
 * 
 */
@Entity
@Table(name = "serviceconfig")
public class ServiceConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	private String configKey;

	private String configType;

	private String configValue;

	public Integer getId() {
		return id;
	}

	public String getConfigKey() {
		return configKey;
	}

	public String getConfigType() {
		return configType;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

}
