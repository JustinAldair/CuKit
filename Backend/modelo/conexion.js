const mysql = require('mysql')
const connection = mysql.createConnection({
  host: 'localhost',
  port: '3306',
  user: 'root',
  password: '1234',
  database: 'cukit',
  ssl: {
    rejectUnauthorized: false,
  },
})

connection.connect((err) => {
  if (err) return err;
  console.log("Connected")
})


module.exports = connection;
