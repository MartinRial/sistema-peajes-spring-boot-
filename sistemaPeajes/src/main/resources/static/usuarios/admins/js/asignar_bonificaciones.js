/**
 * Asignar Bonificaciones - JavaScript
 * Handles owner search and bonification assignment
 */
 // DOM Elements
urlIniciarVista = "/usuarios/admins/bonificaciones/vistaConectada";
urlCierreVista="/usuarios/admins/bonificaciones/vistaCerrada";
const divPuestos = document.getElementById('divPuestos');
const divBonificaciones = document.getElementById('divBonificaciones');
const propietarioSection = document.getElementById('propietarioSection');
const propietarioNombre = document.getElementById('propietarioNombre');
const propietarioEstado = document.getElementById('propietarioEstado');

function mostrar_puestos(puestos) {
  divPuestos.innerHTML = crearListaDesdeJson({
    data: puestos,
    campoMostrar: 'nombre',
    multiple : false,
    mostrarOpcionInicial : true, 
    id : 'posPuesto'
  });
}

function cargarDatosBonificacion(e) {
  e.preventDefault();
  propietarioSection.style.display = 'none';
  submit("/usuarios/admins/bonificaciones/buscarPropietario", serializarFormulario("formAsignarBonificacion"));
}

function asignarBonificacion(e) {
  e.preventDefault();
  submit("/usuarios/admins/bonificaciones/asignarBonificacion", serializarFormulario("formAsignarBonificacion"));
}

function mostrar_bonificaciones(bonificaciones) {
  divBonificaciones.innerHTML = crearListaDesdeJson({
    data: bonificaciones,
    campoMostrar: 'nombre',
    multiple : false,
    mostrarOpcionInicial : true, 
    id : 'posBonificacion'
  });
}

function mostrar_propietario(propietario) {
  // Mostrar la sección del propietario
  propietarioSection.style.display = 'block';
  
  // Mostrar datos del propietario
  propietarioNombre.textContent = propietario.nombreCompleto;
  propietarioEstado.textContent = propietario.estado;
  
  // Mostrar bonificaciones asignadas
  const bonificacionesTable = document.getElementById('bonificacionesTable');
  if (propietario.bonificaciones && propietario.bonificaciones.length > 0) {
    bonificacionesTable.innerHTML = propietario.bonificaciones.map(bonificacion => `
      <tr>
        <td>${bonificacion.nombreBonificacion}</td>
        <td>${bonificacion.nombrePuesto}</td>
        <td>${bonificacion.fechaAsignada}</td>
      </tr>
    `).join('');
  } else {
    bonificacionesTable.innerHTML = '<tr><td colspan="3" class="text-center text-muted">Sin bonificaciones asignadas</td></tr>';
  }
}

async function mostrar_exito(mensaje) {
  await mostrarMensaje(mensaje);
}

async function mostrar_error(mensaje) {
  await mostrarMensaje(mensaje);
}

// Ocultar sección de propietario al cargar la página
document.addEventListener('DOMContentLoaded', function() {
  const propietarioSection = document.getElementById('propietarioSection');
  if (propietarioSection) {
    propietarioSection.style.display = 'none';
  }
});

