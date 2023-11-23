const mongoose = require("mongoose");

const clienteSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: [true, "name is required"],
    },
    password: {
      type: String,
      required: [true, "password is required"],
    },
    idpost: {
      type: String,
    },
    email: {
      type: String,
      required: [true, "email is required"],
    },
  },
  { timestamps: true }
);

clienteSchema.add({ id: mongoose.Types.ObjectId });

clienteSchema.methods.toJSON = function () {
  const { __v, password, _id, ...user } = this.toObject();
  user._id = _id;
  return user;
};

module.exports = mongoose.model('User', clienteSchema);
