const connection = require("./conexion");

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
      connection.query(`UPDATE perfil SET usuario='${this.usuario}', url_foto='${this.url_foto}', pais='${this.pais}' WHERE idPerfil = ${this.idPerfil}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })

  }

}

module.exports = ModPerfil;