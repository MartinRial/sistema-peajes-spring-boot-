package obligatorio.da.sistemaPeajes.controladores;

import java.util.List;

import excepciones.PeajeException;
import jakarta.servlet.http.HttpSession;
import obligatorio.da.sistemaPeajes.dominio.Usuario;
import obligatorio.da.sistemaPeajes.utils.Respuesta;

/**
 * Controlador abstracto que implementa el patrón Template Method para el login.
 * Define la estructura general del proceso de autenticación, delegando
 * partes específicas a las subclases.
 */
public abstract class ControladorLogin {

    /**
     * Método Template que define el flujo general del login.
     * Este método no puede ser sobrescrito por las subclases.
     * 
     * @param sesion La sesión HTTP actual
     * @param cedula Cédula del usuario
     * @param contrasena Contraseña del usuario
     * @return Lista de respuestas con el resultado del login
     * @throws PeajeException Si ocurre algún error durante el login
     */
    public final List<Respuesta> login(HttpSession sesion, String cedula, String contrasena) throws PeajeException {
        // 1. Obtener el usuario usando la fachada
        Usuario usuario = getUsuario(cedula, contrasena);
        
        // 2. Eliminar sesión anterior si existe (método hook)
        eliminarSesion(sesion);
        
        // 3. Guardar el estado del usuario en la sesión (método hook)
        guardarEstadoUsuarioSesion(usuario, sesion);
        
        // 4. Obtener el destino después del login exitoso
        String destino = getDestinoLoginExitoso();
        
        // 5. Retornar la respuesta con el destino
        return Respuesta.lista(new Respuesta("loginExitoso", destino));
    }

    /**
     * Obtiene el usuario del sistema mediante la fachada.
     * Método común para todas las implementaciones.
     * 
     * @param cedula Cédula del usuario
     * @param contrasena Contraseña del usuario
     * @return El usuario autenticado
     * @throws PeajeException Si las credenciales son inválidas
     */
    protected abstract Usuario getUsuario(String cedula, String contrasena) throws PeajeException;

    /**
     * Método abstracto que debe ser implementado por las subclases
     * para guardar el usuario en la sesión con la clave apropiada.
     * 
     * @param usuario El usuario autenticado
     * @param sesion La sesión HTTP
     */
    protected abstract void guardarEstadoUsuarioSesion(Usuario usuario, HttpSession sesion);

    /**
     * Método abstracto que debe ser implementado por las subclases
     * para indicar la URL de destino después de un login exitoso.
     * 
     * @return La URL de destino
     */
    protected abstract String getDestinoLoginExitoso();

    /**
     * Método hook que puede ser sobrescrito por las subclases
     * para eliminar sesiones anteriores si es necesario.
     * Por defecto no hace nada.
     * 
     * @param sesion La sesión HTTP
     * @throws excepciones.PeajeException
     */
    protected abstract void eliminarSesion(HttpSession sesion) throws PeajeException;

    /**
     * Método auxiliar para obtener el destino del logout.
     * 
     * @return La URL de destino para logout
     */
    protected String getDestinoLogoutExitoso() {
        return "login.html";
    }

    /**
     * Método para cerrar sesión (logout).
     * 
     * @param sesion La sesión HTTP
     * @return Lista de respuestas con el resultado del logout
     * @throws excepciones.PeajeException
     */
    public List<Respuesta> logout(HttpSession sesion) throws PeajeException {
        eliminarSesion(sesion);
        sesion.invalidate();
        return Respuesta.lista(new Respuesta("logoutExitoso", getDestinoLogoutExitoso()));
    }
}