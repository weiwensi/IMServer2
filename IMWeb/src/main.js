import Vue from 'vue'
import App from './App'
import router from './router'
import iView from 'iview'
import 'iview/dist/styles/iview.css'
import GeminiScrollbar from 'vue-gemini-scrollbar'
import bubble from '../src/components/bubble.vue'
import mqttws from '../src/view/mqtt.vue'
import api from '../src/api'
import axios from '../src/api/axios'
import store from '../src/store/idnex'
import localStorage from '../src/utis/localStotage'
Vue.prototype.$ajax = axios
Vue.prototype.$api = api
Vue.use(localStorage);
Vue.component('bubble', bubble)
Vue.component('mqttws', mqttws)
Vue.use(GeminiScrollbar)
Vue.use(iView)
var vm = new Vue({
    el: '#app',
    router,
    store,
    components: { App },
    template: '<App/>'
})
Vue.use(vm)