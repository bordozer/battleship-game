const path = require("path");

module.exports = (env, options) => {
    return {
        entry: './src/app.js',
        output: {
            path: __dirname,
            filename: './build/bundle.js'
        },
        resolve: {
            alias: {
                src: path.resolve(__dirname, "src"),
                components: path.resolve(__dirname, "src", "components")
            }
        },
        module: {
            rules: [
                {
                    test: /\.(js|jsx)$/,
                    exclude: /node_modules/,
                    use: [
                        {
                            loader: "babel-loader",
                            options: {
                                presets: [
                                    "@babel/preset-env",
                                    "@babel/preset-react", {
                                        'plugins': ['@babel/plugin-proposal-class-properties']
                                    }
                                ],
                            }
                        },
                        {
                            loader: "eslint-loader"
                        }
                    ],
                },
                {
                    test: /\.html$/,
                    use: 'html-loader'
                },
                {
                    test: /\.css$/,
                    loader: "style-loader!css-loader"
                },
                {
                    test: /\.(jpg|png|gif)$/,
                    use: [{
                        loader: 'file-loader',
                        options: {
                            limit: 8000, // Convert images < 8kb to base64 strings
                            name: 'images/[hash]-[name].[ext]'
                        }
                    }]
                },
                {
                    test: /\.(eot|woff2?|svg|ttf)([\?]?.*)$/,
                    use: 'file-loader'
                },
                {
                    test: /favicon\.ico$/,
                    loader: 'url-loader',
                    query: {
                        limit: 1,
                        name: '[name].[ext]',
                    },
                }
            ]
        },
        optimization: {
            splitChunks: {
                cacheGroups: {
                    vendor: {
                        test: /node_modules/,
                        chunks: "initial",
                        name: "vendor",
                        enforce: true
                    }
                }
            }
        },
        performance: {
            hints: false
        },
        devServer: {
            contentBase: path.join(__dirname, 'dist'),
            compress: true,
            useLocalIp: false,
            port: 3100
        }
    }
};
