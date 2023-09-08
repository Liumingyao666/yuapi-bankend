package com.yupi.yuapicommon.service;

import com.yupi.yuapicommon.model.entity.InterfaceInfo;

/**
* @author LiuMingyao
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-08-29 09:50:03
*/
public interface InnerInterfaceInfoService{


    /**
     * 从数据库中查出模拟接口是否存在
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
