package com.gysoft.im.group.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gysoft.im.common.core.session.ImSession;
import com.gysoft.im.group.bean.GroupInfo;
import com.gysoft.im.group.bean.param.GroupMoveParam;
import com.gysoft.im.group.bean.param.GroupUpdateParam;
import com.gysoft.im.group.service.GroupService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * @Description：分组
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@CrossOrigin
@RestController
@RequestMapping("/group")
@Api(value="分组",tags="分组")
public class GroupController extends ImSession{

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
	@Autowired
	private GroupService groupService;
	
	@ApiOperation(value="新增分组信息",notes="新增分组信息",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String addGroupInfo(@RequestBody String groupName) throws Exception{
		try {
			return groupService.addGroupInfo(groupName,getUserId());
		}catch(Exception e) {
			logger.error("addGroupInfo error,groupName={},userId={}",groupName,getUserId(),e);
			throw e;
		}
	}
	@ApiOperation(value="修改分组信息",notes="修改分组信息",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public void updateGroupInfo(@RequestBody GroupUpdateParam groupUpdateInfo) throws Exception{
		try {
			groupService.updateGroupInfo(groupUpdateInfo);
		}catch(Exception e) {
			logger.error("updateGroupInfo error,groupUpdateInfoe={}",groupUpdateInfo,e);
			throw e;
		}
	}
	@ApiOperation(value="移动分组",notes="移动分组",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/move",method=RequestMethod.PUT)
	public void moveGroup(@RequestBody GroupMoveParam groupMoveParam) throws Exception{
		try {
			groupService.move(groupMoveParam);
		}catch(Exception e) {
			logger.error("moveGroup error,groupMoveParam={}",groupMoveParam,e);
			throw e;
		}
	}
	@ApiOperation(value="删除分组信息",notes="删除分组信息",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public void deleteGroupInfo(@RequestParam String groupId) throws Exception{
		try {
			groupService.deleteGroupInfo(groupId);
		}catch(Exception e) {
			logger.error("deleteGroupInfo error,groupId={}",groupId,e);
			throw e;
		}
	}
	@ApiOperation(value="查询分组信息",notes="查询分组信息",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/query",method=RequestMethod.GET)
	public List<GroupInfo> queryGroups() throws Exception{
		try {
			return groupService.queryGroupInfo(getUserId());
		}catch(Exception e) {
			logger.error("queryGroups error,userId={}",getUserId(),e);
			throw e;
		}
	}
}
