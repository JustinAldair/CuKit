const ModReceta = require("../modelo/modReceta");

class Receta {

  cargarRecetas(req, res) {

    let _ModReceta = new ModReceta()

    _ModReceta.obtenerRecetas()
      .then(rows => {
        res.send({
          msg: "Recetas Obtnenidas",
          res: rows,
          status: true
        })
      })
      .catch(err => {
        res.send({
          msg: "Error al Obtener la Recetas",
          status: false
        })
      })
  }

  obtenerPopulares(req, res) {
    let _ModReceta = new ModReceta()

    _ModReceta.obtenerPopulares()
      .then(rows => {
        res.send({
          msg: "Recetas Obtnenidas",
          res: rows,
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Obtener la Recetas",
          status: false,
        })
      })
  }

  cargarReceta(req, res) {
    let _ModReceta = new ModReceta()
    _ModReceta.setIdReceta = req.params.idReceta

    _ModReceta.obtenerReceta()
      .then(rows => {
        res.send({
          msg: "Receta Obtnenidas",
          res: rows,
          status: true
        })
      })
      .catch(err => {
        res.send({
          msg: "Error al Obtener la Receta",
          status: false
        })
      })
  }

  agregarReceta(req, res) {

    let { idPerfil, nombre, descripcion, ingredientes, instrucciones, url_fotos, idCategoria, tiempo, status } = req.body

    let _ModReceta = new ModReceta(idPerfil, nombre, descripcion, ingredientes, instrucciones, url_fotos, idCategoria, status, tiempo);

    _ModReceta.agregarReceta()
      .then(row => {
        res.send({
          msg: "Nueva Receta Agregada",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Agregar la Receta Intentelo más Tarde",
          status: false
        })
      })

  }

  eliminarReceta(req, res) {

    let _ModReceta = new ModReceta();
    _ModReceta.setIdReceta = req.params.idReceta;

    _ModReceta.eliminarReceta()
      .then(row => {
        res.send({
          msg: "Receta Eliminada",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Eliminar Receta",
          status: false
        })
      })

  }

  obtenerPuntuacion(req, res) {

    let _ModReceta = new ModReceta()
    _ModReceta.setIdReceta = req.params.idReceta
    _ModReceta.obtenerPuntuacion()
      .then((row => {
        console.log(row)
        res.send({
          msg: "Puntuación Obtenida",
          res: row[0]['AVG(puntuacion)'],
          status: false
        })
      }))
      .catch(err => {
        res.send({
          msg: "Error al Obtener la puntuación de la Receta",
          status: false
        })
      })

  }

  obtenerPuntuaciones(req, res) {

    let _ModReceta = new ModReceta()
    _ModReceta.setIdReceta = req.params.idReceta
    _ModReceta.obtenerPuntuaciones()
      .then((row => {
        console.log(row)
        res.send({
          msg: "Puntuaciones Obtenida",
          res: row,
          status: false
        })
      }))
      .catch(err => {
        res.send({
          msg: "Error al Obtener la puntuaciones de la Receta",
          status: false
        })
      })

  }

  obtenerComentarios(req, res) {
    let _ModReceta = new ModReceta()
    _ModReceta.setIdReceta = req.params.idReceta
    _ModReceta.obtenerComentarios()
      .then((row => {
        console.log(row)
        res.send({
          msg: "Comentarios Obtenidos",
          res: row,
          status: true
        })
      }))
      .catch(err => {
        res.send({
          msg: "Error al Obtener los comentarios de la Receta",
          status: false
        })
      })
  }

}

module.exports = Receta