package com.yupi.yuapicommon.service;


import com.yupi.yuapicommon.model.entity.User;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService{


    /**
     * 数据中是否已给用户分配密匙
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
