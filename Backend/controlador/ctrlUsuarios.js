const modelUsuario = require('../modelo/modUsuarios')
const modelPerfil = require('../modelo/modPerfil')
const modelComentarios = require('../modelo/modComentarios')
const modeloPuntuacion = require('../modelo/modPuntacion')

class Usuario {


  crearCuenta(req, res) {

    let { nombre, apellido_p, apellido_m, pass, correo, usuario, url_foto } = req.body
    let _model = new modelUsuario(nombre, apellido_p, apellido_m, pass, correo, usuario, url_foto)

    //VALIDACIONES NO SE PERMITEN CAMPOS VACIOS
    if (nombre.length == 0 || apellido_p == 0 || apellido_m == 0 || pass == 0 || correo == 0) {
      res.send({ error: "Campos Vacios", status: false })
      return
    }

    //SI CUENTA EXISTE ENTONCES LA OPERACIÓN SE CANCELA
    _model.encontrarCuenta()
      .then(row => {

        //CREO LA CUENTA
        _model.crearCuenta()
          .then(row => {
            res.send({
              status: true
            })
          })
          .catch(err => {
            console.log(err)
            res.send({
              status: false
            })
          })

      })
      .catch(err => {
        res.send({
          error: "El correo ingresado ya se encuentra registrado",
          status: false
        })
      })


  }

  recuperarCuenta(req, res) {

  }

  editarPerfil(req, res) {

    let _model = new modelPerfil(req.params.idPerfil)
    _model.setUsuario = req.body.usuario
    _model.setFoto = req.body.url_foto
    _model.setPais = req.body.pais

    _model.editarPerfil()
      .then(row => {
        res.send({
          msg: "Perfil Actualizado",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Actualizar el Pefil",
          status: false
        })
      })


  }

  comentar(req, res) {

    let { idReceta, usuario, comentario, fecha, idPerfil } = req.body
    let _modelComentarios = new modelComentarios(idReceta, usuario, comentario, fecha, idPerfil)

    _modelComentarios.guardarComentario()
      .then(row => {
        res.send({
          msg: "Comentario Agregado",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Guardar el Comentario",
          status: false
        })
      })


  }

  puntuarReceta(req, res) {

    let { idReceta, usuario, puntuacion, fecha } = req.body

    let _modeloPuntuacion = new modeloPuntuacion(idReceta, usuario, puntuacion, fecha)
    _modeloPuntuacion.guardarPuntuacion()
      .then(() => {
        res.send({
          msg: "Puntuación Guardada",
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Guarar la Puntuación",
          status: false
        })
      })
  }

  iniciarSesion(req, res) {

    let { pass, correo } = req.body
    let _model = new modelUsuario()
    _model.setCorreo = correo
    _model.setPass = pass

    _model.iniciarSesion()
      .then(row => {
        res.send({
          msg: "Usuario Autenticado",
          auth: row["token"],
          idPerfil: row["row"][0]["idperfil"],
          status: true
        })
      })
      .catch(err => {
        console.log(err)
        res.send({
          msg: "Error al Autenticar el Usuario",
          status: false
        })
      })

  }

}


module.exports = Usuario

