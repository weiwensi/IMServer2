package com.gysoft.im.common.core.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author 周宁
 * @Date 2018-08-08 11:39
 */
@Data
@Configuration
@DisconfFile(filename = "user_acl_template.json",targetDirPath = "com/gysoft/im/common/core/disconf")
public class UserAclTemplateConfig {


}
