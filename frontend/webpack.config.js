const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');
require('babel-polyfill');

module.exports = {
    node: {
        fs: 'empty',
    },
    entry: {
        app: ['babel-polyfill', './src/index.js'],
    },
    output: {
        path: path.join(__dirname, '/generated'),
        filename: 'index_bundle.js',
        publicPath: '/',
    },
    devServer: {
        historyApiFallback: true,
        port: 3000,
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                },
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
        ],
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './src/index.html',
        }),
    ],
};
