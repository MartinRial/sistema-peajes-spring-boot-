package obligatorio.da.sistemaPeajes;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.Administrador;
import obligatorio.da.sistemaPeajes.dominio.Categoria;
import obligatorio.da.sistemaPeajes.dominio.Exonerado;
import obligatorio.da.sistemaPeajes.dominio.Frecuente;
import obligatorio.da.sistemaPeajes.dominio.Habilitado;
import obligatorio.da.sistemaPeajes.dominio.Penalizado;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Puesto;
import obligatorio.da.sistemaPeajes.dominio.Tarifa;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;
import obligatorio.da.sistemaPeajes.servicios.Fachada;

@SpringBootApplication
public class SistemaPeajesApplication {

    public static void main(String[] args) throws PeajeException, ParseException {
        SpringApplication.run(SistemaPeajesApplication.class, args);
        cargarDatosDePrueba();
    }

    private static void cargarDatosDePrueba() throws PeajeException, ParseException {
        /* 
         * 
         * Para el caso de los Administradores y Propietarios, 
         * debe precargar al menos 2 de cada tipo y 
         * debe considerarse que uno de ellos se precargue con la siguiente información:
        Administrador
        Cédula de identidad: 12345678
        Contraseña: admin.123
        Nombre completo: Usuario Administrador
        */
        Administrador admin1 = new Administrador("12345678", "admin.123", "Usuario Administrador");
        Fachada.getInstancia().agregarAdministrador(admin1);
        Administrador admin2 = new Administrador("87654321", "admin.456", "Usuario Administrador 2");
        Fachada.getInstancia().agregarAdministrador(admin2);
        /* 
        Propietario
        Cédula de identidad: 23456789
        Contraseña: prop.123
        Nombre completo: Usuario Propietario
        Saldo actual: 2000
        Saldo mínimo para alerta: 500
        */
        Propietario prop1 = new Propietario("23456789", "prop.123", "Usuario Propietario", 2000, 500);
        Fachada.getInstancia().agregarPropietario(prop1);
        Fachada.getInstancia().cambiarEstadoPropietario(prop1, new Penalizado());
        Fachada.getInstancia().cambiarEstadoPropietario(prop1, new Habilitado());

        Propietario prop2 = new Propietario("98765432", "prop.456", "Usuario Propietario 2", 1500, 300);
        Fachada.getInstancia().agregarPropietario(prop2);

        // Agregar un usuario deshabilitado para pruebas
        Propietario prop3 = new Propietario("34567890", "prop.789", "Usuario Propietario Deshabilitado", 1000, 200);
        prop3.deshabilitar();
        Fachada.getInstancia().agregarPropietario(prop3);

        // Crear categorías según la imagen
        Categoria categoriaA = new Categoria("A (Auto)");
        Categoria categoriaB = new Categoria("B (Camioneta)");
        Categoria categoriaC = new Categoria("C (Camión)");

        // ==================== VEHÍCULOS DEL PROPIETARIO 1 ====================
        // Agregar vehículo con matrícula hardcodeada para pruebas al propietario 1
        Vehiculo v1_1 = new Vehiculo("ABC1234", "Toyota Corolla 2020", "Blanco", categoriaA);
        Fachada.getInstancia().agregarVehiculo(v1_1, prop1);

        Vehiculo v1_2 = new Vehiculo("DEF5678", "Honda Civic 2021", "Negro", categoriaA);
        Fachada.getInstancia().agregarVehiculo(v1_2, prop1);

        Vehiculo v1_3 = new Vehiculo("GHI9012", "Ford Ranger 2022", "Azul", categoriaB);
        Fachada.getInstancia().agregarVehiculo(v1_3, prop1);

        // ==================== VEHÍCULOS DEL PROPIETARIO 2 ====================
        Vehiculo v2_1 = new Vehiculo("JKL3456", "Chevrolet S10 2020", "Rojo", categoriaB);
        Fachada.getInstancia().agregarVehiculo(v2_1, prop2);

        Vehiculo v2_2 = new Vehiculo("MNO7890", "Volkswagen Amarok 2021", "Gris", categoriaB);
        Fachada.getInstancia().agregarVehiculo(v2_2, prop2);

        Vehiculo v2_3 = new Vehiculo("PQR1234", "Scania R450 2019", "Blanco", categoriaC);
        Fachada.getInstancia().agregarVehiculo(v2_3, prop2);

        Vehiculo v2_4 = new Vehiculo("STU5678", "Mercedes Benz Actros 2020", "Verde", categoriaC);
        Fachada.getInstancia().agregarVehiculo(v2_4, prop2);

        // ==================== VEHÍCULOS DEL PROPIETARIO 3 (DESHABILITADO) ====================
        Vehiculo v3_1 = new Vehiculo("VWX9012", "Nissan Frontier 2018", "Plata", categoriaB);
        Fachada.getInstancia().agregarVehiculo(v3_1, prop3);

        Vehiculo v3_2 = new Vehiculo("YZA3456", "Jeep Compass 2019", "Negro", categoriaA);
        Fachada.getInstancia().agregarVehiculo(v3_2, prop3);

        // Cargar Puesto 1 con tarifas estándar (según imagen)
        Puesto puesto1 = new Puesto("Puesto 101 - Acceso Norte", "Ruta 101 Norte");
        puesto1.getTarifas().add(new Tarifa(120.00, categoriaA));
        puesto1.getTarifas().add(new Tarifa(180.00, categoriaB));
        puesto1.getTarifas().add(new Tarifa(280.00, categoriaC));
        Fachada.getInstancia().agregarPuesto(puesto1);
        
        // Cargar Puesto 2 con tarifas diferentes (20% más caras)
        Puesto puesto2 = new Puesto("Puesto 102 - Acceso Sur", "Ruta 102 Sur");
        puesto2.getTarifas().add(new Tarifa(144.00, categoriaA));
        puesto2.getTarifas().add(new Tarifa(216.00, categoriaB));
        puesto2.getTarifas().add(new Tarifa(336.00, categoriaC));
        Fachada.getInstancia().agregarPuesto(puesto2);
        
        // Cargar Puesto 3 con tarifas diferentes (10% más baratas)
        Puesto puesto3 = new Puesto("Puesto 103 - Acceso Centro", "Ruta 103 Centro");
        puesto3.getTarifas().add(new Tarifa(108.00, categoriaA));
        puesto3.getTarifas().add(new Tarifa(162.00, categoriaB));
        puesto3.getTarifas().add(new Tarifa(252.00, categoriaC));
        Fachada.getInstancia().agregarPuesto(puesto3);

        // Asignar bonificaciones de ejemplo al propietario 1 (Usuario Propietario - 23456789)
        // Bonificación Exonerado para Puesto 101
        Fachada.getInstancia().asignarBonificacion(prop1, puesto1, new Exonerado());
        
        // Bonificación Frecuente para Puesto 102
        Fachada.getInstancia().asignarBonificacion(prop1, puesto2, new Frecuente());
        
        // Asignar bonificación al propietario 2 (Usuario Propietario 2 - 98765432)
        // Bonificación Frecuente para Puesto 101
        Fachada.getInstancia().asignarBonificacion(prop2, puesto1, new Frecuente());

        // ==================== PRECARGAR TRÁNSITOS ====================
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        // Tránsitos del Propietario 1 (Usuario Propietario - 23456789)
        // Tránsito 1: ABC1234 en Puesto 101 (con bonificación Exonerado = 100% descuento)
        Fachada.getInstancia().registrarTransito(puesto1, v1_1, prop1, sdf.parse("15/11/2024 08:30"));
        
        // Tránsito 2: ABC1234 en Puesto 102 (con bonificación Frecuente = 30% descuento)
        Fachada.getInstancia().registrarTransito(puesto2, v1_1, prop1, sdf.parse("16/11/2024 14:15"));
        
        // Tránsito 3: DEF5678 en Puesto 103 (sin bonificación)
        Fachada.getInstancia().registrarTransito(puesto3, v1_2, prop1, sdf.parse("17/11/2024 10:00"));
        
        // Tránsito 4: GHI9012 (Camioneta) en Puesto 101 (con bonificación Exonerado)
        Fachada.getInstancia().registrarTransito(puesto1, v1_3, prop1, sdf.parse("18/11/2024 16:45"));
        
        // Tránsitos del Propietario 2 (Usuario Propietario 2 - 98765432)
        // Tránsito 5: JKL3456 en Puesto 101 (con bonificación Frecuente)
        Fachada.getInstancia().registrarTransito(puesto1, v2_1, prop2, sdf.parse("15/11/2024 09:00"));
        
        // Tránsito 6: MNO7890 en Puesto 102 (sin bonificación)
        Fachada.getInstancia().registrarTransito(puesto2, v2_2, prop2, sdf.parse("16/11/2024 11:30"));
        
        // Tránsito 7: PQR1234 (Camión) en Puesto 103 (sin bonificación)
        Fachada.getInstancia().registrarTransito(puesto3, v2_3, prop2, sdf.parse("17/11/2024 13:20"));
        
        // Tránsito 8: STU5678 (Camión) en Puesto 101 (con bonificación Frecuente)
        Fachada.getInstancia().registrarTransito(puesto1, v2_4, prop2, sdf.parse("18/11/2024 15:00"));
    }
}