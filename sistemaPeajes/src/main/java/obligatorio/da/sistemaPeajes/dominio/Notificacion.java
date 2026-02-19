package obligatorio.da.sistemaPeajes.dominio;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notificacion {
    
    private Date fechaHora;
    private String mensaje;
    
    public Notificacion(Date fechaHora, String mensaje) {
        this.fechaHora = fechaHora;
        this.mensaje = mensaje;
    }
}