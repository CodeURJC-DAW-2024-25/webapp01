# 🚀 Guion para la Demo - SaveX

## 1️⃣ Presentación Inicial  
### 📌 Explicación del Proyecto  
- **Nombre:** SaveX - Pricing Comparison Platform  
- **Objetivo:** Permitir a los usuarios comparar precios de productos en diferentes supermercados, crear listas de compras y ahorrar dinero.  
- **Tecnologías usadas:** Spring Boot, MySQL, HTML, CSS, JavaScript, AJAX  

---

## 2️⃣ Inicio y Acceso  
### 🏠 Página Principal (`/`)  
- Explicar la organización de la página (búsqueda de productos, posts, etc.).  
- Destacar el diseño y usabilidad.  
- Uso del camel Case.

### 👥 Tipos de Usuarios  
- **Usuario anónimo:** Puede navegar sin registrarse.  
- **Usuario registrado:** Puede acceder a funciones adicionales.  
- **Administrador:** Tiene acceso al panel de control.  

---

## 3️⃣ Registro y Autenticación  
### 🔐 Registro de Usuario  
1. Muestra el formulario de registro.  
2. Completa los datos y crea una cuenta.  
    userTest@gmail.com
    userTest
    userTest1
3. Inicia sesión con el usuario recién creado.  

### 🚪 Inicio de Sesión y Permisos  
1. Intenta acceder a funciones restringidas sin estar autenticado (debe mostrar un error).  (pagina no exisitente) 
2. Inicia sesión y muestra las nuevas opciones disponibles.  

---

## 4️⃣ Funcionalidades Principales  
### 🔎 Búsqueda y Comparación de Productos  
1. Busca un producto en la barra de búsqueda. --> Producto: leche entera
2. Filtra por supermercado, precio y categoría.
3. Muestra la comparación de precios entre distintos supermercados.  

### 🛍️ Creación y Gestión de Listas de Compras  
1. Crea una nueva lista de compras.  
2. Agrega productos a la lista.  
3. Muestra la comparación de precios dentro de la lista.  
4. Edita y elimina productos de la lista.  

### 📝 Publicaciones y Comentarios  
1. Visualiza un post en la plataforma.  
2. Agrega un comentario al post.  
3. Edita o borra un comentario como usuario autenticado.  
4. Demostración de como un usuario no puede eliminar los comentarios de otros. 

---

## 5️⃣ Interacción con la Base de Datos  
### 💾 Persistencia de Datos  
1. Cierra sesión y vuelve a iniciar para mostrar que los datos persisten.  
2. Accede desde otro navegador o en modo incógnito.  

### 📊 Carga de Datos de Ejemplo  
1. Explica cómo se han precargado los datos de productos, usuarios y precios en la base de datos.  

---

## 6️⃣ AJAX y Dinamismo  
### 🔄 Carga Dinámica con AJAX  
1. Explica cómo se muestran los resultados de búsqueda sin recargar la página. (Enseñar 10 de máximo) 
2. Agrega un producto a la lista de compras y muestra que la lista se actualiza en tiempo real.  

### ❌ Páginas de Error Personalizadas  
1. Intenta acceder a una página inexistente y muestra el diseño del error.  
2. Intenta realizar una acción sin los permisos adecuados (ej. eliminar un post sin ser admin).  

---

## 7️⃣ Funcionalidad del Administrador  
### 🎛️ Acceso al Panel de Control  
1. Inicia sesión como Administrador.  
2. Muestra como funcionana las gráficas.
3. Muestra cómo los administradores pueden gestionar usuarios, productos y publicaciones.  
4. Modifica un producto y muestra que el cambio se refleja en la búsqueda.  

### 📝 Creación de Posts  
1. Accede a la sección de creación de posts.  
2. Crea un nuevo post con un título, descripción y una imagen de portada.  
3. Publica el post y verifica que aparece en la lista de publicaciones.  
4. Edita o elimina un post como Administrador.  

---

## 8️⃣ Resumen y Preguntas  
1. Resume las funcionalidades implementadas.  
2. Responde preguntas del profesor.  

---

## ✅ Preparación Adicional  
### 📝 Datos de Prueba Listos  
- Un usuario registrado para la demo.  
- Listas de compras y productos precargados.  
- Un usuario administrador con acceso al panel de control.  

### 🖼️ Imágenes Preparadas  
- Para pruebas de subida de imágenes y posts.  

### 🖥️ Simulación de Usuarios  
- Tener varios navegadores abiertos o usar modo incógnito.  

### 🛠️ Comprobaciones Finales  
- Verificar la base de datos antes de la demo para asegurarse de que todo está en orden.  

