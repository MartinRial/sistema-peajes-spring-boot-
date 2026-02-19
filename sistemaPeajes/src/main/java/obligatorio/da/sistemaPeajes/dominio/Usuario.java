package obligatorio.da.sistemaPeajes.dominio;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Usuario {

	private String cedula;

	@Getter(AccessLevel.NONE)
	private String contrasena;

	private String nombreCompleto;


	public Usuario(String cedula, String contrasena, String nombreCompleto) {
		this.cedula = cedula;
		this.contrasena = contrasena;
		this.nombreCompleto = nombreCompleto;
	}
	
    @Override
    public String toString() {
        return nombreCompleto;
    }

	public boolean esPasswordCorrecto(String password) {
		return this.contrasena.equals(password);
	}
}

//falta estado usuario. enum?