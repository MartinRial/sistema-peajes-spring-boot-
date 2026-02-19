package obligatorio.da.sistemaPeajes.dominio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Administrador extends Usuario {
    
    public Administrador(String cedula, String contrasena, String nombreCompleto) {
        super(cedula, contrasena, nombreCompleto);
    }
}

