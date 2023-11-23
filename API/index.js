const express = require('express');
const app = express();
const mongoose = require("mongoose")
const router = express.Router();
const PORT = 8080;


mongoose.connect('mongodb+srv://mcmp2023:sybmarketplace@clustersybmarketplace.nhtcitw.mongodb.net/sybmarketplace?retryWrites=true&w=majority', {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
  .then(() => {
    console.log('Conexión exitosa a la base de datos');
  })
  .catch((error) => {
    console.error('Error al conectar a la base de datos', error);
  });

app.use(express.json())
app.use(require("./routes/routes"));

app.listen(PORT, () => console.log("corriendo en el puerto" + PORT))
