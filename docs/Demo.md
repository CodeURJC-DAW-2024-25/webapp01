# 🧪 Demo para Práctica 3 de Angular

## Antes de empezar
- Tener datos de ejemplo cargados.
- Tener imágenes asociadas a algunos recursos.
- Tener Docker Compose levantado (`https://localhost:8443/new/`).
- Tener preparada una lista de usuarios (admin y normal).

---

## 1. Presentación inicial
✅ Explicar brevemente qué hace la aplicación.

---

## 2. Funcionalidades principales

### 2.1 Crear un recurso
- Abrir el formulario de creación.
- Subir una imagen.
- Guardar el recurso.

### 2.2 Listar recursos
- Mostrar la lista de recursos ya creados.
- Pulsar "Cargar más" para demostrar la paginación.

### 2.3 Comprobar permisos de usuario
- Cerrar sesión.
- Iniciar sesión como **usuario normal**.
- Intentar realizar acciones de administrador (crear/editar/eliminar) → deberían estar bloqueadas.

### 2.4 Mostrar gráficos
- Navegar a la sección de gráficos (barras, líneas, etc.).
- Confirmar que son los **mismos que en Fase 2**.

### 2.5 Comprobar recarga de pantalla
- Estando en una página interna (lista de recursos, etc.), pulsar **F5** (refrescar).
- Confirmar que permaneces en la misma pantalla.

---

## 3. Aspectos técnicos

- ✅ "Proyecto dockerizado en multi-stage, no necesita `ng` en local."
- ✅ "Accesible en `https://localhost:8443/new/` mediante Docker Compose."

---

## 4. Documentación

- ✅ Diagrama de clases Angular incluido en el `README`.
- ✅ Vídeo de demo subido a YouTube (link en el `README`).
- ✅ Documentación incluye la participación de cada miembro.
