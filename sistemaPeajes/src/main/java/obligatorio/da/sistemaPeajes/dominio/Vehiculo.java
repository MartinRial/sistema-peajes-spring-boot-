package obligatorio.da.sistemaPeajes.dominio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vehiculo {

	private String matricula;
	private String modelo;
	private String color;
	private Categoria categoria;
	private Propietario propietario;

	public Vehiculo(String matricula, String modelo, String color, Categoria categoria) {
		this.matricula = matricula;
		this.modelo = modelo;
		this.color = color;
		this.categoria = categoria;
	}

	/**
	 * Obtiene la tarifa correspondiente para este vehículo en un puesto específico.
	 * 
	 * Principio GRASP: Experto de la Información
	 * El Vehículo es el experto porque conoce su propia categoría y puede solicitar
	 * al Puesto la tarifa que le corresponde según esa categoría.
	 * 
	 * @param puesto El puesto de peaje del cual obtener la tarifa
	 * @return La tarifa correspondiente a la categoría del vehículo, o null si no existe
	 */
	public Tarifa obtenerTarifaEn(Puesto puesto) {
		return puesto.getTarifaPorCategoria(this.categoria);
	}
}
