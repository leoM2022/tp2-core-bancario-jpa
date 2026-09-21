# Trabajo Práctico N°3 - Sistema de Gestión Bancaria

**Universidad Nacional de Jujuy**  
**Cátedra:** Desarrollo y Arquitectura Avanzada de Software

---

## 👥 Equipo de Desarrollo
* **Leandro Mamani**
* **Héctor Daniel Yevara**

---

## 📝 Descripción del Proyecto
Este proyecto implementa el núcleo (Core) de un sistema de gestión financiera basado en una arquitectura en capas. El dominio principal abarca la administración integral de clientes, el manejo de diferentes tipos de cuentas bancarias (Cajas de Ahorro y Cuentas Corrientes) y el procesamiento seguro de transacciones.

El diseño prioriza la integridad transaccional, el manejo riguroso de excepciones y la validación estricta de reglas de negocio operativas (como el control de saldos y márgenes de descubierto).

---

## 🛠️ Stack Tecnológico y Arquitectura
El sistema está desarrollado aplicando principios SOLID y buenas prácticas de codificación (Clean Code).

* **Plataforma:** Java (Spring Boot)
* **Persistencia de Datos:** Spring Data JPA / Hibernate
* **Estructura de Datos:** Inicialización automatizada a través de `data.sql` y configuración en `application.yml`.
* **Testing:** Pruebas unitarias aisladas utilizando JUnit 5 y Mockito.

### Diseño en Capas
1. **Model/Entities:** Clases de dominio ricas (`Cliente`, `CuentaBancaria`, `Transaccion`) que extienden de `EntidadAuditable` para el seguimiento de cambios.
2. **Repository:** Interfaces de acceso a datos encapsulando consultas específicas.
3. **Service:** Lógica de negocio centralizada con control transaccional (`@Transactional`).
4. **Exception:** Manejo personalizado de errores (`RecursoNoEncontradoException`, `RecursoDuplicadoException`).

---

## ⚙️ Características Principales

### Gestión de Clientes
* Operaciones CRUD completas.
* Validaciones de unicidad a nivel de servicio para atributos críticos (CUIL y Correo Electrónico).

### Operaciones Financieras
* **Cuentas Soportadas:** Manejo polimórfico de `CajaAhorro` y `CuentaCorriente`.
* **Depósitos y Extracciones:** Lógica transaccional que previene condiciones de carrera e inconsistencias.
* **Descubiertos:** Soporte dinámico para márgenes de descubierto en cuentas corrientes.
* **Estados:** Validación estricta del `EstadoCuenta` (ej. Activa/Bloqueada) previo a cualquier movimiento de fondos.

---

## 🧪 Pruebas y Cobertura
El proyecto cuenta con un conjunto de pruebas unitarias enfocadas en la capa de servicios (`ClienteServiceTest`, `CuentaBancariaServiceTest`). Los tests validan el comportamiento ante escenarios de éxito y garantizan que las restricciones del negocio levanten las excepciones adecuadas ante datos inválidos o fondos insuficientes.