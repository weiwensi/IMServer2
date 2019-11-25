<template>
    <div class="login">
        <Card style="width:320px">
            <Form ref="formInline" :model="formInline" :rules="ruleInline" >
                <FormItem prop="user">
                    <Input type="text" v-model="formInline.user" placeholder="Username">
                        <Icon type="ios-person-outline" slot="prepend"></Icon>
                    </Input>
                </FormItem>
                <FormItem prop="password">
                    <Input type="password" v-model="formInline.password" placeholder="Password">
                        <Icon type="ios-lock-outline" slot="prepend"></Icon>
                    </Input>
                </FormItem>
                <FormItem>
                    <Button type="primary" long @click="handleSubmit('formInline')">登录</Button>
                </FormItem>
            </Form>
        </Card>
    </div>
</template>

<script>
import md5 from 'js-md5'
export default {
  data() {
    return {
      formInline: {
        user: "wangaoxu",
        password: "111111"
      },
      ruleInline: {
        user: [{ required: true, message: "请输入用户名", trigger: "blur" }],
        password: [
          { required: true, message: "请输入用户密码", trigger: "blur" }
        ]
      }
    };
  },
  mounted(){
      this.$store.commit('initUser');
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
        let data = {
            username:this.formInline.user,
            password:md5(this.formInline.password)
        }
          this.$ajax.post(this.$api.login,data).then((result) => {
              result && result.code === 200 ? (_=>{
                  let data = result.result;
                  data.user = this.formInline;
                  this.$store.commit('setUserInfo',result.result);
                  this.$setLocalStorageByName('test',result.result)
                  this.$router.push('/home');
              })() : ''
          });
        }
      });
    }
  }
};
</script>

<style>
.login {
  width: 350px;
  margin: 100px auto;
  text-align: center;
}
</style>
