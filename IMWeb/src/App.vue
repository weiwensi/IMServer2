<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  mounted(){
    this.$getLocalStorageByName('info') ? (_ => {
      this.$store.commit('setUserInfo',this.$getLocalStorageByName('info'));
      localStorage.removeItem('info')
    })() : '';
    window.addEventListener("beforeunload",()=>{
      this.$setLocalStorageByName('info',this.$store.state.userInfo)
    })
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
*{
  margin: 0;
  padding: 0;
}
body{
  /* background: url(../src/assets/img/bg.jpg); */
  background-repeat: no-repeat;
}
</style>
