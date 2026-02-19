/**
 * Cambiar Estado Propietario - JavaScript
 * Handles owner search and state change
 */

// URL de inicialización
urlIniciarVista = "/usuarios/admins/estado-propietario/vistaConectada";

// DOM Elements
const estadoSelect = document.getElementById('estadoSelect');
const propietarioSection = document.getElementById('propietarioSection');
const propietarioNombre = document.getElementById('propietarioNombre');
const propietarioEstado = document.getElementById('propietarioEstado');

/**
 * Muestra los estados disponibles en el select
 */
function mostrar_estados(estados) {
  estadoSelect.innerHTML = crearListaDesdeJson({
    data: estados,
    campoMostrar: 'nombre',
    multiple: false,
    mostrarOpcionInicial: true,
    id: 'posEstado'
  });
}

/**
 * Busca un propietario por cédula
 */
function buscarPropietario(e) {
  e.preventDefault();
  propietarioSection.style.display = 'none';
  submit("/usuarios/admins/estado-propietario/buscarPropietario", serializarFormulario("formCambiarEstado"));
}

/**
 * Cambia el estado del propietario
 */
function cambiarEstado(e) {
  e.preventDefault();
  submit("/usuarios/admins/estado-propietario/cambiarEstado", serializarFormulario("formCambiarEstado"));
}

/**
 * Muestra la información del propietario
 */
function mostrar_propietario(propietario) {
  // Mostrar la sección del propietario
  propietarioSection.style.display = 'block';
  
  // Mostrar datos del propietario
  propietarioNombre.textContent = propietario.nombreCompleto;
  propietarioEstado.textContent = propietario.estado;
  
  // Actualizar el badge del estado según el estado actual
  propietarioEstado.className = 'badge fs-6';
  switch(propietario.estado.toLowerCase()) {
    case 'habilitado':
      propietarioEstado.classList.add('bg-success');
      break;
    case 'deshabilitado':
      propietarioEstado.classList.add('bg-danger');
      break;
    case 'suspendido':
      propietarioEstado.classList.add('bg-warning', 'text-dark');
      break;
    case 'penalizado':
      propietarioEstado.classList.add('bg-dark');
      break;
    default:
      propietarioEstado.classList.add('bg-secondary');
  }
}

/**
 * Muestra un mensaje de éxito
 */
async function mostrar_exito(mensaje) {
  await mostrarMensaje(mensaje);
  // Después de cambiar el estado exitosamente, buscar nuevamente al propietario para refrescar datos
  submit("/usuarios/admins/estado-propietario/buscarPropietario", serializarFormulario("formCambiarEstado"));
}

/**
 * Muestra un mensaje de error
 */
async function mostrar_error(mensaje) {
  await mostrarMensaje(mensaje);
}

// Event Listeners - Se agregan cuando el DOM está listo
document.addEventListener('DOMContentLoaded', function() {
  const buscarBtn = document.getElementById('buscarBtn');
  const cambiarEstadoBtn = document.getElementById('cambiarEstadoBtn');
  
  if (buscarBtn) {
    buscarBtn.addEventListener('click', buscarPropietario);
  }
  
  if (cambiarEstadoBtn) {
    cambiarEstadoBtn.addEventListener('click', cambiarEstado);
  }
  
  // Ocultar sección de propietario al cargar la página
  if (propietarioSection) {
    propietarioSection.style.display = 'none';
  }
});
