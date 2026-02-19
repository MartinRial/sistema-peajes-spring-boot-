package obligatorio.da.sistemaPeajes.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import excepciones.PeajeException;
import jakarta.servlet.http.HttpSession;
import obligatorio.da.sistemaPeajes.dominio.Administrador;
import obligatorio.da.sistemaPeajes.dominio.Usuario;
import obligatorio.da.sistemaPeajes.servicios.Fachada;
import obligatorio.da.sistemaPeajes.utils.Respuesta;

/**
 * Controlador REST para el login de administradores.
 * Implementa el patrón Template Method heredando de ControladorLogin.
 */
@RestController
@RequestMapping("/usuarios/admins/acceso")
public class ControladorLoginAdministrador extends ControladorLogin {

    private static final String USUARIO_ADMINISTRADOR_STATE_KEY = "usuarioAdministrador";

    /**
     * Endpoint para el login de administradores.
     * Utiliza el método template de la clase padre.
     * 
     * @param sesion La sesión HTTP actual
     * @param cedula Cédula del administrador
     * @param contrasena Contraseña del administrador
     * @return Lista de respuestas con el resultado del login
     */
    @PostMapping("/login")
    public List<Respuesta> loginAdministrador(
            HttpSession sesion, 
            @RequestParam("cedula") String cedula, 
            @RequestParam("contrasena") String contrasena) {
        try {
            return login(sesion, cedula, contrasena);
        } catch (PeajeException e) {
            // Manejo de la excepción (puede ser logging, rethrow, etc.)
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para el logout de administradores.
     * 
     * @param sesion La sesión HTTP actual
     * @return Lista de respuestas con el resultado del logout
     * @throws excepciones.PeajeException
     */
    @PostMapping("/logout")
    public List<Respuesta> logoutAdministrador(HttpSession sesion) throws PeajeException {
        return logout(sesion);
    }

    /**
     * Implementación específica para guardar el usuario administrador en la sesión.
     * Utiliza la clave "usuarioAdministrador".
     */
    @Override
    protected void guardarEstadoUsuarioSesion(Usuario usuario, HttpSession sesion) {
        
        sesion.setAttribute(USUARIO_ADMINISTRADOR_STATE_KEY, usuario);
    }

    /**
     * Implementación específica para obtener el destino después del login exitoso.
     * Los administradores son redirigidos al menú de administración.
     */
    @Override
    protected String getDestinoLoginExitoso() {
        return "menu.html";
    }

    /**
     * Implementación específica para eliminar la sesión del administrador.
     * Elimina cualquier sesión de propietario que pudiera existir.
     * @throws excepciones.PeajeException
     */
    @Override
    protected void eliminarSesion(HttpSession sesion) throws PeajeException {
        Administrador admin = (Administrador) sesion.getAttribute(USUARIO_ADMINISTRADOR_STATE_KEY);
        if (admin != null) {
            Fachada.getInstancia().logoutAdministrador(admin);
        }
        sesion.removeAttribute(USUARIO_ADMINISTRADOR_STATE_KEY);
    }

    /**
     * Obtiene el destino para el logout de administradores.
     */
    @Override
    protected String getDestinoLogoutExitoso() {
        return "login.html";
    }

    @Override
    protected Usuario getUsuario(String cedula, String contrasena) throws PeajeException {
        return Fachada.getInstancia().loginAdministrador(cedula, contrasena);
    }
}
