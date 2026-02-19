package obligatorio.da.sistemaPeajes.servicios;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.AsignacionBonificacion;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Puesto;
import obligatorio.da.sistemaPeajes.dominio.Tarifa;
import obligatorio.da.sistemaPeajes.dominio.Transito;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;;

public class ServicioTransito {

    private final List<Transito> transitos;
    private final List<Puesto> puestos;

    public ServicioTransito() {
        this.transitos = new ArrayList<>();
        this.puestos = new ArrayList<>();
    }

    public void agregar(Transito transito) {
        transitos.add(transito);
    }

    public void agregarPuesto(Puesto puesto) {
        puestos.add(puesto);
    }

    public List<Puesto> getPuestos() {
        return puestos;
    }

    public List<Tarifa> listarTarifas(Puesto puesto) {
        return puesto.getTarifas();
    }

    /**
     * Registra un tránsito de un vehículo por un puesto de peaje.
     * 
     * Cursos alternativos:
     * - No existe un vehículo registrado con la matrícula ingresada. Mensaje: "No
     * existe el vehículo".
     * - El propietario no tiene saldo suficiente para abonar el tránsito. Mensaje:
     * "Saldo insuficiente: " + el saldo actual del propietario.
     * - El propietario está en estado deshabilitado. Mensaje: "El propietario del
     * vehículo está deshabilitado, no puede realizar tránsitos" y no se registra el
     * tránsito.
     * - El propietario está en estado suspendido. Mensaje: "El propietario del
     * vehículo está suspendido, no puede realizar tránsitos" y no se registra el
     * tránsito.
     * - El propietario está en estado penalizado. El tránsito se registra, pero no
     * se aplican bonificaciones (si hubiera) y no se envía la notificación al
     * propietario.
     * 
     * Principio GRASP: Experto de la Información
     * ServicioTransito coordina el proceso pero delega las validaciones y cálculos
     * a los objetos expertos (Propietario, Puesto) que tienen la información necesaria.
     * 
     * @param puesto      El puesto de peaje
     * @param vehiculo    El vehículo que transita
     * @param propietario El propietario del vehículo
     * @param fechaHora   La fecha y hora del tránsito
     * @return El tránsito registrado
     * @throws PeajeException Si no se puede registrar el tránsito
     */
    public Transito registrarTransito(Puesto puesto, Vehiculo vehiculo, Propietario propietario, Date fechaHora)
            throws PeajeException {
        // GRASP: Propietario valida su propio estado (Experto)
        propietario.validarPuedeTransitar();

        // GRASP: Vehículo obtiene su propia tarifa del puesto (Experto)
        // El Vehículo conoce su categoría y puede obtener la tarifa correspondiente
        Tarifa tarifaCorrespondiente = vehiculo.obtenerTarifaEn(puesto);

        if (tarifaCorrespondiente == null) {
            throw new PeajeException("No se encontró una tarifa para la categoría del vehículo");
        }

        double tarifaBase = tarifaCorrespondiente.getMonto();

        // GRASP: Propietario procesa el pago completo (calcula bonificaciones, valida saldo y descuenta)
        // El Propietario es el experto porque conoce sus bonificaciones, su estado y su saldo
        double montoPagado = propietario.procesarPagoTransito(puesto, tarifaBase);

        // GRASP: Propietario conoce la bonificación aplicable para el puesto (Experto)
        // Esto permite registrar qué bonificación se aplicó en este tránsito específico
        AsignacionBonificacion bonificacionAplicada = propietario.obtenerBonificacionAplicable(puesto);

        // ServicioTransito coordina la creación del tránsito (Creador)
        Transito transito = new Transito(puesto, vehiculo, propietario, bonificacionAplicada, montoPagado, fechaHora);
        transitos.add(transito);

        // Registrar notificaciones a través de la Fachada
        // La Fachada coordina con ServicioNotificaciones para registrar las notificaciones
        registrarNotificacionesTransito(puesto, vehiculo, propietario, fechaHora);

        // Emitir evento fachada para emular tránsito
        Fachada.getInstancia().avisar(Fachada.Eventos.TRANSITO_REGISTRADO);

        return transito;
    }

    /**
     * Registra las notificaciones correspondientes al tránsito a través de la Fachada.
     * 
     * Principio GRASP: Controlador/Fachada
     * ServicioTransito coordina el proceso pero usa la Fachada para registrar notificaciones,
     * manteniendo la arquitectura en capas y el bajo acoplamiento.
     * 
     * @param puesto El puesto de peaje
     * @param vehiculo El vehículo que transitó
     * @param propietario El propietario del vehículo
     * @param fechaHora La fecha y hora del tránsito
     */
    private void registrarNotificacionesTransito(Puesto puesto, Vehiculo vehiculo, 
                                                  Propietario propietario, Date fechaHora) {
        Fachada fachada = Fachada.getInstancia();
        
        // Notificación del tránsito realizado
        fachada.notificarTransito(propietario, puesto, vehiculo, fechaHora);

        // Notificación de saldo bajo si corresponde
        // GRASP: Propietario verifica su propio saldo (Experto)
        if (propietario.saldoBajoMinimo()) {
            fachada.notificarSaldoBajo(propietario, fechaHora);
        }
    }

    public List<Transito> obtenerTransitosPropietario(Propietario propietario) {
        List<Transito> transitosPropietario = new ArrayList<>();
        for (Transito transito : transitos) {
            if (transito.getPropietario().equals(propietario)) {
                transitosPropietario.add(transito);
            }
        }
        transitosPropietario.sort(Comparator.comparing(Transito::getFechaHora).reversed());
        return transitosPropietario;
    }

    public int contarTransitosDeVehiculo(Propietario propietario, Vehiculo vehiculo) {
        int contador = 0;
        for (Transito transito : transitos) {
            if (transito.getPropietario().equals(propietario) && transito.getVehiculo().equals(vehiculo)) {
                contador++;
            }
        }
        return contador;
    }

    public double totalGastadoPorVehiculo(Propietario propietario, Vehiculo vehiculo) {
        double total = 0.0;
        for (Transito transito : transitos) {
            if (transito.getPropietario().equals(propietario) && transito.getVehiculo().equals(vehiculo)) {
                total += transito.getMontoPagado();
            }
        }
        return total;
    }
}