<template>
  <div class="main_home" id="home" @mousedown="mousedownHome">
    <Layout>
      <Header>
        <div class="header">
          <div class="user_info"><img src="../assets/img/user/i.jpg" alt=""></div>
          <div class="header_tabs">
            <Icon type="md-chatboxes" />
            <Icon type="md-person" />
          </div>
          <div>
            <Button  type="dashed" ghost @click="out">退出</Button>
          </div>
        </div>
      </Header>
      <Layout>
        <Sider hide-trigger style="width:250px;min-width: 0px;max-width: 250px;flex: none">
          <Tree :data="groups" ref="tree" @on-select-change="click" children-key="userBriefInfos"></Tree>
          <div class="fn">
              <ButtonGroup size="large">
                 <Poptip  placement="top" v-model="visible">
                   <Badge :count="unreadMessage.length" :offset="[15,20]">
                    <Button ghost type="text" icon="md-notifications-outline"></Button>
                    </Badge>
                  <div slot="content">
                      <ul v-if="unreadMessage.length > 0">
                        <li v-for="(item,index) in unreadMessage" :key="index" @click="clickMessage(item,index)" style="cursor: pointer;">
                          <Badge :count="item.count" :offset="[13,7]">
                            <Avatar icon="ios-person" size="small" /> {{item.destinationName || item.sourceName + " : " + item.content}}
                           </Badge>
                        </li>
                      </ul>
                      <div v-else>暂无消息</div>
                  </div>
              </Poptip >
              <Badge dot :offset="[15,20]" :count="inBoxCont" title="系统消息" >
                <Button ghost type="text" icon="md-volume-mute" @click="isInBoxShow"></Button>
              </Badge>
                <Button ghost type="text" icon="ios-settings-outline" title="设置" @click="createBevy"></Button>
                <Button ghost type="text" @click="addUser = true" title="新增群/新增联系人" icon="ios-add-circle-outline"></Button>
              </ButtonGroup>
          </div>
        </Sider>
        <Modal
          v-model="addUser"
          @on-cancel="addUser = false">
          <Tabs value="0" @on-click="tabsClick">
              <TabPane label="查找好友" name="0"></TabPane>
              <TabPane label="查找群" name="1"></TabPane>
          </Tabs>
              <Input search enter-button placeholder="..." @on-search="searchO" v-model="getMan.searchKey"/>
          <ul>
            <li v-for="(item,index) in manS" :key="index">
              {{item.nickName || item.bevyName}}
              <Button type="text" @click="addMan(item)">{{opType ? '加入群' : '加为好友'}}</Button>
            </li>
          </ul>
         <div slot="footer"></div>
      </Modal>
        <Content>
          <div v-if="!isInBox">
              <div class="content">
                <div v-if="!getMessage.requestId" style="height:100%;text-align: center;">暂未选择联系人</div>
                <Scroll v-else :on-reach-top="handleReachTop" height="420" :distance-to-edge="[0,0]">
                    <bubble  v-for="(item,index) in messageBox" :key="index" :r="item.from == $store.state.userInfo.userId ? true : false">{{item.content}}</bubble>
                </Scroll>
            </div>
            <div class="_input">
              <Input v-model="news" type="textarea" :rows="7" placeholder="Enter something..." />
              <Button type="default" ghost size="default" style="float:right;margin-right:10px;" @click="sendMsg">提交</Button>
            </div>
          </div>
          <inBox v-else :groupId="groups[0].groupId" @init="init"></inBox>
        </Content>
      </Layout>
    </Layout>
    <mqttws @getMessage="mqttMessage"></mqttws>
    <div id="r_menu" v-show="isRmenu">
       <Card  :padding="0"  style="width: 200px;">
            <CellGroup>
                <Cell title="修改分组名称" />
                <Cell title="删除分组" />
            </CellGroup>
        </Card>
    </div>
    <!-- 创建群 -->
    <Modal
        v-model="bevy"
        title="创建群"
        @on-ok="ok">
       <Form :model="newBevy" label-position="left" :label-width="100">
          <FormItem label="群名称">
              <Input v-model="newBevy.bevyName"></Input>
          </FormItem>
          <FormItem label="群信息">
              <Input v-model="newBevy.remark"></Input>
          </FormItem>
          <FormItem label="最大群成员数量">
              <Input v-model="newBevy.upperLimit"></Input>
          </FormItem>
    </Form>
    </Modal>
  </div>
</template>
<script>
import "../assets/js/mqttws31.js";
import md5 from "js-md5";
import inBox from './inbox'
export default {
  components:{
    inBox
  },
  data() {
    return {
      bevy:false,
      newBevy:{
        bevyName:'',
        icon:'',
        remark:'',
        upperLimit:100
      },
      addUser:false,
      groups: [],
      manS:[],
      getMan:{
        page: {
          currentPage: 1,
          pageSize: 10
        },
        searchKey:'',
      },
      news: "",
      isInBox:false,
      inBoxCont:0,
      isUPdateTree: true,
      isExpand: "",
      visible: false,
      messageBox: [],
      isRmenu: false,
      opType:0,
      unreadMessage: [],
      getMessage: {
        page: {
          currentPage: 1,
          pageSize: 8
        },
        requestId: 0
      }
    };
  },
  mounted() {
    document.getElementById("home").oncontextmenu = function(e) {
      //禁止浏览器右击事件
      return false;
    };
    this.getGroups();
    this.setScroll();
    this.getOfflineMsg();
    this.offlineInboxCount();
  },
  methods: {
    listUserBevys(){//获取群成员列表
        let data = {
          title:'我的群聊',
          isParent:true,
          userBriefInfos:[]
        }
        this.$ajax.get(this.$api.listUserBevys).then((result) => {
        result.result.forEach(element => {
          element.title = element.bevyName;
          element.isBevy = true;
          data.userBriefInfos.push(element)
        });
        this.groups.push(data)
      });
    },
    ok(){//创建群
       this.$ajax.put(this.$api.createBevy,this.newBevy).then((result) => {
        console.log(result)
      });
    },
    createBevy(){
      this.bevy = true;
    },//添加群/人
    addMan(item){
      if(this.opType){
        let data = {
            devyId: item.id,
            remark: "快,拉我进群"
        }
        this.$ajax.put(this.$api.applyJoinBevy,data).then((result) => {
          console.log(result);
        });
      }else{
        let data = {
            begFriendId: item.id,
            groupId: this.groups[0].groupId,
            remark: "我想加你为好友"
        }
        this.$ajax.post(this.$api.addFriend,this.getMan,data).then((result) => {
          console.log(result);
        });
      }
    },
    tabsClick(num){
      this.opType = num;
    },
    searchO(){//搜索
      if(this.opType){
        this.$ajax.get(this.$api.pageSearchBevy + this.getMan.page.pageSize + '/' + this.getMan.page.currentPage + '?bevyName=' +this.getMan.searchKey).then((result) => {
          console.log(result);
        });
      }else{
        this.$ajax.post(this.$api.searchFriends,this.getMan).then((result) => {
          console.log(result);
        });
      }
        
    },
    init(){
      this.getGroups();
    },
    offlineInboxCount(){//获取系统消息条数
      this.$ajax.get(this.$api.offlineInboxCount).then((result) => {
        this.inBoxCont = result.result;
      });
    },
    isInBoxShow(){//是否显示系统消息
      this.isInBox = !this.isInBox;
      this.inBoxCont = 0;
    },
    mqttMessage(message) {
      //接收用户消息
      console.log(message)
      let msg = JSON.parse(message.payloadString);
      if(msg.msgType == 'INBOX'){
        this.inBoxCont = 1;
      }else{
        msg.destinationName = message.destinationName;
        msg.count = 0;
        console.log(msg)
        msg.from == this.getMessage.requestId
          ? (_ => {
              this.messageBox.push(msg);
              this.setScroll();
            })()
          : (_ => {
              if (this.unreadMessage.length > 0) {
                this.unreadMessage.forEach(element => {
                  console.log(element.to == msg.to)
                  if (element.to == msg.to) {
                    element.content = msg.content;
                    element.count = element.count += 1;
                  }
                });
              } else {
                this.unreadMessage.push(msg);
              }
            })();
      }
    },
    out() {
      this.$ajax.get(this.$api.logout).then(result => {
        result && result.code === 200 ? this.$router.push("/login") : "";
      });
    },
    setScroll() {
      this.$nextTick(() => {
        var scl = document.getElementsByClassName("ivu-scroll-container")[0];
        scl ? (scl.scrollTop = scl.scrollHeight) : "";
      });
    },
    getOfflineMsg() {
      //获取离线消息
      this.$ajax.get(this.$api.getOfflineMsg).then(result => {
        result.result.forEach(element => {
          element.msg.count = element.count;
          element.msg.sourceName = element.sourceName;
          this.unreadMessage.push(element.msg);
        });
        console.log(this.unreadMessage);
      });
    },
    sendMsg() {
      let data = {
        to: this.getMessage.requestId,
        content: this.news
      };
      this.$ajax.post(this.$api.sendMsg, data).then(result => {
        let msg = {
          from: this.$store.state.userInfo.userId,
          content: this.news,
          to: this.getMessage.requestId
        };
        this.messageBox.push(msg);
        this.news = "";
        this.setScroll();
      });
    },
    async getGroups() {
      //获取用户好友分组关系
      let result = await this.$ajax.get(this.$api.queryFriends)
        result && result.code === 200
          ? (_ => {
              result.result.forEach(element => {
                element.expand = false;
                element.isParent = true;
                element.title = element.groupName;
                element.userBriefInfos
                  ? (_ => {
                      element.userBriefInfos.forEach(v => {
                        v.title = v.nickName;
                        v.selected = false;
                      });
                    })()
                  : "";
              });
              this.groups = result.result;
            })()
          : "";
      this.listUserBevys()
    },
    message() {//获取离线消息
      this.$ajax.post(this.$api.getHistoryMsg, this.getMessage).then(result => {
        result.result.list.forEach(element => {
          this.messageBox.unshift(element);
        });
      });
    },
    clickMessage(item, index) {
      //点击消息
      this.isInBox = false;
      this.groups.forEach(element => {
        element.userBriefInfos
          ? (_ => {
              element.userBriefInfos.forEach(e => {
                item.from == e.id
                  ? (_ => {
                      e.selected = true;
                      element.expand = true;
                      console.log(e)
                      this.click([e]);
                    })()
                  : (e.selected = false);
              });
            })()
          : "";
      });
      this.visible = false;
      this.unreadMessage.splice(index, 1);
    },
    click(data, node, root) {
      this.isInBox = false;
      if (data[0] && data[0].isParent) {
        this.isExpand = data[0];
        data[0].selected = false;
        data[0].expand = !data[0].expand;
      } else if (data[0]) {
        this.isExpand = data[0];
        this.$set(data[0], "selected", true);
        this.getMessage.page.currentPage = 1;
        this.getMessage.requestId = data[0].id;
        this.messageBox = [];
        this.message();
      } else {
        this.groups.forEach(e => {
          e.groupId == this.isExpand.groupId ? (e.expand = !e.expand) : "";
        });
      }
    },
    handleReachTop() {
      //滚动顶部加载更多
      this.getMessage.page.currentPage += 1;
      return new Promise(res => {
        this.message();
        res();
      });
    },
    mousedownHome() {
      //控制菜单弹框is显示
      for (var index = 0; index < event.path.length; index++) {
        if (event.path[index].className == "btn" && event.button == 2) {
          this.isRmenu = true;
          break;
        } else {
          this.isRmenu = false;
        }
      }
    },
    mousedown1(data) {
      //定位菜单框位置
      event.button == 2
        ? (_ => {
            var menu = document.getElementById("r_menu");
            this.isRmenu = true;
            menu.style.top = event.pageY + "px";
            menu.style.left = event.pageX + 20 + "px";
          })()
        : "";
    }
  }
};
</script>
<style>
@import url(../../src/assets/css/home.css);
.red {
  background: red;
}
.black12 {
  background: blue;
}
#r_menu {
  position: absolute;
}
.ivu-card-head {
  display: none;
}
.ivu-tree ul {
  font-size: 16px;
}
.ivu-tree-arrow {
  display: none;
}
.ivu-tree-title {
  color: #fff;
}
.ivu-tree-title-selected,
.ivu-tree-title-selected:hover,
.ivu-tree-title:hover {
  background: rgba(0, 0, 0, 0.2);
}
.fn {
  width: 100%;
  text-align: center;
  position: absolute;
  bottom: 2px;
  font-size: 20px;
}
.ivu-badge-count {
  width: 15px;
  padding: 0;
  min-width: 15px;
  height: 15px;
  border-radius: 50%;
  font-size: 8px;
  line-height: 13px;
}
.ivu-btn-group-large .ivu-btn-icon-only .ivu-icon {
  font-size: 24px;
}
ul li {
  list-style-type: none;
}
.ivu-poptip-inner {
  color: black;
}
.ivu-badge {
  max-width: 150px;
  /* padding-right: 10px; */
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.ivu-tabs{
  padding-bottom: 5px;
}
.ivu-modal-wrap .ivu-input{
  color: black;
  border: 1px solid #dcdee2;
}
</style>
