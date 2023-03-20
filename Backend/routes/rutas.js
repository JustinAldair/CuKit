const express = require('express')
const Router = express.Router()
const Usuario = require('../controlador/ctrlUsuarios')
const Receta = require('../controlador/ctrlRecetas')
const App = require('../controlador/App')

//MÉTODO DE SEGURIDAD PARA PROTEGER CADA RUTA CON JWT
const { verificar } = require('../seguridad/seguridad_JWt')

let _App = new App()
let _Usuario = new Usuario()
let _Receta = new Receta()

//RUTAS USUARIOS
Router.post('/usuario/registrarse', _Usuario.crearCuenta)
Router.post('/usuarios/auth', _Usuario.iniciarSesion)

//RUTAS PERFIL
Router.put('/perfil/actualizar/:idPerfil', verificar, _Usuario.editarPerfil)


//RUTAS RECETAS
Router.post('/recetas/agregar', verificar, _Receta.agregarReceta)
Router.delete('/recetas/eliminar/:idReceta', verificar, _Receta.eliminarReceta)
Router.get('/recetas/puntuacion/:idReceta', verificar, _Receta.obtenerPuntuacion)
Router.get('/recetas/puntuaciones/:idReceta', verificar, _Receta.obtenerPuntuaciones)
Router.get('/recetas/comentarios/:idReceta', verificar, _Receta.obtenerComentarios)
Router.get('/recetas', verificar, _Receta.cargarRecetas)
Router.get('/recetas/receta/:idReceta', verificar, _Receta.cargarReceta)
Router.get('/recetas/populares', verificar, _Receta.obtenerPopulares)


//RUTAS COMENTARIOS
Router.post('/comentarios/agregar', verificar, _Usuario.comentar)
Router.post('/puntuacion/agregar', verificar, _Usuario.puntuarReceta)


//RUTAS CATEGORIAS
Router.get('/categorias', verificar, _App.cargarCategorias)

module.exports = Router;