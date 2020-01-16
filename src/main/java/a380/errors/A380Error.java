package a380.errors;

public class A380Error {
	private String errorA380 = "";
	private String errorE352 = "";
	private String descripcion = "";

	public A380Error(String errorA380, String errorE352, String descripcion) {
		super();
		this.errorA380 = errorA380;
		this.errorE352 = errorE352;
		this.descripcion = descripcion;
	}

	public String getErrorA380() {
		return errorA380;
	}

	public void setErrorA380(String errorA380) {
		this.errorA380 = errorA380;
	}

	public String getErrorE352() {
		return errorE352;
	}

	public void setErrorE352(String errorE352) {
		this.errorE352 = errorE352;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
