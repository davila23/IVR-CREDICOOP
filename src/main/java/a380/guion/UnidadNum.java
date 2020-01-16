package a380.guion;

public class UnidadNum {

	private String Singular;
	private String Plural;

	public UnidadNum(String singular, String plural) {
		super();
		Singular = singular;
		Plural = plural;
	}

	public String getSingular() {
		return Singular;
	}

	public void setSingular(String singular) {
		Singular = singular;
	}

	public String getPlural() {
		return Plural;
	}

	public void setPlural(String plural) {
		Plural = plural;
	}

}
