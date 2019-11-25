package com.gysoft.im.chat.bean.param;

import java.util.Date;

import com.gysoft.bean.page.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Description：获取历史消息所需参数的封装类
 * @author DJZ-HXF
 * @date 2018年8月9日
 */
@Data
public class MessageHistoryParam {
	
	/**
	 * 分页参数
	 */
	@ApiModelProperty("分页参数")
	private Page page;
	/**
	 * 时间分割点
	 */
	@ApiModelProperty("时间分割点")
	private Date divisionTime;
	/**
	 * 请求获取历史消息的具体对象id
	 */
	@ApiModelProperty("请求获取历史消息的具体对象id")
	private String requestId;
}
