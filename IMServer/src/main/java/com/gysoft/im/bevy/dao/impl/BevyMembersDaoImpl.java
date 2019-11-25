package com.gysoft.im.bevy.dao.impl;

import com.gysoft.im.bevy.dao.BevyMembersDao;
import com.gysoft.im.bevy.pojo.BevyMembers;
import com.gysoft.utils.jdbc.dao.GuYingJdbc;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import com.gysoft.utils.jdbc.dao.impl.GuyingJdbcTemplate;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 周宁
 * @Date 2018-08-08 19:16
 */
@Repository
public class BevyMembersDaoImpl extends EntityDaoImpl<BevyMembers, String> implements BevyMembersDao {

    @Resource
    private GuyingJdbcTemplate guyingJdbcTemplate;

    @Override
    public Map<String, Integer> queryBevyMembersCountMap(List<String> bevyIds) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bevyId,count(1) as memberCount FROM tb_bevymembers ");
        sql.append("WHERE bevyId in(?) ");
        sql.append("GROUP BY bevyId ");
        return guyingJdbcTemplate.query(sql.toString(), new Object[]{bevyIds}, rs -> {
            Map<String, Integer> result = new HashMap<>();
            while (rs.next()) {
                result.put(rs.getString("bevyId"), rs.getInt("memberCount"));
            }
            return result;
        });
    }

    @Override
    public List<String> queryUserJoinedBevyIds(String userId) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT(bevyId) as bevyId FROM tb_bevymembers ");
        sql.append("WHERE userId = ? ");
        return guyingJdbcTemplate.query(sql.toString(), new Object[] {userId},rs->{
        	List<String> result = new ArrayList<>();
        	while(rs.next()) {
        		result.add(rs.getString(1));
        	}
        	return result;
        });
    }
    
}
