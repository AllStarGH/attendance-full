// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import Router from 'vue-router'
import Axios from 'axios'
import qs from 'qs'

import 'font-awesome/scss/font-awesome.scss'
import 'element-ui/lib/theme-chalk/index.css'
import '@/styles/index.scss'

Vue.prototype.$qs = qs
Vue.prototype.HOST = "/"
Vue.prototype.$axios = Axios
// 设置基本默认路径
Axios.defaults.baseURL = '/'
// 设置post请求的内容类型,不发送POST请求可不用设置
Axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(Router)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {
    App
  },
  // template: '<App/>'
  render: h => h(App)
})
