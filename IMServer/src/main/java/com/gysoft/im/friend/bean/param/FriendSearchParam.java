package com.gysoft.im.friend.bean.param;

import com.gysoft.bean.page.Page;
import com.gysoft.bean.page.Sort;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description：好友搜索信息
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Data
public class FriendSearchParam {
	
	/**
	 * 分页参数
	 */
	@ApiModelProperty("分页参数")
	private Page page;
	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Sort sort;
	/**
	 * 搜索好友
	 */
	@ApiModelProperty("搜索")
	private String searchKey;
}
