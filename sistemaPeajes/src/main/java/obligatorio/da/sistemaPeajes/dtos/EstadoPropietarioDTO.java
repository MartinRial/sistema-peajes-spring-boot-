package obligatorio.da.sistemaPeajes.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.EstadoPropietario;

/**
 * DTO para transferir información de estados de propietario.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstadoPropietarioDTO {
    
    private String nombre;
    
    /**
     * Constructor que crea un DTO a partir de un objeto EstadoPropietario.
     * 
     * @param estado El estado del propietario
     */
    public EstadoPropietarioDTO(EstadoPropietario estado) {
        this.nombre = estado.getNombre();
    }
}
