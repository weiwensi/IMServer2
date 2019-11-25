import Vue from 'vue'
import vuex from 'vuex'
Vue.use(vuex);
export default new vuex.Store({
    state: {
        userInfo: {}
    },
    mutations: {
        initUser(state) {
            state.userInfo = {}
        },
        setUserInfo(state, data) {
            state.userInfo = data;
        }
    }
})