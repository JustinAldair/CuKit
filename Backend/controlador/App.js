const ModCategorias = require("../modelo/modCategorias");
const ModReceta = require("../modelo/modReceta");
const ModUsuarios = require("../modelo/modUsuarios");

class App {

  subirFoto(req, res) {
    let _ModReceta = new ModReceta()

    _ModReceta.subirFoto(req.body.base64)
      .then(row => {
        res.send({
          status: true,
          res: row
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          status: false
        })
      })


  }

  cargarCategorias(req, res) {
    let _ModCategorias = new ModCategorias()

    _ModCategorias.obtenerCategorias()
      .then(row => {
        res.send({
          res: row,
          msg: "Categorias Obtenidas",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Obtener las Categorias",
          status: true
        })
      })

  }

  obtnerDatosUsuario(req, res) {
    let _ModUsuarios = new ModUsuarios()

    _ModUsuarios.obtenerInfo(req.params.idPerfil)
      .then(row => {
        res.send({
          res: row,
          msg: "Info Obtenida",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Obtener la información del Usuario",
          status: true
        })
      })
  }

}

module.exports = App