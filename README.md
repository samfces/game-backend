# Game Backend

Este es un proyecto de backend de ejemplo desarrollado con Spring Boot, que incluye autenticación con JWT, gestión de usuarios, y acceso a base de datos con PostgreSQL. Es parte de mi portfolio profesional, demostrando habilidades en desarrollo Java, Spring Boot y gestión de servicios web.

## Descripción

El proyecto maneja la autenticación de usuarios, tokens JWT, refresco de sesión, y la gestión de contraseñas seguras. Además, se conecta a una base de datos PostgreSQL para el almacenamiento de información del jugador, como credenciales y detalles de sesión.

## Características

- Autenticación con JWT para gestionar sesiones de usuarios.
- Refresco de tokens JWT para mantener sesiones activas.
- Gestión segura de contraseñas utilizando Bcrypt (configurable).
- Sistema de permisos (bitwise) para definir permisos de usuarios.
- Integración con base de datos PostgreSQL.
- Uso de Spring Data JPA para la interacción con la base de datos.
- Seguridad mediante Spring Security.
- Exposición de endpoints REST con Swagger/OpenAPI.
- Configuración de propiedades para JWT y parámetros de seguridad.
- Javadocs generalizados y detallados.

## Requisitos

- Java 17
- PostgreSQL 13 o superior

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/game-backend.git
cd game-backend
````

### 2. Configurar la base de datos

Asegúrate de tener PostgreSQL instalado y ejecutándose. Crea una base de datos con el nombre `minebox-backend` y asegúrate de que las credenciales en el archivo `application.properties` estén correctas.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/game-backend
spring.datasource.username=usuario
spring.datasource.password=contraseña
spring.jpa.hibernate.ddl-auto=update
```

### 3. Compilar y ejecutar

Usa Gradle para compilar y ejecutar el proyecto.

```bash
./gradlew bootRun
```

El backend estará disponible en `http://localhost:8080`.

## Javadocs

Puede acceder a la documentación de todas las clases del proyecto en Javadocs accediendo a: https://samfc.es/docs/game-backend/

## Endpoints Docs

Puede revisar tda documentación de la API REST en Swagger UI. Una vez que el servidor esté en ejecución, puedes acceder a la documentación de la API en: http://localhost:8080/swagger-ui/index.html

### Configuración

Los parámetros de configuración para JWT, refresco de sesión y contraseñas se definen en el archivo `application.properties`:

```properties
app.jwt.secret=secret
app.jwt.expiration-ms=1800000
app.refresh-token.expiration-ms=86400000
app.permissions.default-permissions=VIEW_BALANCE;VIEW_OTHERS_BALANCE
app.password.min-strength=2
```
## Contribuciones

Si deseas colaborar en este proyecto, por favor realiza un fork y abre un pull request con tus cambios.

## Licencia

Este proyecto está bajo la licencia MIT. Para más detalles, consulta el archivo `LICENSE`.

