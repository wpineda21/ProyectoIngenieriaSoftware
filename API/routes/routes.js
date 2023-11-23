const { Router } = require("express")
const bcrypt = require('bcrypt');
const { body, validationResult} = require('express-validator');
const router = Router()
const Product = require("../models/product")
const User = require("../models/user")
const jwt = require("jsonwebtoken");
const { generateJWT } = require("../helpers/generate-jwt");
const nodemailer = require('nodemailer');
const { emailExist } = require("../helpers/db_validators")
const Token = require("../models/tokens")
const Categoria = require('../models/category');
const generateVerificationCode = () => {
  const code = Math.floor(1000 + Math.random() * 9000);
  return code.toString();
};

// Configura el transporte de Nodemailer
const transporter = nodemailer.createTransport({
  service: 'Gmail',
  auth: {
    user: 'sybmarketplace@gmail.com',
    pass: 'ipphardrbiaxnhtp',
  },
});

// Función para enviar el correo electrónico de recuperación de contraseña
const sendPasswordResetEmail = (email, code) => {
  const mailOptions = {
    from: 'sybmarketplace@gmail.com',
    to: email,
    subject: 'Recuperación de contraseña',
    text: `Tu código de verificación es: ${code}`,
  };

  return transporter.sendMail(mailOptions);
};

const emailVerificationCodes = {};

router.post('/auth/register', [
  body('email')
    .isEmail().withMessage('El correo electrónico no es válido')
    .custom(async (email) => {
      await emailExist(email);
    }).withMessage('Email already exist in the database'),
], async (req, res) => {
  try {
    const { name, password, email } = req.body;

    const cliente = new User({ name, password, email });

    const salt = bcrypt.genSaltSync();
    cliente.password = bcrypt.hashSync(password, salt);

    await cliente.save();

    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      const errorMessages = errors.array().map(error => error.msg);
      return res.status(400).json({ msg: errorMessages });
    }

    const transporter = nodemailer.createTransport({
      service: 'Gmail',
      auth: {
        user: 'sybmarketplace@gmail.com',
        pass: 'ipphardrbiaxnhtp',
      },
    });
    //pjtixaqiaptobhqc

    const mailOptions = {
      from: 'sybmarketplace@gmail.com',
      to: email,
      subject: 'Registro exitoso',
      text: `¡Bienvenido, ${cliente.name}! Gracias por registrarte en nuestro sitio.`,
    };


    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.log(error);
        res.status(500).send('Error al enviar el correo electrónico');
      } else {
        console.log('Correo electrónico enviado: ' + info.response);
        res.status(201).json({ message: 'Usuario creado', cliente });
      }
    });
  } catch (error) {
    res.status(500).send('Error al agregar el cliente');
  }
});

router.post('/auth/login', async (req, res) => {
  try {
    const { email, password } = req.body;

    // Buscar el cliente por su correo electrónico en la base de datos
    const cliente = await User.findOne({ email });

    // Si no se encuentra el cliente, enviar una respuesta de error
    if (!cliente) {
      return res.status(404).json({ message: 'Cliente no encontrado' });
    }

    // Comparar la contraseña ingresada con la contraseña almacenada
    const isPasswordValid = bcrypt.compareSync(password, cliente.password);

    // Si las contraseñas no coinciden, enviar una respuesta de error
    if (!isPasswordValid) {
      return res.status(401).json({ message: 'Usuario o contraseña incorrecta' });
    }

    // La contraseña es válida, iniciar sesión exitosamente
    // Generar y enviar un token JWT
    const token = await generateJWT(cliente._id);

    // Crear una instancia del modelo Token
    const tokenInstance = new Token({
      token: token,
      userId: cliente._id,
    });

    // Guardar la instancia en la base de datos
    await tokenInstance.save();

    // Actualizar el campo isWhitelisted a true
    await Token.updateOne({ token: token }, { isWhitelisted: true });

    res.status(200).json({ message: 'Inicio de sesión exitoso', token });
  } catch (error) {
    res.status(500).send('Error en el servidor');
  }
});

router.post('/forgot-password', async (req, res) => {
  const { email } = req.body;

  try {
    // Verificar si el correo electrónico existe en la base de datos
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(404).json({ message: 'Correo electrónico no encontrado' });
    }

    // Generar un código de verificación único
    const verificationCode = generateVerificationCode();

    // Guardar el código de verificación en la base de datos para el usuario
    emailVerificationCodes[email] = verificationCode;

    // Enviar el correo electrónico de recuperación de contraseña
    await sendPasswordResetEmail(email, verificationCode);

    res.status(200).json({ message: 'Se ha enviado un correo electrónico de recuperación de contraseña' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Error al enviar el correo electrónico de recuperación de contraseña' });
  }
});

router.post('/restore-password', async (req, res) => {
  try {
    const { email, code, newPassword } = req.body;

    // Obtener el código de verificación almacenado para el correo electrónico
    const storedCode = emailVerificationCodes[email];

    if (!storedCode) {
      return res.status(404).json({ message: 'Código de verificación no encontrado' });
    }

    // Verificar si el código ingresado por el usuario coincide con el código almacenado
    if (code !== storedCode) {
      return res.status(400).json({ message: 'Código de verificación incorrecto' });
    }

    // Eliminar el código de verificación del objeto emailVerificationCodes
    delete emailVerificationCodes[email];

    // Actualizar la contraseña del usuario con la nueva contraseña
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(404).json({ message: 'Usuario no encontrado' });
    }

    const salt = bcrypt.genSaltSync();
    user.password = bcrypt.hashSync(newPassword, salt);
    await user.save();

    // Enviar una respuesta de éxito si la contraseña se actualiza correctamente
    res.status(200).json({ message: 'Contraseña actualizada exitosamente' });
  } catch (error) {
    res.status(500).send('Error al verificar el código de verificación');
  }
});

router.get('/allProducts', async (req, res) => {

  try {
    const products = await Product.find({});
    res.status(200).json(products);
  } catch (error) {
    res.status(500).send('Error al obtener los productos');
  }
});

router.get('/searchProducts/:id', async (req, res) => {
  try {
    const productId = req.params.id;

    const product = await product.findById(productId);

    if (!product) {
      return res.status(404).json({ message: 'Producto no encontrado' });
    }

    res.status(200).json(product);
  } catch (error) {
    res.status(500).send('Error al obtener el producto');
  }
});

router.post('/products/add', async (req, res) => {
  console.log(process.env.JWT_SECRET)
  const token = req.headers.authorization.split(' ')[1];
  const { titulo, descripcion, precio, numerodecontacto, categoriaNombre } = req.body;

  try {
    // Verificar y decodificar el token
    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Obtener el ID del usuario del token decodificado
    const userId = decoded._id;

    // Imprimir el ID y el token recibidos en la consola
    console.log('ID del usuario:', userId);
    console.log('Token recibido:', token);

    // Verificar si la categoría existe
    const categoria = await Categoria.findOne({ nombre: categoriaNombre });
    if (!categoria) {
      return res.status(404).json({ message: 'Categoría no encontrada' });
    }

    // Crear una instancia del modelo Product
    const product = new Product({
      titulo,
      descripcion,
      precio,
      numerodecontacto,
      categoria: categoria._id,
      idcliente: userId,
    });

    // Guardar el producto en la base de datos
    await product.save();

    res.status(201).json({ message: 'Producto creado exitosamente', product });
  } catch (error) {
    console.error('Error al agregar el producto:', error);
    res.status(500).json({ message: 'Error al agregar el producto' });
  }
});

router.post('/categoria/add', async (req, res) => {
  try {
    const { nombre } = req.body;

    // Crear una nueva categoría
    const nuevaCategoria = new Categoria({
      nombre,
    });

    // Guardar la nueva categoría en la base de datos
    await nuevaCategoria.save();

    res.status(201).json({ message: 'Categoría agregada exitosamente', categoria: nuevaCategoria });
  } catch (error) {
    res.status(500).send('Error al agregar la categoría');
  }
});

router.get('/allCategorias', async (req, res) => {
  const token = req.headers.authorization.split(' ')[1];
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Obtener el ID del usuario del token decodificado
    const userId = decoded._id;

    const categorias = await Categoria.find();
    res.status(200).json(categorias);
  } catch (error) {
    res.status(500).send('Error al obtener las categorías');
  }
});

router.get('/searchCategorias/:nombreCategoria', async (req, res) => {
  console.log(process.env.JWT_SECRET)
  const token = req.headers.authorization.split(' ')[1];
  try {

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Obtener el ID del usuario del token decodificado
    const userId = decoded._id;

    const nombreCategoria = req.params.nombreCategoria;

    // Buscar la categoría por su nombre
    const categoria = await Categoria.findOne({ nombre: nombreCategoria });
    if (!categoria) {
      return res.status(404).json({ message: 'Categoría no encontrada' });
    }

    // Buscar los productos que pertenecen a la categoría
    const productos = await Product.find({ categoria: categoria._id });

    res.status(200).json({ categoria, productos });
  } catch (error) {
    res.status(500).send('Error al obtener la categoría y los productos');
  }
});

router.get('/cliente/productos', async (req, res) => {
  const token = req.headers.authorization.split(' ')[1];
  try {

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Obtener el ID del usuario del token decodificado
    const idcliente = decoded._id;

    // Buscar los productos del cliente por su ID
    const productos = await Product.find({ idcliente });

    res.status(200).json(productos);
  } catch (error) {
    res.status(500).send('Error al obtener los productos');
  }
});

router.get('/allClientes', async (req, res) => {
  try {
    const clientes = await User.find({});
    res.status(200).json(clientes);
  } catch (msg) {
    res.status(500).send('Error al obtener los clientes');
  }
});

router.post('/logout', async (req, res) => {
  const token = req.headers.authorization.split(' ')[1];

  try {
    // Verificar y decodificar el token
    console.log('Token recibido:', token);

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Verificar si el token ha expirado
    const now = Date.now() / 1000; // Obtener la fecha y hora actual en segundos
    if (decoded.exp < now) {
      // El token ha expirado
      res.status(401).json({ message: 'El token ha expirado' });
      return;
    }

    // Actualizar el token en el esquema Token
    await Token.updateOne(
      { token: token },
      { isWhitelisted: false, isBlacklisted: true }
    );

    // Verificar si el token está en la lista negra
    const tokenInstance = await Token.findOne({ token: token });
    if (tokenInstance.isBlacklisted) {
      // El token está en la lista negra, la sesión ya ha expirado
      res.status(401).json({ message: 'Logout exitoso, sesión ya ha expirado' });
      return;
    }

  } catch (error) {
    // Imprimir el error para depurar
    console.error('Error al verificar el token:', error);

    // En caso de que ocurra un error al verificar el token, enviar una respuesta de error
    res.status(401).json({ message: 'Error al realizar el logout' });
  }
});

router.delete('/delete/productos/cliente/:id', async (req, res) => {
  const token = req.headers.authorization.split(' ')[1];
  const productId = req.params.id;

  try {

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Obtener el ID del usuario del token decodificado
    const idcliente = decoded._id;
    // Buscar el producto por su ID
    const product = await Product.findById(productId);

    // Verificar si el producto existe
    if (!product) {
      return res.status(404).json({ message: 'Producto no encontrado' });
    }

    // Realizar la eliminación del producto
    await product.deleteOne();

    res.status(200).json({ message: 'Producto eliminado correctamente' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Error al eliminar el producto' });
  }
});

router.delete('/delete/usuarios/:id', async (req, res) => {
  const token = req.headers.authorization.split(' ')[1];
  const userId = req.params.id;

  try {

    const decoded = jwt.verify(token, process.env.JWT_SECRET);

    // Obtener el ID del usuario del token decodificado
    const idcliente = decoded._id;

    // Buscar el usuario por su ID
    const user = await User.findById(userId);

    // Verificar si el usuario existe
    if (!user) {
      return res.status(404).json({ message: 'Usuario no encontrado' });
    }

    // Realizar la eliminación del usuario
    await user.deleteOne();

    res.status(200).json({ message: 'Usuario eliminado correctamente' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Error al eliminar el usuario' });
  }
});

module.exports = router;
