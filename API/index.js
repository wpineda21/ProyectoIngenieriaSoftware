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





/*mongoose.connect('mongodb+srv://mcmp2023:sybmarketplace@clustersybmarketplace.nhtcitw.mongodb.net/?retryWrites=true&w=majority', {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
.then(() => {
  console.log('Conexión exitosa a la base de datos');
})
.catch((error) => {
  console.error('Error al conectar a la base de datos', error);
});


app.use(express.json());
app.use('/', router);


/*app.get('/clientes/:id/posts', (req, res) => {
    const clientId = parseInt(req.params.id);
    const clientPosts = [];
  
    for (let i = 0; i < products.length; i++) {
      if (products[i].idcliente === clientId) {
        clientPosts.push(products[i]);
      }
    }
  
    if (clientPosts.length > 0) {
      res.status(200).send(clientPosts);
    } else {
      res.status(404).send('No se encontraron publicaciones para el cliente especificado');
    }
  });


  app.post('/clientes/add', (req, res) => {
    const { nombre, email, password } = req.body;
    const id = clientes.length + 1;
    const idposts = [];
    const nuevoCliente = { id, nombre, email, password, idposts };
    clientes.push(nuevoCliente);
    res.status(201).json(nuevoCliente);
  });


  app.post('/clientes/:id/posts/create', (req, res) => {
    const clientId = parseInt(req.params.id);
    const { titulo, descripcion, precio, numerodecontacto } = req.body;
    const id = products.length + 1;
    const nuevoProducto = { id, titulo, descripcion, precio, numerodecontacto, idcliente: clientId };
    products.push(nuevoProducto);
    const cliente = clientes.find(c => c.id === clientId);
    if (cliente) {
      cliente.idposts.push(id);
    } else {
      res.status(404).send('No se encontró el cliente especificado');
    }
    res.status(201).json(nuevoProducto);
  });


 /* app.get('/products/:id', (req, res) => {
    const productId = parseInt(req.params.id);
    const product = products.find(p => p.id === productId);
  
    if (product) {
      res.status(200).send(product);
    } else {
      res.status(404).send('No se encontró el producto especificado');
    }
  });
  
  

app.get('/clientes', (req, res) => {
    res.status(200).send(clientes);
  });
  

app.use((req, res) => {
  res.status(404).send('Not Found');
});

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});


process.on('SIGINT', () => {
  mongoose.connection.close(() => {
    console.log('Conexión a la base de datos cerrada');
    process.exit(0);
  });
});

*/