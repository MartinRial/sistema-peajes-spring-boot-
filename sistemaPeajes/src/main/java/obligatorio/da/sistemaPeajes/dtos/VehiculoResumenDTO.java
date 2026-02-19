package obligatorio.da.sistemaPeajes.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;

@Getter
@Setter
@NoArgsConstructor
public class VehiculoResumenDTO {

    private String matricula;
    private String modelo;
    private String color;
    private String categoria;
    private int cantidadTransitos;
    private double totalGastado;

    public VehiculoResumenDTO(Vehiculo vehiculo, int cantidadTransitos, double totalGastado) {
        this.matricula = vehiculo.getMatricula();
        this.modelo = vehiculo.getModelo();
        this.color = vehiculo.getColor();
        this.categoria = vehiculo.getCategoria().getNombre();
        this.cantidadTransitos = cantidadTransitos;
        this.totalGastado = totalGastado;
    }
}
