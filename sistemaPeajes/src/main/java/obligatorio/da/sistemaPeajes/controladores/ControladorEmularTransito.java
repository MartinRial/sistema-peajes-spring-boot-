package obligatorio.da.sistemaPeajes.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Puesto;
import obligatorio.da.sistemaPeajes.dominio.Tarifa;
import obligatorio.da.sistemaPeajes.dominio.Transito;
import obligatorio.da.sistemaPeajes.dominio.Usuario;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;
import obligatorio.da.sistemaPeajes.dtos.PuestoDTO;
import obligatorio.da.sistemaPeajes.dtos.TarifaDTO;
import obligatorio.da.sistemaPeajes.dtos.TransitoDTO;
import obligatorio.da.sistemaPeajes.servicios.Fachada;
import obligatorio.da.sistemaPeajes.utils.Respuesta;

@RestController
@RequestMapping("/usuarios/admins/emular-transito")
@Scope("session") // Define el alcance de la sesión para este controlador
public class ControladorEmularTransito {
    private List<Puesto> puestos;

    private static final String USUARIO_ADMINISTRADOR_STATE_KEY = "usuarioAdministrador";

    @GetMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(
            @SessionAttribute(name = USUARIO_ADMINISTRADOR_STATE_KEY, required = false) Usuario usuario) {
        if (usuario == null) {
            // Manejar el caso en que el usuario no está en la sesión pide redireccionar a
            // la página de login
            return Respuesta.lista(new Respuesta("usuarioNoAutenticado", "login.html"));
        }
        return Respuesta.lista(puestos());
    }

    @PostMapping("/tarifas")
    public List<Respuesta> listarTarifas(@RequestParam("posPuesto") int posPuesto) {
        if (posPuesto < 0)
            return Respuesta.lista(new Respuesta("error", "Seleccione el puesto"));

        Puesto puesto = puestos.get(posPuesto);
        List<Tarifa> tarifas = Fachada.getInstancia().listarTarifas(puesto);
        List<TarifaDTO> tarifasDto = new ArrayList<>();
        for (Tarifa tarifa : tarifas) {
            tarifasDto.add(new TarifaDTO(tarifa));
        }
        return Respuesta.lista(new Respuesta("tarifas", tarifasDto));
    }

    private Respuesta puestos() {
        puestos = new ArrayList<>(Fachada.getInstancia().getPuestos());
        List<PuestoDTO> puestosDto = new ArrayList<>();

        for (Puesto puesto : puestos) {
            puestosDto.add(new PuestoDTO(puesto));
        }
        return new Respuesta("puestos", puestosDto);
    }

    @PostMapping("/registrar")
    public List<Respuesta> registrarTransito(
            @RequestParam("posPuesto") int posPuesto,
            @RequestParam("matricula") String matricula,
            @RequestParam("fecha") String fechaStr,
            @SessionAttribute(name = USUARIO_ADMINISTRADOR_STATE_KEY, required = false) Usuario usuario) {

        try {
            // Validar autenticación
            if (usuario == null) {
                return Respuesta.lista(new Respuesta("error", "Usuario no autenticado"));
            }

            // Validar puesto
            if (posPuesto < 0 || posPuesto >= puestos.size()) {
                return Respuesta.lista(new Respuesta("error", "Seleccione un puesto válido"));
            }

            // Validar matrícula
            if (matricula == null || matricula.trim().isEmpty()) {
                return Respuesta.lista(new Respuesta("error", "Ingrese una matrícula válida"));
            }

            // Buscar el vehículo por matrícula usando la lógica de negocio
            // Curso alternativo: No existe un vehículo registrado con la matrícula
            // ingresada
            Vehiculo vehiculo = Fachada.getInstancia().buscarVehiculoPorMatricula(matricula);
            Propietario propietarioEncontrado = vehiculo.getPropietario();

            // Parsear la fecha
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date fechaHora;
            try {
                fechaHora = sdf.parse(fechaStr);
            } catch (ParseException e) {
                return Respuesta.lista(new Respuesta("error", "Formato de fecha inválido. Use: yyyy-MM-dd'T'HH:mm"));
            }

            // Obtener el puesto seleccionado
            Puesto puestoSeleccionado = puestos.get(posPuesto);

            // Registrar el tránsito
            Transito transito = Fachada.getInstancia().registrarTransito(
                    puestoSeleccionado,
                    vehiculo,
                    propietarioEncontrado,
                    fechaHora);

            // Preparar respuesta exitosa
            String mensaje = "Tránsito registrado exitosamente";

            TransitoDTO transitoDTO = new TransitoDTO(transito);

            return Respuesta.lista(
                    new Respuesta("exito", mensaje),
                    new Respuesta("transito", transitoDTO));

        } catch (PeajeException e) {
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        } catch (Exception e) {
            return Respuesta.lista(new Respuesta("error", "Error inesperado: " + e.getMessage()));
        }
    }
}
