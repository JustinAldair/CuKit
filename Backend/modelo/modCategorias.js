const connection = require("./conexion");

class ModCategorias {
  constructor(nombre) {
    this.idcategoria = null
    this.nombre = nombre
    this.icon = null
  }



  obtenerCategorias() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT *FROM categoria`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

}


module.exports = ModCategorias