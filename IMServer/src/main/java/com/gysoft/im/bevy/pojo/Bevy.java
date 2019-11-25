package com.gysoft.im.bevy.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author 周宁
 * @Date 2018-08-08 17:30
 */
@Data
@Table(name = "tb_bevy")
public class Bevy {

    private String id;
    /**
     * 群名称
     */
    private String bevyName;
    /**
     * 群主
     */
    private String belongTo;
    /**
     * 群图标
     */
    private String icon;
    /**
     * 群描述
     */
    private String remark;
    /**
     * 群成员人数上限
     */
    private Integer upperLimit;
    /**
     * 群创建时间
     */
    private Date createTime;
    /**
     * 群更新时间
     */
    private Date updateTime;

}
