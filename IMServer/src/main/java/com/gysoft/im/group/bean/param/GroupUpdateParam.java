package com.gysoft.im.group.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description：修改分组时，需要的信息
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Data
public class GroupUpdateParam {
	
	/**
	 * 分组名称
	 */
	@ApiModelProperty("分组名称")
	private String groupName;
	/**
	 * 待修改的分组Id
	 */
	@ApiModelProperty("待修改的分组Id")
	private String groupId;
}
