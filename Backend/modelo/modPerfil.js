const connection = require("./conexion");
const ModReceta = require("./modReceta");

class ModPerfil {
  constructor(id) {
    this.idPerfil = id
    this.usuario = null;
    this.url_foto = null;
    this.pais = null;
  }

  set setUsuario(usuario) {
    this.usuario = usuario;
  }

  set setFoto(url_foto) {
    this.url_foto = url_foto
  }

  set setPais(pais) {
    this.pais = pais
  }

  editarPerfil() {
    return new Promise((resolve, reject) => {
      new ModReceta().subirFoto(this.url_foto)
        .then(row => {
          connection.query(`UPDATE perfil SET usuario='${this.usuario}', url_foto='${row}', pais='${this.pais}' WHERE idPerfil = ${this.idPerfil}`, (err, row) => {
            if (err) return reject(err)


            return resolve(row)
          })
        })
        .catch(err => {
          return reject(err)
        })


    })

  }

}

module.exports = ModPerfil;