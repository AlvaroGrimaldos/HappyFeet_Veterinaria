# Sistema de Gestión Veterinaria "Happy Feet" by Alvaro Andres Grimaldos - Nicolas Ardila Rosas

## 📌 Descripción del Contexto
La veterinaria **Happy Feet** necesitaba un sistema que le permitiera gestionar de manera eficiente la información de sus clientes, mascotas, tratamientos, citas, facturación y compras.  
El sistema desarrollado resuelve esta necesidad proporcionando un entorno centralizado para la administración de datos, mejorando el control interno y optimizando los procesos diarios de la veterinaria.

## 🛠️ Tecnologías Utilizadas
- **Java 17** → Lenguaje principal de programación.
- **MySQL** → Sistema de gestión de bases de datos relacional.
- **JDBC** → Conexión entre Java y MySQL.
- **Maven** → Gestión de dependencias y construcción del proyecto.
- **Git & GitHub** → Control de versiones y repositorio.

## ⚙️ Funcionalidades Implementadas
El sistema incluye diferentes módulos que permiten:
- **Gestión de Clientes y Mascotas**: Registro, consulta, actualización y eliminación de información.
- **Agenda de Citas**: Programación y control de citas médicas.
- **Inventario y Compras**: Control de insumos y relación con proveedores.
- **Ventas y Facturación**: Generación de facturas y gestión de pagos.
- **Reportes**: Estadísticas de clientes, tratamientos más comunes y ventas.
- **Seguridad y Roles**: Usuarios con diferentes permisos (administrador, veterinario, asistente).

### Principales Reglas de Negocio
- Una mascota siempre está asociada a un cliente.
- Las citas solo pueden programarse con veterinarios activos en el sistema.
- No se permite registrar una venta sin existencia en inventario.
- El control de acceso está definido por roles.

## 🗄️ Modelo de la Base de Datos
El modelo relacional incluye **17 tablas** interconectadas (CLIENTE, MASCOTA, VETERINARIO, CITA, PRODUCTO, VENTAS, COMPRAS, etc.).

📌 Cada cliente puede tener varias mascotas.  
📌 Las citas están asociadas a una mascota y un veterinario.  
📌 Las ventas y compras mantienen integridad referencial con productos y proveedores.

> Se recomienda consultar el archivo `diagrama_ER.png` incluido en el repositorio para visualizar el diagrama Entidad-Relación completo.

## 🚀 Instrucciones de Instalación y Ejecución

### ✅ Requisitos Previos
- JDK 17 instalado y configurado en el PATH.
- Maven instalado.
- MySQL Server en ejecución.

### 📂 Clonación del Proyecto
```bash
https://github.com/AlvaroGrimaldos/HappyFeet_Veterinaria.git
