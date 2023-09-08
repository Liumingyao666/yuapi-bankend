package com.yupi.yuapicommon.service;

import com.yupi.yuapicommon.model.entity.UserInterfaceInfo;

/**
* @author LiuMingyao
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-09-01 08:40:30
*/
public interface InnerUserInterfaceInfoService {


    /**
     * 用户调用接口统计
     *
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCount(long userId, long interfaceInfoId);

}
