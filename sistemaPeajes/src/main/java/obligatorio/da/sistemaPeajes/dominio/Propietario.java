package obligatorio.da.sistemaPeajes.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import excepciones.PeajeException;
import lombok.Getter;
import lombok.Setter;
import observador.ObservableImpl;
import observador.Observador;

@Getter
@Setter
public class Propietario extends Usuario implements ObservableImpl {

    // Definición de eventos para el patrón Observer
    public static class Eventos {
        public static final String ESTADO_CAMBIADO = "estadoCambiado";
        public static final String BONIFICACION_ASIGNADA = "bonificacionAsignada";
        public static final String SALDO_MODIFICADO = "saldoModificado";
        public static final String NOTIFICACION_AGREGADA = "notificacionAgregada";
        public static final String NOTIFICACIONES_BORRADAS = "notificacionesBorradas";
    }

    // Atributos propios de Propietario
    private double saldoActual;
    private double saldoMinimoAlerta;

    // Listas de relaciones
    private List<Vehiculo> vehiculos;
    private List<AsignacionBonificacion> bonificaciones;
    private List<Notificacion> notificaciones;

    // Estado del propietario (patrón State)
    private EstadoPropietario estado;

    // Lista de observadores (patrón Observer)
    private final List<Observador> observadores;

    public Propietario(String cedula, String contrasena, String nombreCompleto,
            double saldoActual, double saldoMinimoAlerta) {
        super(cedula, contrasena, nombreCompleto);
        this.saldoActual = saldoActual;
        this.saldoMinimoAlerta = saldoMinimoAlerta;

        // Inicializar listas vacías
        this.vehiculos = new ArrayList<>();
        this.bonificaciones = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.observadores = new ArrayList<>();

        // Estado inicial: Habilitado (según la letra del obligatorio)
        this.estado = new Habilitado();
    }

    /**
     * Delega al estado actual para determinar si puede ingresar.
     * Patrón Strategy/State en acción.
     * 
     * @return
     */
    public boolean puedeIngresar() {
        return estado.puedeIngresar();
    }

    public boolean aplicanBonificaciones() {
        return estado.aplicanBonificaciones();
    }


    public void agregarBonificacion(AsignacionBonificacion asignacion) {
        this.bonificaciones.add(asignacion);
        avisar(Eventos.BONIFICACION_ASIGNADA);
    }

    /**
     * Delega al estado actual para determinar si puede transitar.
     * 
     * @return
     */
    public boolean puedeTransitar() {
        return estado.puedeTransitar();
    }

    /**
     * Delega al estado actual para determinar si puede asignar bonificaciones.
     * 
     * @return
     */
    public boolean puedeAsignarBonificacion() {
        return estado.puedeAsignarBonificacion();
    }

    /**
     * Delega al estado actual para determinar si puede recibir notificaciones.
     * 
     * @return
     */
    public boolean puedeRecibirNotificaciones() {
        return estado.puedeRecibirNotificaciones();
    }

    /**
     * Habilita al propietario delegando al estado actual.
     * 
     * @throws PeajeException
     */
    public void habilitar() throws PeajeException {
        estado.habilitar(this);
    }

    /**
     * Deshabilita al propietario delegando al estado actual.
     * 
     * @throws PeajeException
     */
    public void deshabilitar() throws PeajeException {
        estado.deshabilitar(this);
    }

    /**
     * Suspende al propietario delegando al estado actual.
     * 
     * @throws PeajeException
     */
    public void suspender() throws PeajeException {
        estado.suspender(this);
    }

    /**
     * Penaliza al propietario delegando al estado actual.
     * 
     * @throws PeajeException
     */
    public void penalizar() throws PeajeException {
        estado.penalizar(this);
    }

    private void validarEstado(EstadoPropietario nuevoEstado) throws PeajeException {
        if (nuevoEstado == null) {
            throw new PeajeException("El nuevo estado no puede ser nulo");
        }
        boolean esValido = !this.estado.getNombre().equals(nuevoEstado.getNombre());
        if (!esValido) {
            throw new PeajeException("El propietario ya esta en estado " + nuevoEstado.getNombre());
        }
    }

    /**
     * Establece el estado del propietario.
     * Este método es utilizado por las clases de estado para cambiar de estado.
     * 
     * @param nuevoEstado El nuevo estado del propietario
     * @throws PeajeException
     */
    public void setEstado(EstadoPropietario nuevoEstado) throws PeajeException {
        this.validarEstado(nuevoEstado);
        this.estado = nuevoEstado;
        Date fechaHora = new java.util.Date();
        this.notificarCambioEstado(fechaHora);
        avisar(Eventos.ESTADO_CAMBIADO);
    }

    /**
     * Agrega saldo al propietario.
     * 
     * @param monto El monto a agregar (debe ser positivo)
     * @throws IllegalArgumentException si el monto es negativo o cero
     */
    public void agregarSaldo(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        this.saldoActual += monto;
        avisar(Eventos.SALDO_MODIFICADO);
    }

    /**
     * Quita saldo al propietario (por ejemplo, al realizar un tránsito).
     * 
     * @param monto El monto a descontar (debe ser positivo)
     * @throws IllegalArgumentException si el monto es negativo o cero
     * @throws IllegalStateException    si no hay saldo suficiente
     */
    public void descontarSaldo(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (this.saldoActual < monto) {
            throw new IllegalStateException("Saldo insuficiente: " + this.saldoActual);
        }
        this.saldoActual -= monto;
        avisar(Eventos.SALDO_MODIFICADO);
    }

    /**
     * Verifica si el propietario tiene saldo suficiente.
     * 
     * @param monto El monto a verificar
     * @return true si tiene saldo suficiente, false en caso contrario
     */
    public boolean tieneSaldoSuficiente(double monto) {
        return this.saldoActual >= monto;
    }

    /**
     * Valida si el propietario puede realizar un tránsito según su estado actual.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce su estado y puede determinar
     * si puede transitar. Encapsula la validación y el mensaje de error apropiado.
     * 
     * @throws PeajeException Si el propietario no puede transitar (deshabilitado o suspendido)
     */
    public void validarPuedeTransitar() throws PeajeException {
        if (!this.puedeTransitar()) {
            throw new PeajeException(
                "El propietario del vehículo está " + this.getNombreEstado() + 
                ", no puede realizar tránsitos");
        }
    }

    /**
     * Valida que el propietario tenga saldo suficiente para realizar un pago.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce su saldo actual y puede validarlo.
     * Encapsula la validación y el mensaje de error con el saldo actual.
     * 
     * @param monto El monto requerido para el pago
     * @throws PeajeException Si no tiene saldo suficiente
     */
    public void validarSaldoSuficiente(double monto) throws PeajeException {
        if (!this.tieneSaldoSuficiente(monto)) {
            throw new PeajeException("Saldo insuficiente: " + this.saldoActual);
        }
    }

    /**
     * Obtiene el nombre del estado actual del propietario.
     * 
     * @return El nombre del estado
     */
    public String getNombreEstado() {
        return this.estado.getNombre();
    }

    // ==================== Implementación de ObservableImpl ====================

    /**
     * Agrega un observador a la lista de observadores.
     * 
     * @param obs El observador a agregar
     */
    @Override
    public void agregarObservador(Observador obs) {
        if (!observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    /**
     * Quita un observador de la lista de observadores.
     * 
     * @param obs El observador a quitar
     */
    @Override
    public void quitarObservador(Observador obs) {
        observadores.remove(obs);
    }

    /**
     * Notifica a todos los observadores sobre un evento.
     * 
     * @param evento El evento a notificar
     */
    @Override
    public void avisar(Object evento) {
        List<Observador> copia = new ArrayList<>(observadores);
        for (Observador obs : copia) {
            obs.actualizar(evento, null);
        }
    }

    public void agregarVehiculo(Vehiculo vehiculo) {
        vehiculo.setPropietario(this); // Vehiculo conoce a Propietario
        this.vehiculos.add(vehiculo); // Propietario conoce a Vehículo
    }

    /**
     * Obtiene la bonificación aplicable para un puesto específico.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce sus bonificaciones asignadas
     * y su estado actual para determinar si aplican o no.
     * 
     * @param puesto El puesto de peaje
     * @return La AsignacionBonificacion aplicable para el puesto, o null si no aplican bonificaciones
     *         o no tiene bonificación asignada para ese puesto
     */
    public AsignacionBonificacion obtenerBonificacionAplicable(Puesto puesto) {
        // Si el propietario está en un estado donde no aplican bonificaciones
        if (!this.aplicanBonificaciones()) {
            return null;
        }

        // Buscar si tiene una bonificación asignada para este puesto
        for (AsignacionBonificacion asignacion : this.bonificaciones) {
            if (asignacion.getPuesto().equals(puesto)) {
                return asignacion;
            }
        }

        // No tiene bonificación para este puesto
        return null;
    }

    /**
     * Calcula el monto a pagar aplicando las bonificaciones asignadas al propietario
     * para el puesto específico.
     * 
     * Si el propietario está en un estado donde no aplican bonificaciones
     * (por ejemplo, penalizado o deshabilitado), se retorna la tarifa base completa.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce:
     * - Sus bonificaciones asignadas
     * - Su estado actual
     * - Si las bonificaciones aplican según su estado
     * 
     * @param puesto El puesto de peaje
     * @param tarifaBase La tarifa base antes de aplicar bonificaciones
     * @return El monto final a pagar después de aplicar bonificaciones
     */
    public double calcularMontoConBonificaciones(Puesto puesto, double tarifaBase) {
        AsignacionBonificacion asignacion = this.obtenerBonificacionAplicable(puesto);
        
        if (asignacion != null) {
            // Aplicar la bonificación y retornar el monto bonificado
            return asignacion.calcularBonificacion(tarifaBase);
        }

        // Si no tiene bonificaciones para este puesto, paga la tarifa completa
        return tarifaBase;
    }

    /**
     * Procesa el pago de un tránsito: calcula el monto con bonificaciones,
     * valida que haya saldo suficiente y descuenta el monto del saldo.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce y gestiona:
     * - Sus bonificaciones y cómo aplicarlas
     * - Su saldo actual
     * - Su estado y si aplican bonificaciones
     * 
     * Este método encapsula toda la lógica de pago en un solo lugar,
     * evitando que otras clases necesiten conocer estos detalles.
     * 
     * @param puesto El puesto de peaje
     * @param tarifaBase La tarifa base antes de aplicar bonificaciones
     * @return El monto efectivamente pagado (después de aplicar bonificaciones)
     * @throws PeajeException Si no tiene saldo suficiente
     */
    public double procesarPagoTransito(Puesto puesto, double tarifaBase) throws PeajeException {
        // Calcular el monto final aplicando bonificaciones si corresponde
        double montoPagado = this.calcularMontoConBonificaciones(puesto, tarifaBase);
        
        // Solo validar y descontar si el monto es mayor a 0
        // (En caso de exonerados, el monto es 0 y no hay que descontar nada)
        if (montoPagado > 0) {
            // Validar que tenga saldo suficiente
            this.validarSaldoSuficiente(montoPagado);
            
            // Descontar el monto del saldo
            this.descontarSaldo(montoPagado);
        }
        
        return montoPagado;
    }

    /**
     * Registra una notificación para el propietario.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce y gestiona su lista de notificaciones.
     * Solo registra la notificación si el propietario puede recibirlas según su estado.
     * 
     * @param notificacion La notificación a registrar
     */
    public void registrarNotificacion(Notificacion notificacion) {
        if (this.puedeRecibirNotificaciones()) {
            this.notificaciones.add(notificacion);
            avisar(Eventos.NOTIFICACION_AGREGADA);
        }
    }

    /**
     * Registra una notificación de tránsito para el propietario.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce su estado y si puede recibir notificaciones.
     * Encapsula la lógica de creación del mensaje específico de tránsito.
     * 
     * @param puesto El puesto por el que transitó
     * @param vehiculo El vehículo con el que transitó
     * @param fechaHora La fecha y hora del tránsito
     */
    public void notificarTransito(Puesto puesto, Vehiculo vehiculo, Date fechaHora) {
        if (!this.puedeRecibirNotificaciones()) {
            return;
        }
        
        String mensaje = "Pasaste por el puesto " + puesto.getNombre() + 
                        " con el vehículo " + vehiculo.getMatricula();
        Notificacion notificacion = new Notificacion(fechaHora, mensaje);
        this.notificaciones.add(notificacion);
        avisar(Eventos.NOTIFICACION_AGREGADA);
    }

    /**
     * Registra una notificación de saldo bajo para el propietario.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque:
     * - Conoce su saldo actual
     * - Conoce su estado y si puede recibir notificaciones
     * - Puede formatear el mensaje con su propia información
     * 
     * @param fechaHora La fecha y hora de la notificación
     */
    public void notificarSaldoBajo(Date fechaHora) {
        if (!this.puedeRecibirNotificaciones()) {
            return;
        }
        
        String mensaje = "Tu saldo actual es de $ " + 
                        String.format("%.2f", this.saldoActual) + 
                        " Te recomendamos hacer una recarga";
        Notificacion notificacion = new Notificacion(fechaHora, mensaje);
        this.notificaciones.add(notificacion);
        avisar(Eventos.NOTIFICACION_AGREGADA);
    }

    /**
     * Registra una notificación de cambio de estado para el propietario.
     * Esta notificación SIEMPRE se registra, sin importar el estado actual o anterior.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque:
     * - Conoce su estado actual
     * - Gestiona su lista de notificaciones
     * - Puede formatear el mensaje con su propia información
     * 
     * @param fechaHora La fecha y hora de la notificación
     */
    public void notificarCambioEstado(Date fechaHora) {
        // Esta notificación SIEMPRE se registra, sin verificar puedeRecibirNotificaciones()
        String mensaje = "Se ha cambiado tu estado en el sistema. Tu estado actual es " + 
                        this.estado.getNombre();
        Notificacion notificacion = new Notificacion(fechaHora, mensaje);
        this.notificaciones.add(notificacion);
        avisar(Eventos.NOTIFICACION_AGREGADA);
    }

    /**
     * Borra todas las notificaciones del propietario.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce y gestiona su lista de notificaciones.
     * 
     * @return true si se borraron notificaciones, false si no había notificaciones
     */
    public boolean borrarNotificaciones() {
        if (this.notificaciones.isEmpty()) {
            return false;
        }
        this.notificaciones.clear();
        avisar(Eventos.NOTIFICACIONES_BORRADAS);
        return true;
    }

    /**
     * Verifica si el saldo actual está por debajo del saldo mínimo de alerta.
     * 
     * Principio GRASP: Experto de la Información
     * El Propietario es el experto porque conoce su saldo actual y su umbral de alerta.
     * 
     * @return true si el saldo está por debajo del mínimo, false en caso contrario
     */
    public boolean saldoBajoMinimo() {
        return this.saldoActual < this.saldoMinimoAlerta;
    }

}