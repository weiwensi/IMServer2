package com.gysoft.im.bevy.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author 周宁
 * @Date 2018-08-08 19:58
 */
@Data
@Table(name = "tb_bevy_announcement")
public class BevyAnnouncement {
    /**
     * 主键
     */
    private String id;
    /**
     * 群id
     */
    private String bevyId;
    /**
     * 公告内容
     */
    private String content;
    /**
     * 公告时间
     */
    private Date createTime;
    /**
     * 发布人id
     */
    private String userId;
}
