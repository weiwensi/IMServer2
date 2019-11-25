<template>
  <div id="mqttws">
  </div>
</template>
<style>

</style>
<script>
  export default {
    name: 'mqttws',
    data() {
      return {
        hostname: '192.168.1.232',
        port: 8083,
        clientId: '',
        timeout: 5,
        userName: this.$store.state.userInfo.user.user,
        password: this.$store.state.userInfo.user.password,
        topic: this.$store.state.userInfo.userName,
        client:{},
        options: {},
      }
    },
   mounted : function (){
     this.linkMqtt();
   },
    methods: {
      linkMqtt(){
        this.client = new Paho.MQTT.Client('192.168.1.232',Number("8083"),'');//创建客户端实例
        this.client.onConnectionLost = this.onConnectionLost;//设置回调信息
        this.client.onMessageArrived = this.onMessageArrived;//设置回调信息
        this.client.connect({onSuccess: this.onConnect,userName:this.userName,password:this.password});
      },
      onConnectionLost: function (responseObject) {
        if (responseObject.errorCode !== 0) {
          this.Message.error(responseObject.errorMessage);
        }
      },
      onMessageArrived: function (message) {
        this.$emit('getMessage',message)
      },
      onConnect : function() {
        this.client.subscribe(this.topic,{
          onSuccess:() => {
            console.log('连接服务器成功')
          },
          onFailure:() => {
            console.log('连接服务器成功')
          }
        });//订阅主题
      },
    }
  }
</script>