const connection = require("./conexion");

class ModPuntuacion {
  constructor(idReceta, usuario, puntuacion, fecha) {
    this.idReceta = idReceta
    this.usuario = usuario
    this.puntuacion = puntuacion
    this.fecha = fecha
  }


  guardarPuntuacion() {

    return new Promise((resolve, reject) => {
      connection.query(`INSERT INTO puntuacion(idReceta, usuario, puntuacion, fecha) 
      VALUE (${this.idReceta}, '${this.usuario}', ${this.puntuacion}, '${this.fecha}')`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)

      })
    })

  }



}

module.exports = ModPuntuacion