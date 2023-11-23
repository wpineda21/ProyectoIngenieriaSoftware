const jwt = require("jsonwebtoken");
require('dotenv').config();


// function that generates a JWT token
const generateJWT = (_id = "") => {
  return new Promise((resolve, reject) => {
    const payload = { _id };

    // signing the token
    jwt.sign(
      payload,
      process.env.JWT_SECRET,
      {
        expiresIn: "4h",
      },
      (err, token) => {
        if (err) {
          console.log(err);
          reject("Error generating JWT");
        } else {
          resolve(token);
        }
      }
    );
  });
};

module.exports = {
  generateJWT,
}