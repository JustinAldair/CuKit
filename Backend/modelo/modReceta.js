const connection = require("./conexion");
let fs = require('fs');
const path = require('path');
const { v4: uuidv4 } = require('uuid');


class ModReceta {
  constructor(idPerfil, nombre, descripcion, ingredientes, instrucciones, url_fotos, idCategoria, status, tiempo) {
    this.idPerfil = idPerfil
    this.nombre = nombre
    this.descripcion = descripcion
    this.ingredientes = ingredientes
    this.instrucciones = instrucciones
    this.url_fotos = url_fotos
    this.idCategoria = idCategoria
    this.status = status
    this.tiempo = tiempo
    this.idReceta = null;
  }

  set setIdReceta(idReceta) {
    this.idReceta = idReceta
  }

  obtenerRecetas() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT *, AVG(puntuacion.puntuacion) AS promedio_puntuaciones, receta.nombre as platillo, categoria.nombre as categoria FROM receta 
      LEFT JOIN puntuacion ON puntuacion.idReceta  = receta.idreceta
      INNER JOIN categoria ON receta.idCategoria = categoria.idCategoria
      GROUP BY receta.nombre, categoria.nombre;
      `, (err, row) => {

        if (err) return reject(err)

        return resolve(row)

      })
    })
  }

  obtenerPopulares() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT receta.idreceta, receta.nombre, receta.url_fotos, puntuacion.puntuacion, categoria.nombre as categoria, perfil.url_foto as fotoPerfil FROM puntuacion 
      INNER JOIN receta ON puntuacion.idReceta = receta.idreceta
      INNER JOIN categoria ON categoria.idCategoria = receta.idCategoria
      INNER JOIN perfil ON perfil.idPerfil = receta.idPerfil
      WHERE puntuacion.puntuacion > 4
      LIMIT 3;`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

  obtenerReceta() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT receta.*, AVG(puntuacion.puntuacion) as promedio, categoria.nombre as categoria, perfil.usuario, perfil.url_foto as fotoPerfil FROM receta 
      LEFT JOIN puntuacion ON puntuacion.idReceta = receta.idReceta
      INNER JOIN categoria ON categoria.idCategoria = receta.idCategoria
      INNER JOIN perfil ON perfil.idPerfil = receta.idPerfil
      WHERE receta.idReceta = ${this.idReceta}`, (err, row) => {
        if (err) return reject(err)


        return resolve(row)
      })
    })
  }

  agregarReceta() {

    return new Promise((resolve, reject) => {

      this.subirFoto(this.url_fotos)
        .then(row => {
          connection.query(`INSERT INTO receta(idPerfil, nombre, descripcion, ingredientes, instrucciones, url_fotos, idCategoria, tiempo, status) 
          VALUE(${this.idPerfil}, '${this.nombre}', '${this.descripcion}', '${this.ingredientes}', '${this.instrucciones}', '${row},', ${this.idCategoria},${this.tiempo},false)`, (err, rows) => {
            if (err) return reject(err)

            return resolve(rows)

          })
        })
        .catch(err => {

        })

    })

  }

  subirFoto(base64) {

    const base64Image = base64.split(';base64,').pop();
    const filename = `${uuidv4()}.jpg`;

    const filePath = path.join(__dirname, '../imagenes', filename);

    return new Promise((resolve, reject) => {
      fs.writeFile(filePath, base64Image, { encoding: 'base64' }, (error) => {
        if (error) {
          console.error('Error al guardar la imagen:', error);
          return reject(error)

        } else {
          return resolve(filename)
        }
      });
    });

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
      connection.query(`SELECT comentario, comentarios.usuario, fecha, perfil.url_foto FROM comentarios 
      INNER JOIN receta ON comentarios.idReceta = receta.idReceta
      INNER JOIN perfil ON comentarios.idPerfil = perfil.idperfil
      WHERE comentarios.idReceta = ${this.idReceta}
      ORDER BY idComentarios DESC;
      `, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })
  }

}

module.exports = ModReceta