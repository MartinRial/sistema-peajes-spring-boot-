package obligatorio.da.sistemaPeajes.servicios;
import java.util.ArrayList;
import java.util.List;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.Administrador;
import obligatorio.da.sistemaPeajes.dominio.Deshabilitado;
import obligatorio.da.sistemaPeajes.dominio.EstadoPropietario;
import obligatorio.da.sistemaPeajes.dominio.Habilitado;
import obligatorio.da.sistemaPeajes.dominio.Penalizado;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Suspendido;
import obligatorio.da.sistemaPeajes.dominio.Usuario;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;


public class ServicioUsuarios {
    private final List<Propietario> propietarios;
    private final List<Administrador> administradores;
    private final List<Administrador> administradoresLogueados;
    private final List<EstadoPropietario> estadosDisponibles;

    //o coleccion hashmap
    
    public ServicioUsuarios() {
        propietarios = new ArrayList<>();
        administradores = new ArrayList<>();
        administradoresLogueados = new ArrayList<>();
        
        // Inicializar estados disponibles en el sistema
        // Se crean instancias prototipo sin propietario asociado (null) solo para obtener los nombres
        estadosDisponibles = new ArrayList<>();
        estadosDisponibles.add(new Habilitado());
        estadosDisponibles.add(new Deshabilitado());
        estadosDisponibles.add(new Suspendido());
        estadosDisponibles.add(new Penalizado());
    }

    public Administrador loginAdministrador(String cedula, String contrasena) throws PeajeException {
        List<Usuario> adminsDisponibles = new ArrayList<>(administradores);
        Usuario usuario = login(cedula, contrasena, adminsDisponibles);
        // El administrador ya se encuentra logueado. Mensaje. Mensaje “Ud. Ya está logueado”.
        Administrador admin = (Administrador) usuario;
        if (administradoresLogueados.contains(admin)) {
            throw new PeajeException("Ud. ya está logueado");
        }
        administradoresLogueados.add(admin);
        return admin;
    }

    public Administrador logoutAdministrador(Administrador administrador) throws PeajeException {
        if (!administradoresLogueados.contains(administrador)) {
            throw new PeajeException("El administrador no está logueado");
        }
        administradoresLogueados.remove(administrador);
        return administrador;
    }

    public Propietario loginPropietario(String cedula, String contrasena) throws PeajeException {
        List<Usuario> propsDisponibles = new ArrayList<>(propietarios);
        Usuario usuario = login(cedula, contrasena, propsDisponibles);
        Propietario prop = (Propietario) usuario;
        // El usuario está deshabilitado. Mensaje “Usuario deshabilitado, no puede ingresar al sistema”
        if(!prop.puedeIngresar()) {
            throw new PeajeException("Usuario deshabilitado, no puede ingresar al sistema");
        }
        return prop;
    }

    //falta validar el estado del usuario, que no este deshabilitado
    private Usuario login(String cedula, String contrasena, List<Usuario> usuarios) throws PeajeException{
        for (Usuario usuario : usuarios) {
            if (usuario.getCedula().equals(cedula) && usuario.esPasswordCorrecto(contrasena)) {
                return usuario;
            }
        }
        throw new PeajeException("Acceso denegado");
    }

    public void agregarPropietario(Propietario propietario) throws PeajeException {
        // Validar que no exista otro propietario con la misma cédula
        for (Propietario prop : propietarios) {
            if (prop.getCedula().equals(propietario.getCedula())) {
                throw new PeajeException("Ya existe un propietario con la misma cédula");
            }
        }
        propietarios.add(propietario);
    }

    public void agregarAdministrador(Administrador administrador) throws PeajeException {
        // Validar que no exista otro administrador con la misma cédula
        for (Administrador admin : administradores) {
            if (admin.getCedula().equals(administrador.getCedula())) {
                throw new PeajeException("Ya existe un administrador con la misma cédula");
            }
        }
        administradores.add(administrador);
    }

    public List<Propietario> getPropietarios() {
        return propietarios;
    }

    /**
     * Clase auxiliar para retornar el propietario y el vehículo encontrados.
     */
    public static class PropietarioVehiculo {
        private final Propietario propietario;
        private final Vehiculo vehiculo;

        public PropietarioVehiculo(Propietario propietario, Vehiculo vehiculo) {
            this.propietario = propietario;
            this.vehiculo = vehiculo;
        }

        public Propietario getPropietario() {
            return propietario;
        }

        public Vehiculo getVehiculo() {
            return vehiculo;
        }
    }

    /**
     * Busca un vehículo por su matrícula entre todos los propietarios.
     * 
     * Curso alternativo:
     * - No existe un vehículo registrado con la matrícula ingresada. Mensaje: "No existe el vehículo".
     * 
     * @param matricula La matrícula del vehículo a buscar
     * @return Un objeto PropietarioVehiculo con el propietario y vehículo encontrados
     * @throws PeajeException Si no se encuentra un vehículo con la matrícula especificada
     */
    
    /**
     * Busca un propietario por su cédula.
     * 
     * Curso alternativo:
     * - No se encuentra un propietario con la cedula especificada. Mensaje "no existe el propietario"
     * 
     * @param cedula La cédula del propietario a buscar
     * @return El propietario encontrado
     * @throws PeajeException Si no se encuentra un propietario con la cédula especificada
     */
    public Propietario buscarPropietarioPorCedula(String cedula) throws PeajeException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new PeajeException("Cédula inválida");
        }

        for (Propietario propietario : propietarios) {
            if (propietario.getCedula().equals(cedula.trim())) {
                return propietario;
            }
        }

        throw new PeajeException("no existe el propietario");
    }

    /**
     * Cambia el estado de un propietario.
     * 
     * Validaciones:
     * - El propietario no puede ser nulo
     * - El nuevo estado no puede ser nulo
     * - El estado seleccionado no puede ser igual al actual
     * 
     * @param propietario El propietario al que se le cambiará el estado
     * @param nuevoEstado El nuevo estado a asignar
     * @throws PeajeException Si las validaciones no se cumplen
     */
    public void cambiarEstadoPropietario(Propietario propietario, EstadoPropietario nuevoEstado) throws PeajeException {
        // Validar parámetros
        if (propietario == null) {
            throw new PeajeException("Debe especificar un propietario");
        }
        
        if (nuevoEstado == null) {
            throw new PeajeException("Debe especificar un estado");
        }        
        // Cambiar el estado del propietario
        propietario.setEstado(nuevoEstado);
    }

    /**
     * Obtiene la lista de estados disponibles en el sistema.
     * 
     * @return Lista de estados de propietario disponibles
     */
    public List<EstadoPropietario> getEstadosDisponibles() {
        return new ArrayList<>(estadosDisponibles);
    }
}

