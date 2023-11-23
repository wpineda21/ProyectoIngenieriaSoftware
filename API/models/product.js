const mongoose = require("mongoose")

const productSchema = new mongoose.Schema({
    id: Number,
    titulo: String,
    descripcion: String,
    precio: Number,
    numerodecontacto: String,
    categoria: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Categoria'
    },
    idcliente: String,
  });
  
module.exports = mongoose.model('Product',  productSchema)


  
