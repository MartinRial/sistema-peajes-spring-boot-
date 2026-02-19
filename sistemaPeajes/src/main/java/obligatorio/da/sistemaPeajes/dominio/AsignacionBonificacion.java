package obligatorio.da.sistemaPeajes.dominio;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignacionBonificacion {
    
    private EstrategiaBonificacion bonificacion;
    private Puesto puesto;
    private Date fechaAsignada;
    
    public AsignacionBonificacion(EstrategiaBonificacion bonificacion, Puesto puesto, Date fechaAsignada) {
        this.bonificacion = bonificacion;
        this.puesto = puesto;
        this.fechaAsignada = fechaAsignada;
    }
    
    /**
     * Calcula la bonificación aplicada.
     * @param tarifaBase
     * @return 
     */
    public double calcularBonificacion(double tarifaBase) {
        return bonificacion.calcularBonificacion(puesto, tarifaBase);
    }
}