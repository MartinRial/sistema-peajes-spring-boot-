package obligatorio.da.sistemaPeajes.dominio;

/**
 * Estrategia de bonificación "Trabajador".
 *
 * Regla (enunciado): tienen un 80% de descuento si el tránsito por el
 * puesto asignado se realiza en un día de semana. Es decir, el
 * propietario paga solo el 20% de la tarifa base.
 *
 * Nota: como la interfaz de estrategia recibe únicamente el puesto y la
 * tarifa base, la fecha/hora del tránsito deberá evaluarse en la capa de
 * servicio correspondiente. Esa capa decidirá si corresponde aplicar la
 * bonificación de Trabajador (tránsito en día hábil y por el puesto
 * asignado) y, en caso afirmativo, invocará esta estrategia, que solo
 * se encarga de aplicar el 80% de descuento sobre la tarifa recibida.
 */
public class Trabajador implements EstrategiaBonificacion {

    @Override
    public double calcularBonificacion(Puesto puesto, double tarifaBase) {
        // Trabajador: 80% de descuento -> el propietario paga el 20%.
        return tarifaBase * 0.20;
    }

    @Override
    public String getNombre() {
        return "Trabajador";
    }

    @Override
    public String getPorcentaje() {
        return "80%";
    }
}
