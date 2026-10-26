# Gimnasio Plus Ultra

Sistema web para administrar un gimnasio: registra clientes, vende planes mensuales y anuales, controla la entrada y salida con códigos QR simulados y agenda sesiones con entrenadores personales.

Proyecto de Programación II – Universidad Evangélica de El Salvador.

## Tecnologías

| Parte | Tecnologías |
|---|---|
| Backend | Java 25, Spring Boot 4.1.1, Spring Web MVC, Spring Data JPA (Hibernate), BCrypt |
| Base de datos | MySQL 8 (Laragon), pool de conexiones Tomcat JDBC |
| Frontend | Vue 3, TypeScript, Vite, Tailwind CSS, Vue Router |
| Pruebas | JUnit 5 y Mockito |

## Estructura del repositorio

```text
gimnasio-plus-ultra/
├── gimnasio-plus-ultra/            Backend (Spring Boot)
│   └── src/main/java/.../
│       ├── config/                 Pool de conexiones, CORS y BCrypt
│       ├── controller/             Endpoints REST
│       ├── dto/                    Datos que entran y salen de la API
│       ├── exception/              Excepciones propias y manejador global
│       ├── model/                  Entidades JPA y enums
│       ├── repository/             Acceso a datos
│       ├── service/                Reglas de negocio y tareas programadas
│       └── util/                   Validaciones comunes
├── vue-tailwind-admin-dashboard/   Frontend (Vue 3)
├── bd-plus-ultra-gimnasio.sql      Base de datos con datos de prueba
└── README.md
```

## Requisitos previos

- JDK 25
- Node.js 20 o superior (incluye npm)
- Laragon con MySQL 8 (o cualquier MySQL 8 en el puerto 3306)

## Instalación y ejecución

### 1. Base de datos

1. Abre Laragon y presiona **Iniciar todo**.
2. En phpMyAdmin (o HeidiSQL), crea la base de datos `bd-plus-ultra-gimnasio`.
3. Importa el archivo `bd-plus-ultra-gimnasio.sql` que está en la raíz del repositorio.

La conexión está configurada en `gimnasio-plus-ultra/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd-plus-ultra-gimnasio
spring.datasource.username=root
spring.datasource.password=
```

Si tu MySQL usa otro usuario o contraseña, cámbialos ahí.

### 2. Backend

Desde la carpeta `gimnasio-plus-ultra`:

```powershell
.\mvnw spring-boot:run
```

Queda disponible en `http://localhost:8080`. La API está bajo `http://localhost:8080/api`.

### 3. Frontend

Desde la carpeta `vue-tailwind-admin-dashboard`:

```powershell
npm install
npm run dev
```

Queda disponible en `http://localhost:5173`.

> **Importante:** ejecuta `npm install` la primera vez, antes de `npm run dev`. La carpeta `node_modules` no se sube a GitHub (está en `.gitignore`), y sin ella aparece el error *"vite" no se reconoce como un comando*.

La dirección de la API se configura en el archivo `.env` del frontend:

```text
VITE_API_URL=http://localhost:8080/api
```

## Usuario de prueba

| Rol | Usuario (DUI) | Contraseña |
|---|---|---|
| Administrador | `01234567-9` | `Admin123` |

El nombre de usuario de todo el personal es su DUI.

## Funcionalidades

- **Clientes:** registro sin DUI ni email duplicados, perfil con código QR (clic para copiar) e historial.
- **Suscripciones:** planes mensuales y anuales; la renovación empieza el día siguiente al vencimiento; al cancelar, los planes siguientes se adelantan.
- **Recepción:** entrada y salida por código QR o búsqueda por nombre; rechaza a clientes inactivos o sin plan vigente.
- **Entrenadores y sesiones:** agenda sin choques de horario; estados Programada → Confirmada → Completada, o Cancelada.
- **Usuarios:** roles Administrador, Gerente, Recepcionista y Entrenador.
- **Dashboard:** clientes activos, ingresos del mes, asistencias y sesiones de hoy.
- **Tareas automáticas (cada hora):** marca las suscripciones vencidas y cierra las entradas que no marcaron salida.

## Pruebas unitarias

Desde la carpeta `gimnasio-plus-ultra`:

```powershell
.\mvnw test
```

Resultado esperado: `Tests run: 31, Failures: 0, Errors: 0` y `BUILD SUCCESS`.

## Principales endpoints

| Método | Ruta | Uso |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión |
| GET, POST, PUT, DELETE | `/api/clientes`, `/api/usuarios`, `/api/entrenadores`, `/api/planes-suscripcion`, `/api/sesiones` | CRUD de cada módulo |
| POST | `/api/suscripciones` | Vender una suscripción |
| PUT | `/api/suscripciones/{id}/cancelar` | Cancelar una suscripción |
| POST | `/api/asistencias/qr` | Marcar entrada o salida con el código QR |

## Limitación conocida

El inicio de sesión comprueba el usuario y la contraseña, pero la API no exige un token en las demás peticiones. En producción se agregaría Spring Security con JWT y permisos por rol.

## Autora

Karla Marcela Bonilla Rosales – [github.com/KMarceR](https://github.com/KMarceR)