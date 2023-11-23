const User = require("../models/user");

// check if the email exist in the database
const emailExist = async (email = "") => {
  const emailExist = await User.findOne({ email });

  if (emailExist) {
    throw new Error(`Email: ${email} Already exist in the database`);
  }
};

module.exports = {
  emailExist,
};
