const path = require("path");

module.exports = {
  target: "node",
  entry: "./src/index.js",
  output: {
    path: path.resolve(__dirname, "dist"),
    filename: "bundle.js",
  },
  externalsPresets: { node: true },
  externals: [
    function ({ request }, callback) {
      // Treat any non-relative, non-absolute import as an external
      // (i.e. everything resolved from node_modules), so webpack
      // doesn't try to bundle server-only packages like express.
      if (/^[a-z@][a-z0-9\-_./]*$/i.test(request) && !request.startsWith(".")) {
        return callback(null, "commonjs " + request);
      }
      callback();
    },
  ],
  mode: "production",
};
