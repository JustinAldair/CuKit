const connection = require("./conexion");

class ModComentarios {
  constructor(idReceta, usuario, comentario, fecha) {
    this.idReceta = idReceta
    this.usuario = usuario
    this.comentario = comentario
    this.fecha = fecha
  }

  guardarComentario() {
    return new Promise((resolve, reject) => {
      connection.query(`INSERT INTO comentarios(idReceta, usuario, comentario, fecha) 
      VALUES(${this.idReceta}, '${this.usuario}', '${this.comentario}', '${this.fecha}')`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

}

module.exports = ModComentarios