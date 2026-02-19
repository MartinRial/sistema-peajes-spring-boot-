package obligatorio.da.sistemaPeajes.dominio;

/**
 * Estrategia de bonificación "Exonerado".
 *
 * Regla: el propietario exonerado no paga el tránsito en el puesto
 * correspondiente, por lo que el monto a pagar siempre es 0.
 */
public class Exonerado implements EstrategiaBonificacion {

    @Override
    public double calcularBonificacion(Puesto puesto, double tarifaBase) {
        // Exonerado: 100% de descuento, no paga el tránsito.
        return 0.0;
    }

    @Override
    public String getNombre() {
        return "Exonerado";
    }

    @Override
    public String getPorcentaje() {
        return "100%";
    }
}
