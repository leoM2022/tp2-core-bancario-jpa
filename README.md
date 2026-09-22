# Sistema de Gestión Bancaria (Core Bancario JPA)

* **Institución:** Universidad Nacional de Jujuy (UNJu).
* **Cátedra:** Desarrollo y Arquitectura Avanzada de Software (DAAS).
* **Repositorio Remoto:** [tp2-core-bancario-jpa](https://github.com/leoM2022/tp2-core-bancario-jpa)

---

## 👥 Equipo de Desarrollo y Roles

* **Héctor Daniel Yevara (Dyevara23):** Desarrollador 1.
  * **Rama de trabajo:** `feature/domain-audit-entities`
  * **Responsabilidades:** Modelado del dominio JPA, jerarquía de herencia relacional, superclase de auditoría, enumeraciones y restricciones Bean Validation.

* **Leandro Mamani (leoM2022):** Desarrollador 2.
  * **Rama de trabajo:** `feature/services-and-dtos`
  * **Responsabilidades:** Servicios de aplicación transaccionales (`@Transactional`), excepciones de negocio, desacoplamiento y pruebas unitarias aisladas con Mockito.

---

## 📝 Descripción del Proyecto

* **Propósito Central:** Implementación del núcleo transaccional (**Core Bancario**) aplicando principios de diseño desacoplado y Clean Code.
* **Dominio Administrado:**
  * **Gestión integral de clientes:** Personas físicas y jurídicas con validación fiscal de unicidad (**CUIL** y **correo electrónico**).
  * **Catálogo polimórfico de productos financieros:** Cajas de Ahorro y Cuentas Corrientes con margen de giro en descubierto.
  * **Registro y orquestación de movimientos monetarios:** Depósitos y extracciones garantizando integridad relacional y atomicidad.

---

## 🛠️ Stack Tecnológico y Arquitectura

* **Lenguaje:** Java 17 LTS (OpenJDK).
* **Framework Principal:** Spring Boot 3.3.4 (módulos Data JPA, Web, Validation).
* **Capa de Persistencia:** Hibernate ORM 6.5.x sobre motor relacional MySQL 8.x.
* **Pruebas y Verificación:** JUnit 5 y Mockito para testing unitario en memoria.

### Estructura en Capas

* **Capa Model/Entities:** Clases de dominio enriquecidas que heredan de `EntidadAuditable` para trazabilidad de creación y modificación.
* **Capa Repository:** Interfaces Spring Data JPA con métodos derivados (`findByCuil`, `findByEmail`, `findByCbu`) y consultas optimizadas.
* **Capa Service:** Servicios sin estado (*stateless*) gobernados por demarcación declarativa (`@Transactional`).
* **Capa Exception:** Errores de negocio no chequeados (`RecursoDuplicadoException`, `RecursoNoEncontradoException`).

---

## 📁 Estructura Física del Proyecto

* 📁 **tp2-core-bancario-jpa/**
  * 📁 **src/main/java/com/example/demo/**
    * 📁 **exception/** — Excepciones de negocio personalizadas
    * 📁 **model/** — Clases de dominio, auditoría y enums
    * 📁 **repository/** — Interfaces de persistencia JPA
    * 📁 **service/** — Contratos de servicio y sus implementaciones (`impl/`)
  * 📁 **src/main/resources/** — `application.yml` y scripts de inicialización SQL
  * 📁 **src/test/java/.../** — Pruebas unitarias de servicios con Mockito
  * 📄 **pom.xml** — Descriptor de dependencias y compilador Maven
  * 📄 **README.md** — Documentación técnica del proyecto

---

## 🌿 Flujo de Ramas (Git Flow)

* **Rama `main`:** Versión estable, consolidada y evaluable del producto.
* **Rama `develop`:** Rama base de integración continua donde convergen las funcionalidades del equipo.
* **Rama `feature/domain-audit-entities`:** Desarrollador 1 (persistencia, entidades y auditoría).
* **Rama `feature/services-and-dtos`:** Desarrollador 2 (servicios, transaccionalidad y tests unitarios).

---

## ⚙️ Características Operativas

* **Gestión de Clientes:** Altas seguras con comprobación previa de duplicados y listados paginados (`Pageable`) para evitar saturación de memoria en la JVM.
* **Operaciones de Cuentas:** Depósitos y extracciones atómicas con validación obligatoria del estado activo de la cuenta.
* **Cálculo de Fondos:** En cuentas corrientes, la disponibilidad de saldo se evalúa sumando dinámicamente el margen de descubierto asignado.
* **Estrategia Relacional:** Herencia unificada bajo `SINGLE_TABLE` sobre la tabla `cuentas_bancarias` con columna discriminadora `tipo_cuenta` para maximizar el rendimiento.

---

## 🧪 Pruebas Unitarias (Testing)

* **Aislamiento Absoluto:** Verificación de métodos sin levantar el contexto de Spring Boot ni requerir conexión física a bases de datos.
* **Dobles de Prueba:** Uso de `@Mock` para repositorios JPA y simulación de escenarios mediante `when(...).thenReturn(...)`.
* **Suites Incluidas:**
  * **`ClienteServiceTest`:** Comprobación de alta exitosa, control de colisiones por CUIL duplicado y búsquedas por identificador UUID.
  * **`CuentaBancariaServiceTest`:** Verificación de acreditación de fondos, extracciones válidas con descubierto y rechazo ante saldo insuficiente.

---

## 🚀 Comandos de Construcción y Ejecución
* **Compilar y Ejecutar Pruebas Unitarias:**
  ```bash
  ./mvnw clean test
