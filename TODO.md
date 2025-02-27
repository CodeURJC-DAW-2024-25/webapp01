
# TODO

- [ ] arreglar pageable en los posts
- [ ] Productos:
- [ ] Arreglar el render del Administrador
  - [ ] Arreglar el render de la página de productos
  - [ ] Peticiones a la api de los productos y traer productos (del cliente a nuestro servidor)
  - [ ] algoritmo de filtrado - victor
  - [ ] algoritmo de recomendacion
- [ ] controlar las listas de la compra
- [x] acabar la página del perfil - dani
- [x] página de editar perfil - dani
- [ ] implementar funcionalidad página editar perfil
- [ ] revisar el error del login
- [ ] filtros  


# Problemas pendientes de esta rama

1. Problema con el userDTO:
   - El userDTO no contempla el campo name, si añadimos el campo name al userDTO, el register no funciona. Hay que encontrar una solución para que el usuario pueda cambiar su name y otros usuario puedan registrarse.
   - si añadimos el campo name al userDTO, nos devuelve un error relacionado con el campo avatar, no se que sentido tiene porque la ruta hace un post a settings de nuevo y settings recupera el avatar del getAuthenticatedUser.

idea -> creo que hay un problema con la seguridad y modificar los datos del usuario, necesariamente debe desloggearlo, como cuando un usuario se registra, que no se puede autoregistrar, debe loggearse con los datos que ha metido

falta implementar el borrado de la cuenta y la modificación de la contraseña.