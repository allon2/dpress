const path = require('path')
const webpack = require('webpack')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const isProd = process.env.NODE_ENV === 'production'

const assetsCDN = {
  externals: {
    vue: 'Vue',
    'vue-router': 'VueRouter',
    vuex: 'Vuex',
    axios: 'axios',
    marked: 'marked'
  },
  css: [],
  js: [
    'https://cdn.jsdelivr.net/npm/vue@2.6.11/dist/vue.min.js',
    'https://cdn.jsdelivr.net/npm/vue-router@3.1.3/dist/vue-router.min.js',
    'https://cdn.jsdelivr.net/npm/vuex@3.1.1/dist/vuex.min.js',
    'https://cdn.jsdelivr.net/npm/axios@0.19.0/dist/axios.min.js',
    'https://cdn.jsdelivr.net/npm/marked@0.8.0/marked.min.js'
  ]
}

// vue.config.js
module.exports = {
  // outputDir: 'build/resources/main',

  publicPath: process.env.PUBLIC_PATH,
  configureWebpack: {
    plugins: [
      new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/)
    ],
    externals: isProd ? assetsCDN.externals : {}
  },
  devServer: {
    // contentBase: './',
    proxy: {
      // 当你请求是以/api开头的时候，则我帮你代理访问到http://localhost:3000
      // 例如：
      // /api/users  http://localhost:3000/api/users
      // 我们真是服务器接口是没有/api的
      '/upload': {
        target: 'http://localhost:8090'
        // pathRewrite:{"^/api":""}
      },
      '/themes': {
        target: 'http://localhost:8090'
        // pathRewrite:{"^/api":""}
      }
    }
  },
  chainWebpack: (config) => {
    config.resolve.alias
      .set('@$', resolve('src'))
      .set('@api', resolve('src/api'))
      .set('@assets', resolve('src/assets'))
      .set('@comp', resolve('src/components'))
      .set('@views', resolve('src/views'))
      .set('@layout', resolve('src/layout'))
      .set('@static', resolve('src/static'))

    const svgRule = config.module.rule('svg')
    svgRule.uses.clear()
    svgRule
      .oneOf('inline')
      .resourceQuery(/inline/)
      .use('vue-svg-icon-loader')
      .loader('vue-svg-icon-loader')
      .end()
      .end()
      .oneOf('external')
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'assets/[name].[hash:8].[ext]'
      })
    if (isProd) {
      config.plugin('html').tap(args => {
        args[0].cdn = assetsCDN
        return args
      })
    }
  },

  css: {
    loaderOptions: {
      less: {
        javascriptEnabled: true
      }
    }
  },

  lintOnSave: undefined,
  // babel-loader no-ignore node_modules/*
  transpileDependencies: [],
  productionSourceMap: false
}
