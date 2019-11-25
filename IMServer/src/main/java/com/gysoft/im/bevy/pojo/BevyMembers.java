package com.gysoft.im.bevy.pojo;

import com.gysoft.utils.jdbc.annotation.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author 周宁
 * @Date 2018-08-08 19:13
 */
@Data
@Table(name = "tb_bevymembers")
public class BevyMembers {
    /**
     * 主键
     */
    private String id;
    /**
     * 群组id
     */
    private String bevyId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 成员在群里备注
     */
    private String remark;
    /**
     * 加入时间
     */
    private Date joinTime;
    /**
     * 0.普通成员,1.子管理员,2.管理员
     */
    private Integer type;
}
