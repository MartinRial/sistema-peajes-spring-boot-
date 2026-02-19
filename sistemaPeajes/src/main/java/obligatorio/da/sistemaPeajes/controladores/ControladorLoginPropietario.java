package obligatorio.da.sistemaPeajes.controladores;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import excepciones.PeajeException;
import jakarta.servlet.http.HttpSession;
import obligatorio.da.sistemaPeajes.dominio.Usuario;
import obligatorio.da.sistemaPeajes.servicios.Fachada;
import obligatorio.da.sistemaPeajes.utils.Respuesta;

/**
 * Controlador REST para el login de propietarios.
 * Implementa el patrón Template Method heredando de ControladorLogin.
 */
@RestController
@RequestMapping("/usuarios/propietarios/acceso")
public class ControladorLoginPropietario extends ControladorLogin {

    private static final String USUARIO_PROPIETARIO_STATE_KEY = "usuarioPropietario";

    /**
     * Endpoint para el login de propietarios.
     * Utiliza el método template de la clase padre.
     * 
     * @param sesion La sesión HTTP actual
     * @param cedula Cédula del propietario
     * @param contrasena Contraseña del propietario
     * @return Lista de respuestas con el resultado del login
     */
    @PostMapping("/login")
    public List<Respuesta> loginPropietario(
            HttpSession sesion, 
            @RequestParam("cedula") String cedula, 
            @RequestParam("contrasena") String contrasena) 
        {
            try {
                return login(sesion, cedula, contrasena);
            } catch (PeajeException e) {
                // Manejo de la excepción (puede ser logging, rethrow, etc.)
                return Respuesta.lista(new Respuesta("error", e.getMessage()));
            }
    }

    /**
     * Endpoint para el logout de propietarios.
     * 
     * @param sesion La sesión HTTP actual
     * @return Lista de respuestas con el resultado del logout
     * @throws excepciones.PeajeException
     */
    @PostMapping("/logout")
    public List<Respuesta> logoutPropietario(HttpSession sesion) throws PeajeException {
        return logout(sesion);
    }

    /**
     * Implementación específica para guardar el usuario propietario en la sesión.
     * Utiliza la clave "usuarioPropietario".
     */
    @Override
    protected void guardarEstadoUsuarioSesion(Usuario usuario, HttpSession sesion) {
        sesion.setAttribute(USUARIO_PROPIETARIO_STATE_KEY, usuario);
    }

    /**
     * Implementación específica para obtener el destino después del login exitoso.
     * Los propietarios son redirigidos a su panel de control.
     */
    @Override
    protected String getDestinoLoginExitoso() {
        return "panel.html";
    }

    /**
     * Implementación específica para eliminar la sesión del propietario.
     * Elimina cualquier sesión de administrador que pudiera existir.
     */
    @Override
    protected void eliminarSesion(HttpSession sesion) {
        sesion.removeAttribute(USUARIO_PROPIETARIO_STATE_KEY);
    }

    /**
     * Obtiene el destino para el logout de propietarios.
     */
    @Override
    protected String getDestinoLogoutExitoso() {
        return "login.html";
    }

    @Override
    protected Usuario getUsuario(String cedula, String contrasena) throws PeajeException {
        return Fachada.getInstancia().loginPropietario(cedula, contrasena);
    }
}
