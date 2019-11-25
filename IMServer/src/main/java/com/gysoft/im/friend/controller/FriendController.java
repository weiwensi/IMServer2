package com.gysoft.im.friend.controller;

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

import com.gysoft.bean.page.PageResult;
import com.gysoft.im.common.core.session.ImSession;
import com.gysoft.im.friend.bean.FriendsInfo;
import com.gysoft.im.friend.bean.UserBriefInfo;
import com.gysoft.im.friend.bean.param.FriendBegHandleParam;
import com.gysoft.im.friend.bean.param.FriendBegParam;
import com.gysoft.im.friend.bean.param.FriendSearchParam;
import com.gysoft.im.friend.service.FriendService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 *
 * @Description：friend controller
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@CrossOrigin
@RestController
@RequestMapping("/friend")
@Api(value="好友",tags="好友")
public class FriendController extends ImSession{
	
	private static final Logger logger = LoggerFactory.getLogger(FriendController.class);
	@Autowired
	private FriendService friendService;
	
	@ApiOperation(value="查询当前用户的好友关系",notes="查询指定用户的好友关系",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/queryFriends",method=RequestMethod.GET)
	public List<FriendsInfo> queryFriendsRelation() throws Exception{
		try {
			return friendService.queryFriendsRelation(getUserId());
		}catch(Exception e) {
			logger.error("queryFriendsRelation userId={}",getUserId(),e);
			throw e;
		}
		
	}
	@ApiOperation(value="添加好友",notes="添加好友",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/addFriend",method=RequestMethod.POST)
	public void addFriend(@RequestBody FriendBegParam friendBegInfo) throws Exception{
		try {
			friendService.addFriend(friendBegInfo,getUserId());
		}catch(Exception e) {
			logger.error("addFriend friendBegInfo={},getUserId={}",friendBegInfo,getUserId(),e);
			throw e;
		}
	}
	@ApiOperation(value="处理添加好友请求",notes="处理添加好友请求",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/handleAddFriendBeg",method=RequestMethod.PUT)
	public void handleAddFriendBeg(@RequestBody FriendBegHandleParam friendBegHandleInfo) throws Exception{
		try {
			friendService.handleAddFriendBeg(friendBegHandleInfo,getUserId());
		}catch(Exception e) {
			logger.error("handleAddFriendBeg friendBegHandleInfo={},userId={}",friendBegHandleInfo,getUserId(),e);
			throw e;
		}
	}
	@ApiOperation(value="搜索好友",notes="搜索好友",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/searchFriends",method=RequestMethod.POST)
	public PageResult<UserBriefInfo> searchFriends(@RequestBody FriendSearchParam friendSearchInfo) throws Exception{
		try {
			return friendService.searchFriends(friendSearchInfo,getUserId());
		}catch(Exception e) {
			logger.error("searchFriends friendSearchInfo={},userId={}",friendSearchInfo,getUserId(),e);
			throw e;
		}
	}
	@ApiOperation(value="修改备注",notes="修改备注",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/updateFriendRemark",method=RequestMethod.GET)
	public void updateFriendRemark(@ApiParam("好友id") @RequestParam String friendId,@ApiParam("备注") @RequestParam String remark) throws Exception{
		try {
			friendService.updateFriendRemark(friendId,remark,getUserId());
		}catch(Exception e) {
			logger.error("updateFriendRemark friendId={},remark={},userId={}",friendId,remark,getUserId(),e);
			throw e;
		}
	}
	@ApiOperation(value="删除好友",notes="删除好友",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/deleteFriend",method=RequestMethod.GET)
	public void deleteFriend(@ApiParam("好友id") @RequestParam String friendId) throws Exception{
		try {
			friendService.deleteFriend(friendId,getUserId());
		}catch(Exception e) {
			logger.error("deleteFriend friendId={},userId={}",friendId,getUserId(),e);
			throw e;
		}
	}
	@ApiOperation(value="移动好友",notes="移动好友",produces=MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value="/removeFriend",method=RequestMethod.GET)
	public void moveFriend(@ApiParam("好友id")@RequestParam String friendId,@ApiParam("分组id")@RequestParam String groupId) throws Exception{
		try {
			friendService.moveFriend(friendId,groupId,getUserId());
		}catch(Exception e) {
			logger.error("moveFriend friendId={},groupId={},userId={}",friendId,groupId,getUserId(),e);
			throw e;
		}
	}
}
