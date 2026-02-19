package obligatorio.da.sistemaPeajes.dominio;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Puesto {

	private String nombre;
	private String direccion;
	private List<Tarifa> tarifas;

	public Puesto(String nombre, String direccion) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.tarifas = new ArrayList<>();
	}

	public Tarifa obtenerTarifaPorCategoria(Categoria categoria) {
		for (Tarifa tarifa : tarifas) {
			if (tarifa.getCategoria().equals(categoria)) {
				return tarifa;
			}
		}
		return null;
	}

	public Tarifa getTarifaPorCategoria(Categoria categoria) {
		for (Tarifa tarifa : tarifas) {
			if (tarifa.tieneCategoria(categoria)) {
				return tarifa;
			}
		}
		return null;
	}
}
