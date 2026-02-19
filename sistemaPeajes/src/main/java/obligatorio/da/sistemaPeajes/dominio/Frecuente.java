package obligatorio.da.sistemaPeajes.dominio;

/**
 * Estrategia de bonificación "Frecuente".
 *
 * Regla (enunciado): tienen un 50% de descuento a partir del segundo
 * tránsito realizado en el día por un puesto determinado con el mismo
 * vehículo. En el primer tránsito del día (para cada vehículo) no hay
 * descuento.
 *
 * Nota: la decisión de si corresponde o no el 50% (es decir, si es el
 * primer o el segundo tránsito del día) debe tomarse en una capa de
 * servicio, que calculará la tarifa base a cobrar y, en caso de que
 * corresponda aplicar la bonificación, invocará esta estrategia con la
 * tarifa ya calculada. Por eso aquí simplemente se aplica el 50% a la
 * tarifa recibida.
 */
public class Frecuente implements EstrategiaBonificacion {

    @Override
    public double calcularBonificacion(Puesto puesto, double tarifaBase) {
        // Frecuente: aplica un 50% de descuento sobre la tarifa base.
        return tarifaBase * 0.5;
    }

    @Override
    public String getNombre() {
        return "Frecuente";
    }

    @Override
    public String getPorcentaje() {
        return "50%";
    }
}
