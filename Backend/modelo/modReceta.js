const connection = require("./conexion");

class ModReceta {
  constructor(idPerfil, nombre, descripcion, ingredientes, instrucciones, url_fotos, idCategoria, status) {
    this.idPerfil = idPerfil
    this.nombre = nombre
    this.descripcion = descripcion
    this.ingredientes = ingredientes
    this.instrucciones = instrucciones
    this.url_fotos = url_fotos
    this.idCategoria = idCategoria
    this.status = status
    this.idReceta = null;
  }

  set setIdReceta(idReceta) {
    this.idReceta = idReceta
  }

  obtenerRecetas() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT *FROM receta`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

  obtenerReceta() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT *FROM receta WHERE idReceta = ${this.idReceta}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

  agregarReceta() {

    return new Promise((resolve, reject) => {
      connection.query(`INSERT INTO receta(idPerfil, nombre, descripcion, ingredientes, instrucciones, url_fotos, idCategoria, status) 
      VALUE(${this.idPerfil}, '${this.nombre}', '${this.descripcion}', '${this.ingredientes}', '${this.instrucciones}', '${this.url_fotos}', ${this.idCategoria}, ${this.status})`, (err, rows) => {
        if (err) return reject(err)

        return resolve(rows)

      })
    })

  }

  eliminarReceta() {
    return new Promise((resolve, reject) => {
      connection.query(`DELETE FROM receta WHERE idReceta=${this.idReceta}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

  obtenerPuntuacion() {
    return new Promise((resolve, reject) => {
      //USO UN INNER JOIN PARA UNIR LAS TABLAS QUE ESTAN RELACIONADAS ENTRE SI Y DE ESA MANERA OBTENER LA PUNTUACION POR EL ID DE LA RECETA, DE LA MISMA MANERA APLICO EL MÉTODO DEL SQL 'AVG()' PARA CALCULAR EL PROMEDIO DE LAS PUNTUACIONES DE LA RECETA.
      connection.query(`SELECT AVG(puntuacion) FROM puntuacion INNER JOIN receta ON puntuacion.idReceta = receta.idReceta WHERE puntuacion.idReceta = ${this.idReceta}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

  obtenerPuntuaciones() {
    return new Promise((resolve, reject) => {

      //USO UN INNER JOIN PARA UNIR LAS TABLES QUE ESTAN RELACIONADAS ENTRE SI Y DE ESA MANERA OBTENER LAS PUNTUACIONES POR EL ID DE LA RECETA
      connection.query(`SELECT puntuacion, usuario FROM puntuacion INNER JOIN receta ON puntuacion.idReceta = receta.idReceta WHERE puntuacion.idReceta = ${this.idReceta}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

  obtenerComentarios() {
    return new Promise((resolve, reject) => {

      //USO UN INNER JOIN PARA UNIR LAS TABLES QUE ESTAN RELACIONADAS ENTRE SI Y DE ESA MANERA OBTENER LOS COMENTARIOS POR EL ID DE LA RECETA
      connection.query(`SELECT comentario, usuario, fecha FROM comentarios INNER JOIN receta ON comentarios.idReceta = receta.idReceta WHERE comentarios.idReceta = ${this.idReceta}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

}

module.exports = ModReceta