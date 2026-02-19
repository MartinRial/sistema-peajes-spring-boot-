/**
 * Panel de Control del Propietario - JavaScript
 * Maneja la visualización de datos del propietario, vehículos, tránsitos, bonificaciones y  // Agregar cada tránsito
  transitos.forEach(transito => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td class="col-puesto">${transito.puesto || ""}</td>
      <td class="col-matricula"><strong>${transito.matricula || ""}</strong></td>
      <td class="col-categoria">${transito.categoria || ""}</td>
      <td class="text-end col-monto-tarifa">$ ${(transito.montoTarifa || 0).toFixed(2)}</td>
      <td class="col-bonificacion">${transito.bonificacion || "Sin bonificación"}</td>
      <td class="text-end text-success col-monto-bonif">-$ ${(transito.montoBonificacion || 0).toFixed(2)}</td>
      <td class="text-end fw-bold col-monto-pagado">$ ${(transito.montoPagado || 0).toFixed(2)}</td>
      <td class="col-fecha">${transito.fecha || ""}</td>
      <td class="col-hora">${transito.hora || ""}</td>
    `;
    transitsTable.appendChild(fila);
  }); */

urlIniciarVista = "/usuarios/propietarios/tablero/vistaConectada";
urlRegistroSSE = "/usuarios/propietarios/tablero/registrarSSE"; // Al cargarse el menu admin se registra el SSE
console.log("Panel");

// ==========================================================
// FUNCIONES PARA MOSTRAR DATOS DEL PROPIETARIO
// ==========================================================

/**
 * Muestra los datos básicos del propietario en el resumen.
 * PASO 1 del CU: Nombre completo, Estado, Saldo actual
 */
function mostrar_datosPropietario(datos) {
  console.log("Datos del propietario:", datos);
  
  // Actualizar nombre completo
  const ownerName = document.getElementById("ownerName");
  if (ownerName && datos.nombreCompleto) {
    ownerName.textContent = datos.nombreCompleto;
  }
  
  // Actualizar estado
  const ownerStatus = document.getElementById("ownerStatus");
  if (ownerStatus && datos.estado) {
    ownerStatus.textContent = datos.estado;
    
    // Cambiar clase según el estado
    ownerStatus.className = "badge";
    const estadoLower = datos.estado.toLowerCase();
    if (estadoLower === "activo" || estadoLower === "habilitado") {
      ownerStatus.classList.add("bg-success");
    } else {
      ownerStatus.classList.add("bg-danger");
    }
  }
  
  // Actualizar saldo actual
  const ownerBalance = document.getElementById("ownerBalance");
  if (ownerBalance && datos.saldoActual !== undefined) {
    ownerBalance.textContent = `$ ${datos.saldoActual.toFixed(2)}`;
  }
}

/**
 * Muestra las bonificaciones asignadas al propietario.
 * PASO 1 del CU: Tabla con las bonificaciones asignadas
 * Información: Nombre de la bonificación, puesto, fecha de asignada
 */
function mostrar_bonificacionesAsignadas(bonificaciones) {
  console.log("Bonificaciones asignadas:", bonificaciones);
  
  const bonificationsTable = document.getElementById("bonificationsTable");
  if (!bonificationsTable) return;
  
  // Limpiar tabla
  bonificationsTable.innerHTML = "";
  
  if (!bonificaciones || bonificaciones.length === 0) {
    bonificationsTable.innerHTML = `
      <tr>
        <td colspan="3" class="text-center text-muted">No hay bonificaciones asignadas</td>
      </tr>
    `;
    return;
  }
  
  // Agregar cada bonificación
  bonificaciones.forEach(bonificacion => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td>${bonificacion.nombreBonificacion || ""}</td>
      <td>${bonificacion.nombrePuesto || ""}</td>
      <td>${bonificacion.fechaAsignada || ""}</td>
    `;
    bonificationsTable.appendChild(fila);
  });
}

/**
 * Muestra los vehículos registrados del propietario.
 * PASO 1 del CU: Tabla de vehículos registrados
 * Información: Matrícula, modelo, color, cantidad de tránsitos y monto total
 */
function mostrar_vehiculosRegistrados(vehiculos) {
  console.log("Vehículos registrados:", vehiculos);
  
  const vehiclesTable = document.getElementById("vehiclesTable");
  if (!vehiclesTable) return;
  
  // Limpiar tabla
  vehiclesTable.innerHTML = "";
  
  if (!vehiculos || vehiculos.length === 0) {
    vehiclesTable.innerHTML = `
      <tr>
        <td colspan="5" class="text-center text-muted">No hay vehículos registrados</td>
      </tr>
    `;
    return;
  }
  
  // Agregar cada vehículo
  vehiculos.forEach(vehiculo => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td><strong>${vehiculo.matricula || ""}</strong></td>
      <td>${vehiculo.modelo || ""}</td>
      <td>${vehiculo.color || ""}</td>
      <td><span class="badge bg-secondary">${vehiculo.cantidadTransitos || 0}</span></td>
      <td class="text-end">$ ${(vehiculo.totalGastado || 0).toFixed(2)}</td>
    `;
    vehiclesTable.appendChild(fila);
  });
}

/**
 * Muestra los tránsitos realizados por el propietario.
 * PASO 1 del CU: Tabla de tránsitos realizados ordenados por fecha/hora descendente
 * Información: Puesto, matrícula, categoría, monto tarifa, bonificación, 
 *              monto bonificación, monto pagado, fecha y hora
 */
function mostrar_transitosRealizados(transitos) {
  console.log("Tránsitos realizados:", transitos);
  
  const transitsTable = document.getElementById("transitsTable");
  if (!transitsTable) return;
  
  // Limpiar tabla
  transitsTable.innerHTML = "";
  
  if (!transitos || transitos.length === 0) {
    transitsTable.innerHTML = `
      <tr>
        <td colspan="9" class="text-center text-muted">No hay tránsitos registrados</td>
      </tr>
    `;
    return;
  }
  
  // Agregar cada tránsito
  transitos.forEach(transito => {
    const fila = document.createElement("tr");
    fila.innerHTML = `
      <td>${transito.puesto || ""}</td>
      <td><strong>${transito.matricula || ""}</strong></td>
      <td>${transito.categoria || ""}</td>
      <td class="text-end" style="padding-right: 15px;">$ ${(transito.montoTarifa || 0).toFixed(2)}</td>
      <td style="padding-left: 15px;">${transito.bonificacion || "Sin bonificación"}</td>
      <td class="text-end text-success">-$ ${(transito.montoBonificacion || 0).toFixed(2)}</td>
      <td class="text-end fw-bold">$ ${(transito.montoPagado || 0).toFixed(2)}</td>
      <td>${transito.fecha || ""}</td>
      <td>${transito.hora || ""}</td>
    `;
    transitsTable.appendChild(fila);
  });
}

/**
 * Muestra las notificaciones del sistema para el propietario.
 * PASO 1 del CU: Tabla de notificaciones del sistema ordenados por fecha/hora descendente
 * Información: Fecha y hora, mensaje
 */
function mostrar_notificaciones(notificaciones) {
  console.log("Notificaciones:", notificaciones);
  
  const notificationsList = document.getElementById("notificationsList");
  if (!notificationsList) return;
  
  // Limpiar lista
  notificationsList.innerHTML = "";
  
  if (!notificaciones || notificaciones.length === 0) {
    notificationsList.innerHTML = `
      <div class="alert alert-secondary" role="alert">
        <i class="bi bi-info-circle me-2"></i>
        No hay notificaciones
      </div>
    `;
    return;
  }
  
  // Agregar cada notificación
  notificaciones.forEach(notificacion => {
    const alerta = document.createElement("div");
    alerta.className = "alert alert-info alert-dismissible fade show mb-2";
    alerta.setAttribute("role", "alert");
    alerta.innerHTML = `
      <i class="bi bi-info-circle me-2"></i>
      <strong>${notificacion.fechaHora || ""}</strong> ${notificacion.mensaje || ""}
    `;
    notificationsList.appendChild(alerta);
  });
}

// ==========================================================
// FUNCIONES PARA MENSAJES Y EVENTOS
// ==========================================================

/**
 * Muestra un mensaje de error al usuario
 */
async function mostrar_error(mensaje) {
  await mostrarMensaje(mensaje);
}

/**
 * Muestra un mensaje de éxito al usuario
 */
async function mostrar_exito(mensaje) {
  await mostrarMensaje(mensaje);
}

/**
 * Muestra un mensaje informativo al usuario
 */
async function mostrar_info(mensaje) {
  await mostrarMensaje(mensaje);
}

function mostrar_usuarioNoAutenticado(urlDestino) {
  console.log("Usuario no autenticado. Redirigiendo a:", urlDestino);
  window.location.href = urlDestino;
}

// Logout function
function logout() {
  submit("acceso/logout", null);
}

function mostrar_logoutExitoso(urlDestino) {
  console.log("Logout exitoso. Redirigiendo a:", urlDestino);
  window.location.href = urlDestino;
}

function borrarNotificaciones() {
  submit("/usuarios/propietarios/tablero/borrarNotificaciones", "");
}


// ==========================================================
// EVENT LISTENERS
// ==========================================================

// Botón para borrar notificaciones
document.getElementById("clearNotifications")?.addEventListener("click", function() {
  submit("/usuarios/propietarios/tablero/borrarNotificaciones", "");
});