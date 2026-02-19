package obligatorio.da.sistemaPeajes.dominio;

public interface EstrategiaBonificacion {
    double calcularBonificacion(Puesto puesto, double tarifaBase);
    String getNombre();
    String getPorcentaje();
}
