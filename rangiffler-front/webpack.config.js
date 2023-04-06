const path = require("path");
const HTMLWebpackPlugin = require("html-webpack-plugin");
const {CleanWebpackPlugin} = require("clean-webpack-plugin");
const CopyWebpackPlugin = require("copy-webpack-plugin");
const ReactRefreshWebpackPlugin = require(
    "@pmmmwh/react-refresh-webpack-plugin");
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");
const TerserWebpackPlugin = require("terser-webpack-plugin")
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const Dotenv = require('dotenv-webpack');

const IS_DEV = process.env.NODE_ENV === "development";
const IS_PROD = !IS_DEV;

const filename = (ext) => IS_DEV ? `[name].${ext}` : `[hash].${ext}`;

module.exports = {
  context: path.resolve(__dirname, "src"),
  entry: "./index.tsx",
  output: {
    filename: filename("js"),
    path: path.resolve(__dirname, "dist"),
    publicPath: "/",

  },
  resolve: {
    extensions: [".tsx", ".ts", ".jsx", ".js"],
    alias: {
      "@img": path.resolve(__dirname, "src/static/img"),
      "@fonts": path.resolve(__dirname, "src/static/fonts/"),
    },
  },
  devServer: {
    historyApiFallback: true,
    port: 3001,
    hot: true,
    static: __dirname + "/dist/",
  },
  devtool: "source-map",
  optimization: {
    minimize: IS_PROD,
    minimizer: [
      new TerserWebpackPlugin(),
      new CssMinimizerPlugin(),
    ],
  },
  plugins: [
    new Dotenv({
      path: `./.env.${process.env.NODE_ENV === "development" ? "dev" : process.env.NODE_ENV === "production" ? "docker" : "test"}`,
    }),
    new HTMLWebpackPlugin({
      template: "./index.html",
      minify: {
        collapseWhitespace: IS_PROD,
      }
    }),
    new MiniCssExtractPlugin({
      filename: filename("css"),
    }),
    new CleanWebpackPlugin(),
    new CopyWebpackPlugin({
      patterns: [{
        from: path.resolve(__dirname, "src/favicon.ico"),
        to: path.resolve(__dirname, "dist"),
      }]
    }),
    IS_DEV && new ReactRefreshWebpackPlugin(),
  ].filter(Boolean),
  module: {
    rules: [
      {
        test: /\.(scss|css)$/,
        use: [MiniCssExtractPlugin.loader, "css-loader", "sass-loader"],
        exclude: /node_modules/,
      },
      {
        test: /\.(?:ico|gif|png|jpg|jpeg)$/i,
        type: 'asset/resource',
      },
      {
        test: /\.(woff(2)?|eot|ttf|otf|svg|)$/,
        type: 'asset/inline',
      },
      {
        test: /\.(js|ts)x?$/,
        exclude: /node-modules/,
        use: [
          {
            loader: 'babel-loader',
            options: {
              plugins: [IS_DEV && require.resolve(
                  'react-refresh/babel')].filter(Boolean),
            },
          },
        ],
      }
    ]
  }
}
