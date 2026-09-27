# Gimnasio Plus Ultra

Sistema de gestión para un gimnasio, pensado para controlar clientes, entrenadores, planes de suscripción, asistencia y sesiones.

## Descripción

Este proyecto está desarrollado con Java y Spring Boot, con una estructura basada en capas de controladores, servicios, repositorios, modelos y DTOs.

Incluye módulos para:

- Clientes
- Entrenadores
- Usuarios y roles
- Planes de suscripción
- Asistencia
- Sesiones
- Estados del sistema

## Tecnologías

- Java 25
- Spring Boot 4.1.1
- Spring Data JPA
- Spring Web MVC
- MySQL
- Maven

## Estructura del repositorio

```text
gimnasio-plus-ultra/
├── back
├── front
└── README.md
```

## Requisitos previos

- JDK 25 instalado
- MySQL en ejecución
- Maven o uso del wrapper incluido

## Configuración de la base de datos

La configuración principal está en:

`gimnasio-plus-ultra/src/main/resources/application.properties`

Debe apuntar a tu base de datos local MySQL. Ejemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:puerto-de-mysql/nombre-base-de-datos
spring.datasource.username=nombre-usuario-mysql
spring.datasource.password=contrasenia-de-usario-mysql
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

Asegúrate de crear la base de datos antes de ejecutar la app.

## Ejecutar el proyecto

Desde la raíz del repositorio:

```powershell
./mvnw.cmd spring-boot:run
```

O con Maven:

```powershell
mvn spring-boot:run
```

La aplicación normalmente queda disponible en:

```text
localhost
```

## Funcionalidades principales

- Registro y gestión de clientes
- Administración de entrenadores
- Control de usuarios y permisos por roles
- Planes de membresía
- Registro de asistencia
- Gestión de sesiones del gimnasio

## Convenciones

- Los modelos representan las entidades principales.
- Los repositorios gestionan acceso a datos con JPA.
- Los servicios contienen la lógica de negocio.
- Los controladores expiden endpoints REST.
- Los DTOs separan la representación de entrada y salida.

## Estado del proyecto

Este proyecto está en desarrollo y preparado para continuar expandiéndose con más funcionalidades del sistema del gimnasio.

## Autores

Marcela.
