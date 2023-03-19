//JWT (JSON Web Tokens) permite la creación de tokens de seguridad para trasmitir información encriptada entre el servidor y cliente consta de 3 partes:
//1.- cabecera -> Generalmente es Bearer que es usado para trasmitir tokens de autenticación
//2.- datos(payload)
//3.- firma digital -> Nos permitira verificar la autenticidad del token.


const connection = require("./conexion");

const jwt = require('jsonwebtoken');//Libraría para usar JWT
require('dotenv').config();//Uso variables de entorno la cual tiene mi firma digital para aplicar JWT


class ModUsuarios {
  constructor(nombre, apellido_p, apellido_m, pass, correo, usuario, url_foto) {
    this.nombre = nombre;
    this.apellido_p = apellido_p;
    this.apellido_m = apellido_m;
    this.pass = pass;
    this.correo = correo;
    this.usuario = usuario;
    this.url_foto = url_foto;
    this.pais = null;
    this.con = connection;
  }

  set setCorreo(correo) {
    this.correo = correo
  }

  set setPass(pass) {
    this.pass = pass
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

  crearCuenta() {

    return new Promise((resolve, reject) => {

      //CREO A UN USUARIO
      connection.query(`INSERT INTO usuario(nombre, apellido_p, apellido_m, pass, correo) 
      VALUES('${this.nombre}', '${this.apellido_p}', '${this.apellido_m}', '${this.pass}', '${this.correo}')`, (err, rows) => {
        if (err) return reject(err)

        //UNA VEZ CREADO EL USUARIO CREO SU PERFIL TOMANDO EL ID DEL USUARIO CREADO
        connection.query(`INSERT INTO perfil(idUsuario, usuario, url_foto, pais) VALUES ('${rows.insertId}', '${this.usuario}', null, null)`, (err, rows) => {
          if (err) return reject(err)

          return resolve(rows)
        })


      })
    })

  }

  //VERIFICO SI YA HAY USUARIOS REGISTRADOS POR SU CORREO
  encontrarCuenta() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT *FROM usuario WHERE correo = '${this.correo}'`, (err, rows) => {
        if (err || rows.length > 0) return reject(err)
        return resolve(rows)
      })
    })
  }

  recuperarCuenta() {

  }

  editarPerfil(id) {

    return new Promise((resolve, reject) => {
      connection.query(`UPDATE perfil SET usuario='${this.usuario}', url_foto='${this.url_foto}', pais='${this.pais}' WHERE idPerfil = ${id}`, (err, row) => {
        if (err) return reject(err)

        return resolve(row)
      })
    })

  }

  iniciarSesion() {
    return new Promise((resolve, reject) => {
      connection.query(`SELECT *FROM usuario WHERE correo = '${this.correo}' and pass='${this.pass}'`, (err, row) => {

        if (err || row.length == 0) return reject(err)

        //MI FIRMA DIGITAL
        const SECRET_KEY = process.env.SECRET_KEY;

        //LOS DATOS A TRASMITIR AL CLIENTE
        const payload = {
          idUsuario: row[0]['idUsuario'],
        };

        //OPCIONES EXTRAS
        const options = {
          expiresIn: '24h'//LA SESIÓN EXPIRARA EN 24H
        };

        //GENERAMOS EL TOKEN CON CON LOS DATOS Y LA FIRMA DIGITAL
        const token = jwt.sign(payload, SECRET_KEY, options);

        //ENVIO EL TOKEN AL CLIENTE; ESTE TOKEN DEBERA SER ALMACENADO EN EL CLIENTE LOCALMENTE PAR FUTURA PETICIONES
        return resolve(token)
      })
    })
  }
}

module.exports = ModUsuarios;