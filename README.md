# 🎮 Game Backend – JWT Auth + Spring Boot API

Este es un proyecto backend profesional desarrollado con **Spring Boot**, enfocado en la autenticación segura con **JWT**, gestión de usuarios, y persistencia de datos usando **PostgreSQL**. Forma parte de mi portfolio y demuestra mis habilidades en desarrollo backend moderno con Java y Spring.

---

## 📌 Descripción

La aplicación ofrece un sistema completo de autenticación, incluyendo:
- Emisión y refresco de tokens JWT
- Gestión segura de contraseñas con BCrypt
- Control de acceso basado en permisos bitwise
- Exposición de endpoints REST documentados con Swagger
- Persistencia con Spring Data JPA y PostgreSQL

---

## ✨ Características

- 🔐 Autenticación basada en JWT
- ♻️ Refresco de tokens para sesiones persistentes
- 🔒 BCrypt configurable para encriptar contraseñas
- 🛡️ Sistema de permisos bitwise
- 💾 Integración con PostgreSQL
- 📚 Interacción con la base de datos mediante Spring Data JPA
- 🧱 Seguridad basada en Spring Security
- 📖 Documentación de la API con Swagger/OpenAPI
- ⚙️ Configuración flexible desde `application.properties`
- 🧠 Código documentado con Javadoc detallado

---

## ⚙️ Requisitos

- Java 17
- PostgreSQL 13+

---

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/game-backend.git
cd game-backend
````

### 2. Configurar la base de datos

Asegúrate de tener PostgreSQL instalado y ejecutándose. Crea una base de datos con el nombre `game-backend` y asegúrate de que las credenciales en el archivo `application.properties` estén correctas.

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

---

## 📚 Documentación

### Javadocs

Puedes explorar la documentación completa del código en:
📎 [https://samfc.es/docs/game-backend/](https://samfc.es/docs/game-backend/)

### Swagger API Docs

Una vez ejecutado el servidor, accede a la documentación interactiva de la API en:
📎 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## ⚙️ Configuración

Personaliza la seguridad y comportamiento del backend a través de `application.properties`:

```properties
app.jwt.secret=secret
app.jwt.expiration-ms=1800000
app.refresh-token.expiration-ms=86400000
app.permissions.default-permissions=VIEW_BALANCE;VIEW_OTHERS_BALANCE
app.password.min-strength=2
```

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Si deseas colaborar:

1. Haz un fork del repositorio.
2. Crea una nueva rama con tu funcionalidad.
3. Abre un Pull Request para revisión.


## 📝 Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE).

