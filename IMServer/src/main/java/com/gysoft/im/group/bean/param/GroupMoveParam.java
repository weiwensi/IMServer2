package com.gysoft.im.group.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GroupMoveParam {
	
	/**
	 * 前一个分组id（不能为空）
	 */
	@ApiModelProperty(name="last",value=" 前一个分组id（不能为空）",required=true)
	private String last;
	/**
	 * 后一个分组id（可以为空）
	 */
	@ApiModelProperty(name="next",value="后一个分组id")
	private String next;
	/**
	 * 当前分组的id
	 */
	@ApiModelProperty(value="当前分组的id",required=true)
	private String currentId;
}
