/**
 * Emular Tránsito - JavaScript
 * Handles form interactions and transit emulation
 */

urlIniciarVista = "/usuarios/admins/emular-transito/vistaConectada";

// DOM Elements
const divPuestos = document.getElementById("divPuestos");
const matriculaInput = document.getElementById("matriculaInput");
const fechaHoraInput = document.getElementById("fechaHoraInput");
const emularBtn = document.getElementById("emularBtn");
const tarifasSection = document.getElementById("tarifasSection");
const resultadoSection = document.getElementById("resultadoSection");

// Establecer fecha y hora actual como valor por defecto
function establecerFechaActual() {
  const ahora = new Date();
  // Formato: YYYY-MM-DDTHH:MM
  const año = ahora.getFullYear();
  const mes = String(ahora.getMonth() + 1).padStart(2, '0');
  const dia = String(ahora.getDate()).padStart(2, '0');
  const horas = String(ahora.getHours()).padStart(2, '0');
  const minutos = String(ahora.getMinutes()).padStart(2, '0');
  
  const fechaHoraLocal = `${año}-${mes}-${dia}T${horas}:${minutos}`;
  fechaHoraInput.value = fechaHoraLocal;
}

// Establecer fecha actual al cargar la página
establecerFechaActual();

function handlePuestoChange(indice, objeto) {
  console.log("Cargar tarifas para el puesto", indice, objeto);
  submit("/usuarios/admins/emular-transito/tarifas", serializarFormulario("emularTransitoForm"));
}

function handleEmularClick() {
  console.log("Emular tránsito");
  submit("/usuarios/admins/emular-transito/registrar", serializarFormulario("emularTransitoForm"));
}

function mostrar_puestos(puestos) {
  divPuestos.innerHTML = crearListaDesdeJson({
    data: puestos,
    campoMostrar: 'nombre',
    multiple : false,
    mostrarOpcionInicial : true, 
    onSelectItem: handlePuestoChange,
    id : 'posPuesto'
  });
  
  // Seleccionar el primer puesto por defecto si hay puestos disponibles
  if (puestos && puestos.length > 0) {
    const selectPuesto = document.getElementById('posPuesto');
    if (selectPuesto) {
      // Seleccionar el primer puesto (índice 0)
      selectPuesto.selectedIndex = 1; // 1 porque 0 es la opción inicial vacía
      // Cargar las tarifas del primer puesto
      handlePuestoChange(0, puestos[0]);
    }
  }
}

function mostrar_tarifas(tarifas) {
  console.log("Mostrar tarifas:", tarifas);
  const tarifasTableContainer = document.getElementById("tarifasTableContainer");
  tarifasTableContainer.innerHTML = crearTablaDesdeJson(tarifas);
  // Agregar clase "table table-sm table-bordered align-middle" a la tabla generada
  const tabla = tarifasTableContainer.querySelector("table");
  if (tabla) {
    tabla.className = "table table-sm table-bordered align-middle";
  }
}

async function mostrar_error(mensaje) {
  await mostrarMensaje(mensaje);
}

async function mostrar_exito(mensaje) {
  await mostrarMensaje(mensaje);
}

function mostrar_transito(transito) {
  console.log("Transito", transito);
  
  // Mostrar la sección de resultados
  if (resultadoSection) {
    resultadoSection.style.display = "block";
  }
  
  // Actualizar los elementos con los datos del tránsito (ya formateados desde el backend)
  const resultPropietario = document.getElementById("resultPropietario");
  const resultCategoria = document.getElementById("resultCategoria");
  const resultBonificacion = document.getElementById("resultBonificacion");
  const resultCosto = document.getElementById("resultCosto");
  const resultSaldo = document.getElementById("resultSaldo");
  
  // Mostrar propietario formateado: "Juan Pérez (Activo)"
  if (resultPropietario && transito.propietario) {
    resultPropietario.textContent = transito.propietario;
  }
  
  // Mostrar categoría formateada: "A (Auto)"
  if (resultCategoria && transito.categoria) {
    resultCategoria.textContent = transito.categoria;
  }
  
  // Mostrar bonificación formateada: "Residente Costa de Oro (10%)" o "Sin bonificación"
  if (resultBonificacion && transito.bonificacion) {
    resultBonificacion.textContent = transito.bonificacion;
  }
  
  // Mostrar costo del tránsito formateado
  if (resultCosto && transito.montoPagado !== undefined) {
    resultCosto.textContent = `$ ${transito.montoPagado.toFixed(2)}`;
  }
  
  // Mostrar saldo restante formateado
  if (resultSaldo && transito.saldoRestante !== undefined) {
    resultSaldo.textContent = `$ ${transito.saldoRestante.toFixed(2)}`;
  }
}
