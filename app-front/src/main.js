import Vue from 'vue'
import './plugins/vuetify'
import './plugins/http'
import './plugins/toast'
// import './plugins/element.js'
// import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
