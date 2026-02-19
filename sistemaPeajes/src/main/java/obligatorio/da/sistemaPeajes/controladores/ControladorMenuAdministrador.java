package obligatorio.da.sistemaPeajes.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpSession;
import obligatorio.da.sistemaPeajes.dominio.Administrador;
import obligatorio.da.sistemaPeajes.utils.ConexionNavegador;
import obligatorio.da.sistemaPeajes.utils.Respuesta;

@RestController
@RequestMapping("/usuarios/admins/menu")
@Scope("session") // Define el alcance de la sesión para este controlador
public class ControladorMenuAdministrador {

    private static final String USUARIO_ADMINISTRADOR_STATE_KEY = "usuarioAdministrador";

    private final ConexionNavegador conexionNavegador; 

    public ControladorMenuAdministrador(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        // Establecer la conexión SSE con el navegador en este caso el menú admin
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE(); 
       
    }

    @GetMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(HttpSession sesionHttp){
        Administrador usuario = (Administrador) sesionHttp.getAttribute(USUARIO_ADMINISTRADOR_STATE_KEY);
        if (usuario == null) {
             // Manejar el caso en que el usuario no está en la sesión pide redireccionar a la página de login
             return Respuesta.lista(new Respuesta("usuarioNoAutenticado", "login.html"));
         }
         return Respuesta.lista(new Respuesta("nombreCompleto", usuario.getNombreCompleto()));
        
    }
    
}
