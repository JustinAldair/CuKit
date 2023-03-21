const connection = require("./conexion");

class ModComentarios {
  constructor(idReceta, usuario, comentario, fecha, idPerfil) {
    this.idReceta = idReceta
    this.usuario = usuario
    this.comentario = comentario
    this.fecha = fecha
    this.idPerfil = idPerfil
  }

  guardarComentario() {
    return new Promise((resolve, reject) => {
      connection.query(`INSERT INTO comentarios(idReceta, usuario, comentario, fecha, idPerfil) 
      VALUES(${this.idReceta}, '${this.usuario}', '${this.comentario}', '${this.fecha}' , ${this.idPerfil})`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

}

module.exports = ModComentarios