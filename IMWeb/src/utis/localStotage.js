const local = {}
local.install = (Vue) => {
    Vue.prototype.$getLocalStorageByName = local.$getLocalStorageByName
    Vue.prototype.$setLocalStorageByName = local.$setLocalStorageByName
}
local.$getLocalStorageByName = (name) => {
    if (!localStorage || !localStorage.getItem) {
        throw Error('浏览器不支持localStorage')
    }
    let s = name ? localStorage.getItem(name) : localStorage
    try {
        return JSON.parse(s)
    } catch (e) {
        return s
    }
}
local.$setLocalStorageByName = (name, value) => {
    if (!localStorage || !localStorage.setItem) {
        throw Error('浏览器不支持localStorage')
    }
    localStorage.setItem(name, typeof value === 'object' ? JSON.stringify(value) : value)
}
export default local