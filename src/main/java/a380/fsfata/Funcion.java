package a380.fsfata;

public class Funcion {
	private String codigo = "";
	private String fs = "";
	private String fa = "";
	private String ta = "";

	public Funcion(String codigo, String fs, String fa, String ta) {
		super();
		this.codigo = codigo;
		this.fs = fs;
		this.fa = fa;
		this.ta = ta;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getFs() {
		return fs;
	}

	public void setFs(String fs) {
		this.fs = fs;
	}

	public String getFa() {
		return fa;
	}

	public void setFa(String fa) {
		this.fa = fa;
	}

	public String getTa() {
		return ta;
	}

	public void setTa(String ta) {
		this.ta = ta;
	}

}
