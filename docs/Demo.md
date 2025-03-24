# 🎥 Demo para el Profesor – Práctica 2: API REST y Docker

## 🔍 1. Funcionalidades principales (desde el navegador)

- **Inicio de sesión y registro de usuario**
  - Mostramos cómo registrarse y loguearse con distintos roles: usuario normal y admin.

- **Operaciones básicas del CRUD desde la interfaz**
  - Crear, leer, actualizar y borrar entidades principales de la aplicación (por ejemplo: notas, usuarios, categorías…).
  - Incluimos subida de imágenes (si aplica) en estas operaciones.

- **Consultas especiales**
  - Ejemplo de búsqueda o filtrado (por nombre, fecha, usuario…).
  - Recomendaciones.

- **Control de permisos**
  - Mostramos que los usuarios autenticados y admins ven distintas cosas.
  - Verificamos que un usuario no puede editar lo que no le pertenece.

---

## 📬 2. API REST con Postman

- **Colección completa**
  - Mostramos que tenemos una colección de Postman bien organizada por carpetas (usuarios, notas, etc).
  - Todas las operaciones básicas están cubiertas (GET, POST, PUT, DELETE).

- **Datos de ejemplo**
  - Enseñamos cómo se pueden usar los entornos de Postman para cambiar la URL del servidor o el token JWT.

- **Consultas especiales y paginación**
  - Endpoint `/api/notas?page=1&size=5` funcionando.
  - Endpoint de búsqueda `/api/notas/search?titulo=algo`.

- **Subida y descarga de imágenes**
  - Mostramos un `POST` con `form-data` para subir imágenes.
  - Mostramos cómo acceder a la imagen desde una URL pública.

- **Buenas prácticas REST**
  - Las URLs no contienen verbos, usamos status codes adecuados, y devolvemos headers como `Location` tras crear un recurso.

---

## 🔐 3. Seguridad y Autenticación

- **Control de acceso por rol y dueño**
  - Mostramos con Postman cómo se impide acceso sin token o con token no autorizado.
  - Verificamos que un usuario sólo puede modificar sus propios recursos.

- **Contraseñas seguras**
  - Demostramos que las contraseñas no son visibles ni al crear usuario ni al consultar perfil.

---

## 🛠️ 4. Docker y despliegue

- **Aplicación desplegada en máquina virtual**
  - Accedemos a la app desde el navegador del servidor.

- **Docker**
  - Mostramos el `Dockerfile` multi-stage.
  - Mostramos el `docker-compose.yml` con imágenes propias y de DockerHub (por ejemplo, base de datos).

---

## 📚 5. Documentación

- **Documentación de la API**
  - Accesible desde la web: explicamos brevemente el uso de Swagger o similar.

- **Despliegue**
  - Enseñamos la guía/documento donde explicamos cómo levantar el sistema con Docker.

- **Diagrama de clases**
  - Mostramos un esquema con los nuevos `RestController`, servicios y DTOs.

- **Reparto del trabajo**
  - Enseñamos en el README cómo se ha repartido el trabajo entre los miembros del grupo.

---

## ✅ Cierre

Todo lo mostrado está funcionando sin errores. La aplicación sigue las buenas prácticas de REST, está completamente dockerizada y cumple todos los requisitos de la práctica.

