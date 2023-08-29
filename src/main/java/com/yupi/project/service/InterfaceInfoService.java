package com.yupi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.project.model.entity.InterfaceInfo;

/**
* @author LiuMingyao
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-08-29 09:50:03
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验参数
     * @param interfaceInfo
     * @param add
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
