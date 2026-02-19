package obligatorio.da.sistemaPeajes.servicios;

import java.util.ArrayList;
import java.util.List;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;

public class ServicioVehiculos {

    private final List<Vehiculo> vehiculos;

    public ServicioVehiculos() {
        this.vehiculos = new ArrayList<>();
    }

    /**
     * Registra un vehículo en el sistema y asegura que conozca a su propietario.
     */
    public void agregarVehiculo(Vehiculo vehiculo, Propietario propietario) throws PeajeException {
        
        for (Vehiculo v : vehiculos) {
            if (v.getMatricula().equalsIgnoreCase(vehiculo.getMatricula())) {
                throw new PeajeException("Ya existe un vehículo con esa matrícula");
            }
        }

        // Delegar la bidireccionalidad al propietario
        propietario.agregarVehiculo(vehiculo);

        //Agregar al registro global del sistema
        vehiculos.add(vehiculo);
    }

    /**
     * Busca un vehículo por matrícula y retorna el vehículo y su propietario.
     * Curso alternativo: no existe ese vehículo.
     */
    public Vehiculo buscarVehiculoPorMatricula(String matricula) throws PeajeException {

        for (Vehiculo v : vehiculos) {
            if (v.getMatricula().equalsIgnoreCase(matricula)) {
                return v;
            }
        }

        throw new PeajeException("No existe el vehículo");
    }

    /**
     * Devuelve todos los vehículos del sistema.
     */
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }
}
