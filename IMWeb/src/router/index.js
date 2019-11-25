import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
export default new Router({
    routes: [{
            path: '/',
            redirect: '/login'
        },
        {
            path: '/home',
            name: '首页',
            component: (resolve) => require(['@/view/Home.vue'], resolve)
        },
        {
            path: '/login',
            name: '登录',
            component: (resolve) => require(['@/view/login.vue'], resolve)
        }
    ]
})