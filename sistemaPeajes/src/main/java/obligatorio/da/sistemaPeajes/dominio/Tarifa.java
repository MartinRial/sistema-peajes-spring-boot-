package obligatorio.da.sistemaPeajes.dominio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tarifa {

	private double monto;
	private Categoria categoria;

	public Tarifa(double monto, Categoria categoria) {
		this.monto = monto;
		this.categoria = categoria;
	}

	public boolean tieneCategoria(Categoria categoria) {
		return this.categoria.equals(categoria);
	}
}
