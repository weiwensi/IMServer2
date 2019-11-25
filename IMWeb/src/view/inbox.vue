<template>
    <div>
        <Table :columns="tableKey" :data="tableData" ></Table>
        <!-- <ul>
            <li v-for="item in tableData" :key = "item.id">
                <div>
                    <p>{{item.content}}</p>
                    <p>{{item.remark}}</p>
                </div>
                <div>操作</div>
            </li>
        </ul> -->
    </div>
</template>

<script>
 export default {
        props:['groupId'],
        data () {
            return {
                tableKey: [
                    {
                        title:'消息',
                        key: 'content'
                    },
                    {
                        title:'时间',
                        key: 'sendTime'
                    },
                    {
                        title:'备注',
                        render:(h,params) => {
                            return h('span',params.row.source == this.$store.state.userInfo.userId ? '' : params.row.remark)
                        }
                    },
                     {
                        title:'状态',
                        render: (h, params) => {
                            if(params.row.status == 2){
                                 return h('div', [
                                    h('Button', {
                                        props: {
                                            type: 'success',
                                            size: 'small'
                                        },
                                        style: {
                                            marginRight: '5px'
                                        },
                                        on: {
                                            click: () => {
                                                let data = {
                                                    deal:'agree',
                                                    groupId:this.groupId,
                                                    inboxId:params.row.id
                                                }
                                                if(params.row.type == 0){
                                                     this.$ajax.put(this.$api.handleAddFriendBeg,data).then((result) => {
                                                        result && result.code === 200 ? params.row.status = 0 : '';
                                                        this.$emit('init');
                                                    });
                                                }else{
                                                    this.$ajax.post(this.$api.dealJoinBevyApply + 'agree/' + params.row.id).then((result) => {
                                                        result && result.code === 200 ? params.row.status = 0 : '';
                                                        this.$emit('init');
                                                    });
                                                }
                                               
                                            }
                                        }
                                    }, '同意'),
                                    h('Button', {
                                        props: {
                                            type: 'error',
                                            size: 'small'
                                        },
                                        on: {
                                            click: () => {
                                                 let data = {
                                                    deal:'refuse',
                                                    groupId:'',
                                                    inboxId:params.row.id
                                                }
                                                if(params.row.type == 0){
                                                      this.$ajax.put(this.$api.handleAddFriendBeg,data).then((result) => {
                                                        result && result.code === 200 ? params.row.status = 1 : '';
                                                        this.$emit('init');
                                                        });
                                                }else{
                                                    this.$ajax.post(this.$api.dealJoinBevyApply + 'refuse/' + params.row.id).then((result) => {
                                                        result && result.code === 200 ? params.row.status = 0 : '';
                                                        this.$emit('init');
                                                    });
                                                }
                                               
                                            }
                                        }
                                    }, '拒绝')
                                ]);
                            }else{
                                return h('div',params.row.status == 0 ? '已同意' : params.row.status == 1 ? '已拒绝' : '')
                            }
                        }
                    }
                ],
                page:1,
                pageSize:15,
                total:0,
                tableData: []
            }
        },
        mounted(){
            this.$ajax.get(this.$api.InboxInfos + this.page + '/' + this.pageSize + '?type=').then((result) => {
                this.total = result.result.total;
                this.tableData = result.result.list;
                console.log(this.tableData)
            });
        },
        methods:{

        }
    }
</script>

<style>

</style>
