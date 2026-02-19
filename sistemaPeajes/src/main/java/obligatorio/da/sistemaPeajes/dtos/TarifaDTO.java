package obligatorio.da.sistemaPeajes.dtos;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.Tarifa;

@Getter
@Setter
@AllArgsConstructor
public class TarifaDTO {
    private final String categoría;
    private final String monto;
    
    /**
     * Constructor que crea un DTO a partir de una entidad Tarifa.
     * 
     * @param tarifa La entidad Tarifa de dominio
     */
    public TarifaDTO(Tarifa tarifa) {
        this.categoría = tarifa.getCategoria().getNombre();
        this.monto = formatearMonto(tarifa.getMonto());
    }
    
    /**
     * Formatea el monto como $ 120,00
     * 
     * @param monto El monto a formatear
     * @return El monto formateado como String
     */
    private String formatearMonto(double monto) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        
        DecimalFormat df = new DecimalFormat("$ #,##0.00", symbols);
        return df.format(monto);
    }
}
