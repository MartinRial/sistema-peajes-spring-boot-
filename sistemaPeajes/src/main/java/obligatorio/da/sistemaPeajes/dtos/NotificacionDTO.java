package obligatorio.da.sistemaPeajes.dtos;

import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.Notificacion;

/**
 * DTO para transferir información de notificaciones.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {
    
    private String fechaHora;
    private String mensaje;
    
    /**
     * Constructor que crea un DTO a partir de una entidad Notificacion.
     * 
     * @param notificacion La entidad Notificacion de dominio
     */
    public NotificacionDTO(Notificacion notificacion) {
        this.fechaHora = formatearFechaHora(notificacion.getFechaHora());
        this.mensaje = notificacion.getMensaje();
    }
    
    /**
     * Formatea la fecha y hora como dd/MM/yyyy HH:mm:ss
     * 
     * @param fechaHora La fecha y hora a formatear
     * @return La fecha y hora formateada como String
     */
    private String formatearFechaHora(java.util.Date fechaHora) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(fechaHora);
    }
}