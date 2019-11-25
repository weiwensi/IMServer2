package com.gysoft.im.common.core.session;

public interface ImSessionService {

	/**
	 * 
	 * @Description：修改登出记录
	 * @author DJZ-HXF
	 * @date 2018年8月10日
	 * @param userId
	 * @return void
	 * @throws Exception
	 */
	void updateLogoutRecord(String userId) throws Exception;

}
