import Vue from 'vue'
import App from './App.vue'
import router from './router'
import { Button, Cell, Picker } from 'vant'

Vue.use(Button)
Vue.use(Cell)
Vue.use(Picker)

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
