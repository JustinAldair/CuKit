const ModCategorias = require("../modelo/modCategorias");
const ModReceta = require("../modelo/modReceta");

class App {

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

}

module.exports = App