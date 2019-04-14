var webpack = require('webpack');
var path = require('path');
require( "@babel/polyfill")

module.exports = {
    entry: ["@babel/polyfill", "./src/index.js"],
    output: {
        path: path.resolve(__dirname, '../../../target/classes/META-INF/resources'),
        filename: "bundle.js"
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_module/,
                include: [
                    path.resolve(__dirname, "./src")
                ],
                use: {
                    loader: 'babel-loader'
                }
            }
        ]
    }
};
