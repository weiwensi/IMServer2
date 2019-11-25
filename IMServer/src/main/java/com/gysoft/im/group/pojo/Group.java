package com.gysoft.im.group.pojo;

import java.util.Date;

import com.gysoft.utils.jdbc.annotation.Table;

import lombok.Data;

/**
 *
 * @Description：分组
 * @author DJZ-HXF
 * @date 2018年8月8日
 */
@Table(name="tb_group",pk="id")
@Data
public class Group {
	
	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 组名称
	 */
	private String groupName;
	/**
	 * 谁的分组
	 */
	private String belong;
	/**
	 * 排序 
	 */
	private int sort;
	/**
	 * 前一分组id
	 */
	private String last;
	/**
	 * 后一分组id
	 */
	private String next;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
}
