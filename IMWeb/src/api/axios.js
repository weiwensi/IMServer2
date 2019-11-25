import axios from 'axios'
import { Message } from 'iview'
import router from '../router'
const Axios = axios.create({
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*'
    },
    baseURL: 'http://192.168.1.232:9000',
    withCredentials: true
})
Axios.interceptors.response.use(function(response) {
    // 对响应数据做点什么
    response.data.code == 302 ? router.push('/login') : ''
    response.data.code != 200 ? Message.error(response.data.msg) : ''

    return response.data
}, function(error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});
export default Axios