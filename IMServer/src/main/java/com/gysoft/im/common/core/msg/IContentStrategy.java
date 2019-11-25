package com.gysoft.im.common.core.msg;


/**
 * 消息内容生成策略
 *
 * @author 周宁
 * @Date 2018-08-10 17:22
 */
public interface IContentStrategy<T> {


    /**
     * 生成内容
     *
     * @return
     */
    String generateContent(T t)throws Exception;
}
