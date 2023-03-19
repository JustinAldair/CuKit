const express = require('express')

const app = express()
const Router = require('./routes/rutas')
const bodyParser = require('body-parser')

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({ extended: true }))
app.use('/', Router)

app.listen(3000, () => {
  console.log("Servidor en http://localhost:3000")
})