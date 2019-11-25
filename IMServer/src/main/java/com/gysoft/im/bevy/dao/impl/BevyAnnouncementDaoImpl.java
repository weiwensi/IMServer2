package com.gysoft.im.bevy.dao.impl;

import com.gysoft.im.bevy.dao.BevyAnnouncementDao;
import com.gysoft.im.bevy.pojo.BevyAnnouncement;
import com.gysoft.utils.jdbc.dao.impl.EntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * @author 周宁
 * @Date 2018-08-08 19:59
 */
@Repository
public class BevyAnnouncementDaoImpl extends EntityDaoImpl<BevyAnnouncement,String> implements BevyAnnouncementDao {
}
