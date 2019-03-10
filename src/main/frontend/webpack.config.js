var webpack = require('webpack');
var path = require('path');
require( "@babel/polyfill")
var ExtractTextPlugin = require('mini-css-extract-plugin');

module.exports = {
    entry: ["@babel/polyfill", "./src/index.js"],
    output: {
        path: path.resolve(__dirname, '../../../target/classes/public'),
        filename: "bundle.js"
    },
    module: {
        rules: [
            { test: /\.css$/, loader: "style-loader!css-loader" },
            {
                test: /\.jsx?$/,
                exclude: /node_module/,
                include: [
                    path.resolve(__dirname, "./src")
                ],
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]

                    }
                }
            }
        ]
    },
    plugins: [
        new ExtractTextPlugin("[name].css")
    ]
};
