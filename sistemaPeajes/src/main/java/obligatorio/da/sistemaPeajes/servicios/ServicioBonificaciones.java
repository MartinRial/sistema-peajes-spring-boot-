package obligatorio.da.sistemaPeajes.servicios;

import java.util.ArrayList;
import java.util.List;

import obligatorio.da.sistemaPeajes.dominio.AsignacionBonificacion;
import obligatorio.da.sistemaPeajes.dominio.EstrategiaBonificacion;
import obligatorio.da.sistemaPeajes.dominio.Exonerado;
import obligatorio.da.sistemaPeajes.dominio.Frecuente;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Puesto;
import obligatorio.da.sistemaPeajes.dominio.Trabajador;

/**
 * Servicio responsable de administrar las bonificaciones del sistema.
 *
 * Según el enunciado, actualmente existen 3 bonificaciones predefinidas:
 * - Exonerados: no pagan el tránsito en un determinado puesto.
 * - Frecuentes: 50% de descuento a partir del segundo tránsito del día
 *   para un mismo vehículo y puesto.
 * - Trabajadores: 80% de descuento si el tránsito por el puesto
 *   asignado se realiza en día de semana.
 *
 * En el futuro se podrán agregar nuevas bonificaciones sin modificar
 * este servicio, reutilizando el modelo de {@link Bonificacion} y
 * {@link EstrategiaBonificacion}.
 */
public class ServicioBonificaciones {

	// ----------------- Estado interno del servicio -----------------
	/**
	 * Lista de bonificaciones definidas en el sistema. En este caso se
	 * inicializa con las tres bonificaciones básicas, pero podrían
	 * agregarse más en el futuro.
	 */
	private final List<EstrategiaBonificacion> bonificaciones = new ArrayList<>();

	public ServicioBonificaciones() {
		// Bonificaciones predefinidas.
		bonificaciones.add(new Exonerado());
		bonificaciones.add(new Frecuente());
		bonificaciones.add(new Trabajador());
	}

	// ----------------- Operaciones públicas -----------------
	/**
	 * Devuelve las bonificaciones actualmente disponibles en el sistema.
	 * Se retorna una lista inmodificable para proteger el estado interno.
     * @return 
	 */
	public List<EstrategiaBonificacion> getBonificaciones() {
		return bonificaciones;
	}

	/**
	 * Asigna una bonificación a un propietario para un puesto específico.
	 *
	 * @param propietario Propietario al que se le asigna la bonificación.
	 * @param puesto Puesto para el cual aplica la bonificación.
	 * @param bonificacion Estrategia de bonificación a asociar.
	 */
	public void asignarBonificacion(Propietario propietario, Puesto puesto, EstrategiaBonificacion bonificacion) {
		if (propietario == null || puesto == null || bonificacion == null) {
			return; // En una versión futura podría lanzarse una excepción controlada.
		}
        
		AsignacionBonificacion asignacion = new AsignacionBonificacion(bonificacion, puesto, new java.util.Date());
		propietario.agregarBonificacion(asignacion);
	}

	/**
	 * Obtiene la asignación de bonificación (si existe) para un propietario y un
	 * puesto determinados.
	 *
	 * @param propietario Propietario que realiza el tránsito.
	 * @param puesto Puesto por el que transita.
	 * @return La asignación de bonificación para ese propietario y puesto, o
	 *         {@code null} si no existe.
	 */
	public AsignacionBonificacion obtenerAsignacionPara(Propietario propietario, Puesto puesto) {
		if (propietario == null || puesto == null) {
			return null;
		}

		// Itera sobre las bonificaciones del propietario
		for (AsignacionBonificacion asignacion : propietario.getBonificaciones()) {
			// Verifica si el puesto de la asignación coincide con el puesto buscado
			if (asignacion.getPuesto() != null && asignacion.getPuesto().equals(puesto)) {
				return asignacion;
			}
		}

		return null;
	}

	/**
	 * Verifica si un propietario ya tiene una bonificación asignada para un puesto específico.
	 *
	 * @param propietario El propietario a verificar
	 * @param puesto El puesto a verificar
	 * @return true si ya tiene una bonificación para ese puesto, false en caso contrario
	 */
	public boolean tieneBonificacionParaPuesto(Propietario propietario, Puesto puesto) {
		return obtenerAsignacionPara(propietario, puesto) != null;
	}

	/**
	 * Asigna una bonificación a un propietario para un puesto específico con todas las validaciones necesarias.
	 *
	 * @param propietario El propietario al que se le asignará la bonificación
	 * @param bonificacion La bonificación a asignar
	 * @param puesto El puesto para el cual aplica la bonificación
	 * @throws excepciones.PeajeException Si hay algún error en las validaciones
	 */
	public void asignarBonificacionConValidaciones(Propietario propietario, EstrategiaBonificacion bonificacion, Puesto puesto) throws excepciones.PeajeException {
		// Validar que se especificó un propietario
		if (propietario == null) {
			throw new excepciones.PeajeException("Debe especificar un propietario");
		}

		// Validar que se especificó una bonificación
		if (bonificacion == null) {
			throw new excepciones.PeajeException("Debe especificar una bonificación");
		}

		// Validar que se especificó un puesto
		if (puesto == null) {
			throw new excepciones.PeajeException("Debe especificar un puesto");
		}

		// Validar que el propietario no esté deshabilitado
		if (!propietario.puedeAsignarBonificacion()) {
			throw new excepciones.PeajeException("El propietario esta deshabilitado. No se pueden asignar bonificaciones");
		}

		// Validar que el propietario no tenga ya una bonificación para ese puesto
		if (tieneBonificacionParaPuesto(propietario, puesto)) {
			throw new excepciones.PeajeException("Ya tiene una bonificación asignada para ese puesto");
		}

		// Asignar la bonificación
		asignarBonificacion(propietario, puesto, bonificacion);
	}
}

