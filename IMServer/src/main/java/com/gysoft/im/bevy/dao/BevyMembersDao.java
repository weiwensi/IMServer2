package com.gysoft.im.bevy.dao;


import com.gysoft.im.bevy.pojo.BevyMembers;
import com.gysoft.utils.jdbc.dao.EntityDao;

import java.util.List;
import java.util.Map;

/**
 * @author 周宁
 * @Date 2018-08-08 19:12
 */
public interface BevyMembersDao extends EntityDao<BevyMembers,String> {

    /**
     * 查询群成员数量
     * @author 周宁
     * @param
     * @throws
     * @version 1.0
     * @return Map<String,Integer>
     */
    Map<String,Integer> queryBevyMembersCountMap(List<String> bevyIds)throws Exception;

    /**
     * 查询用户加入的群组id
     * @author 周宁
     * @param userId
     * @throws Exception
     * @version 1.0
     * @return List<String>
     */

    List<String> queryUserJoinedBevyIds(String userId)throws Exception;
}
