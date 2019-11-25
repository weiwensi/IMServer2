package com.gysoft.im.group.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupInfo {
	
	/**
	 * 分组id
	 */
	@ApiModelProperty("分组id")
	private String id;
	/**
	 * 分组名
	 */
	@ApiModelProperty("分组名")
	private String groupName;
	
}
