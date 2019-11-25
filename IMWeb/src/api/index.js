export default {
    login: 'IMServer/login/login', // 登录
    logout: 'IMServer/login/logout', // 登出 
    queryFriends: 'IMServer/friend/queryFriends', // 查询用户好友
    getOfflineMsg: 'IMServer/chat/getOfflineMsg', // 获取离线消息
    getHistoryMsg: 'IMServer/chat/getHistoryMsg', // 获取历史消息
    sendMsg: 'IMServer/chat/sendMsg', // 发送消息 
    offlineInboxCount: 'IMServer/inbox/offlineInboxCount', // 查询是否有系统消息 
    InboxInfos: 'IMServer/inbox/pageQueryInboxInfos/', // 查询系统消息
    handleAddFriendBeg: 'IMServer/friend/handleAddFriendBeg', // 是否添加好友 
    searchFriends: 'IMServer/friend/searchFriends', // 搜索好友  
    pageSearchBevy: 'IMServer/bevy/pageSearchBevy/', // 搜索群   
    addFriend: 'IMServer/friend/addFriend', // 添加好友  
    applyJoinBevy: 'IMServer/bevy/applyJoinBevy', // 添加群 
    createBevy: 'IMServer/bevy/createBevy', // 创建群 
    listUserBevys: 'IMServer/bevy/listUserBevys', // 用户群列表 
    dealJoinBevyApply: 'IMServer/bevy/dealJoinBevyApply/', // 同意加群 
}